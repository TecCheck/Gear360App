package io.github.teccheck.gear360app.live;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class VideoThread extends Thread {

    private static final String TAG = "VideoThread";

    CustomMediaCodec videoDecoder = null;
    MediaExtractor mediaExtractor;

    ByteBuffer audioBuffer = null;
    private final HashMap<Long, VrotData> vrotList = new HashMap<>();

    String filepath = "";
    Surface surface;

    boolean hasReleased = false;
    boolean isVideoDecoderThreadStopped;

    long videoPresentationTime = 0;
    long decoderEntryIndex = 0;
    long decoderOutIndex = 0;

    public static long cnt = 0;

    int channel = 0;
    int sampleRate = 0;

    private int sampleSize;

    public void init(Surface surface, String filePath) {
        this.surface = surface;
        filepath = filePath;
        hasReleased = false;
        videoPresentationTime = 0;
    }

    private MediaFormat makeAACCodecSpecificData(int audioProfile, int sampleRate, int channelConfig) {
        MediaFormat format = new MediaFormat();
        format.setString("mime", "audio/mp4a-latm");
        format.setInteger("sample-rate", sampleRate);
        format.setInteger("channel-count", channelConfig);

        int[] samplingFreq = new int[]{96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000};

        int sampleIndex = -1;
        for (int i = 0; i < samplingFreq.length; i++) {
            if (samplingFreq[i] == sampleRate) {
                sampleIndex = i;
            }
        }

        if (sampleIndex == -1) {
            return null;
        }

        ByteBuffer csd = ByteBuffer.allocate(2);
        csd.put((byte) ((audioProfile << 3) | (sampleIndex >> 1)));
        csd.position(1);
        csd.put((byte) (((byte) ((sampleIndex << 7) & 128)) | (channelConfig << 3)));
        csd.flip();
        format.setByteBuffer("csd-0", csd);
        return format;
    }

    public synchronized void releaseEveryThing() {
        if (!hasReleased) {
            hasReleased = true;

            if (videoDecoder != null) {
                videoDecoder.stop();
                videoDecoder.release();
            }

            if (mediaExtractor != null) {
                try {
                    mediaExtractor.release();
                } catch (Exception e) {
                }
            }

            if (audioBuffer != null) {
                audioBuffer.clear();
            }
            /////////// LVBTsMuxer.getInstance().stopTsMuxer();
        }
    }

    private void decodeAndPlay() throws IOException {
        ByteBuffer[] videoInputBuffers = videoDecoder.getInputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

        int outputTimeoutCount = 0;
        isVideoDecoderThreadStopped = false;

        while (!isVideoDecoderThreadStopped) {
            int trackIndex = mediaExtractor.getSampleIndex();

            if (trackIndex < 0) {
                Log.w(TAG, "Invalid track index > 0");
                return;
            } else if (!mediaExtractor.isKeyFrame() && decoderEntryIndex - decoderOutIndex > 10) {
                Log.w(TAG, "Frame drop");
                mediaExtractor.dropFrame();
                mediaExtractor.advance();

            } else if (trackIndex == 300) {
                // Vrot data

                VrotData vrotData = mediaExtractor.getVrotData();
                vrotList.put(vrotData.getTimeStamp(), vrotData);
                mediaExtractor.advance();
            } else if (trackIndex == 100) {
                // Video data

                int videoInIndex = videoDecoder.dequeueInputBuffer(5000);
                if (videoInIndex >= 0) {
                    ByteBuffer buffer = videoInputBuffers[videoInIndex];
                    sampleSize = mediaExtractor.readFrameSampleData(buffer);

                    if (sampleSize < 0) {
                        videoDecoder.queueInputBuffer(videoInIndex, 0, 0, 0, 4);

                    } else {
                        videoPresentationTime = mediaExtractor.getSampleTime();
                        videoDecoder.queueInputBuffer(videoInIndex, 0, sampleSize, videoPresentationTime, 0);
                        mediaExtractor.advance();
                        decoderEntryIndex++;
                    }
                }

                int outIndex = videoDecoder.dequeueOutputBuffer(info, 10000);
                switch (outIndex) {
                    case -3:
                    case -2:
                        break;
                    case -1:
                        outputTimeoutCount++;
                        break;
                    default:
                        videoDecoder.releaseOutputBuffer(outIndex, true);
                        decoderOutIndex++;
                        cnt++;
                        break;
                }
                if ((info.flags & 4) != 0) {
                    return;
                }
            } else if (trackIndex == 200) {
                // Audio data

                audioBuffer.clear();
                sampleSize = mediaExtractor.readFrameSampleData(audioBuffer);
                long sampleTime = mediaExtractor.getSampleTime();
                if (sampleSize < 0) {
                    info.set(0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                } else {
                    info.set(0, sampleSize, sampleTime, MediaCodec.BUFFER_FLAG_KEY_FRAME);
                    mediaExtractor.advance();
                }
            } else {
                Log.w(TAG, "Invalid track index: " + trackIndex);
            }
        }
    }

    public void run() {
        mediaExtractor = new MediaExtractor();

        if (!mediaExtractor.setDataSource(filepath))
            return;

        MediaFormat videoFormat = mediaExtractor.getVideoFormat();
        if (videoFormat == null)
            return;

        String videoMIME = videoFormat.getString("mime");
        Log.d(TAG, "Video mime type: " + videoMIME);

        try {
            videoDecoder = new CustomMediaCodec(videoMIME);
            videoDecoder.configure(videoFormat, surface, null, 0);
            videoDecoder.start();

            MediaFormat audioFormat = mediaExtractor.getAudioFormat();

            if (audioFormat != null) {
                String audioMIME = audioFormat.getString("mime");
                Log.d(TAG, "Audio mime type: " + audioMIME);

                sampleRate = audioFormat.getInteger("sample-rate");
                channel = audioFormat.getInteger("channel-count");

                audioFormat.setInteger("aac-profile", 2);
                audioFormat = makeAACCodecSpecificData(2, sampleRate, channel);
                //convertingCallback.setAudioFormat(audioFormat);
                if (audioBuffer == null) {
                    audioBuffer = ByteBuffer.allocate(4096);
                }
            }

            decodeAndPlay();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}