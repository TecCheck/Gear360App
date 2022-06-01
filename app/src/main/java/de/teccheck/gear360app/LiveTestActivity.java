package de.teccheck.gear360app;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;


public class LiveTestActivity extends AppCompatActivity {

    SurfaceView surfaceView = null;
    VideoThread videoThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_test);

        surfaceView = findViewById(R.id.surfaceView);
        Surface surface = surfaceView.getHolder().getSurface();
        String file = "http://192.168.107.1:7679/livestream_high.avi";
        ConvertingCallback callback = new ConvertingCallback() {

            MediaFormat format = null;

            @Override
            public MediaFormat getAudioFormat() {
                return format;
            }

            @Override
            public void muxerStopped() {

            }

            @Override
            public void setAudioData(ByteBuffer var1, MediaCodec.BufferInfo var2) {

            }

            @Override
            public void setAudioFormat(MediaFormat var1) {
                format = var1;
            }
        };

        videoThread = new VideoThread();
        videoThread.init(surface, file, callback);
        videoThread.setName("Video Thread");
        videoThread.start();

        /*
        Uri uri = new Uri.Builder().path(file).build();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.USE_DEFAULT_STREAM_TYPE);
        //mediaPlayer.start();

        SimpleExoPlayer exoPlayer;
        SimpleExoPlayerView exoPlayerView =  findViewById(R.id.playerView);
        //String videoURL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            Uri videoURI = Uri.parse(file);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }catch (Exception e){
            Log.e("MainAcvtivity"," exoplayer error "+ e.toString());
        }
        */
    }

    @Override
    public void onBackPressed(){
        if(videoThread != null){
            videoThread.releaseEveryThing();
            videoThread.interrupt();
            videoThread = null;
            //videoThread.stop();
        }

        super.onBackPressed();
    }

    static class VideoThread extends Thread{

        CustomMediaCodec videoDecoder = null;
        MediaExtractor mediaExtractor;
        ConvertingCallback convertingCallback = null;
        VideoTimeStampUpdater videoTimeStampUpdater = null;
        ByteBuffer audioBuffer = null;
        private HashMap<Long, VrotData> vrotList;
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

        public void init(Surface surface, String filePath, ConvertingCallback convertingCallback) {
            this.surface = surface;
            this.filepath = filePath;
            this.hasReleased = false;
            this.convertingCallback = convertingCallback;
            this.videoPresentationTime = 0;
            this.vrotList = new HashMap();
        }

        public void init(Surface surface, String filePath, ConvertingCallback convertingCallback, VideoTimeStampUpdater mVideoTimeStampUpdater) {
            this.surface = surface;
            this.filepath = filePath;
            this.hasReleased = false;
            this.convertingCallback = convertingCallback;
            this.videoPresentationTime = 0;
            this.videoTimeStampUpdater = mVideoTimeStampUpdater;
            this.vrotList = new HashMap();
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

        private synchronized void releaseEveryThing() {
            if (!this.hasReleased) {
                this.hasReleased = true;
                if (this.videoDecoder != null) {
                    this.videoDecoder.stop();
                    this.videoDecoder.release();
                }
                if (this.mediaExtractor != null) {
                    try {
                        this.mediaExtractor.release();
                    } catch (Exception e) {
                    }
                }
                if (this.audioBuffer != null) {
                    this.audioBuffer.clear();
                }
                /////////// LVBTsMuxer.getInstance().stopTsMuxer();
            }
        }

        private void decodeAndPlay() throws IOException {
            ByteBuffer[] videoInputBuffers = this.videoDecoder.getInputBuffers();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            int outputTimeoutCount = 0;
            isVideoDecoderThreadStopped = false;

            while (!isVideoDecoderThreadStopped){
                int trackIndex = mediaExtractor.getSampleIndex();
                Log.w(MainActivity.TAG, "track index: " + trackIndex);
                if(trackIndex < 0){
                    return;
                }else if(!mediaExtractor.isKeyFrame() && decoderEntryIndex - decoderOutIndex > 10){
                    Log.d(MainActivity.TAG, "Frame Drop");
                    mediaExtractor.dropFrame();
                    mediaExtractor.advance();
                }else if(trackIndex == 300){
                    Log.d(MainActivity.TAG, "Vrot Data");
                    VrotData vrotData = mediaExtractor.getVrotData();
                    vrotList.put(Long.valueOf(vrotData.getTimeStamp()), vrotData);
                    mediaExtractor.advance();
                }else if(trackIndex == 100){
                    Log.d(MainActivity.TAG, "Video Data");
                    int videoInIndex = this.videoDecoder.dequeueInputBuffer(5000);
                    if (videoInIndex >= 0) {
                        ByteBuffer buffer = videoInputBuffers[videoInIndex];
                        sampleSize = this.mediaExtractor.readFrameSampleData(buffer);
                        if (sampleSize < 0) {
                            this.videoDecoder.queueInputBuffer(videoInIndex, 0, 0, 0, 4);
                        } else {
                            this.videoPresentationTime = this.mediaExtractor.getSampleTime();
                            if (this.videoTimeStampUpdater != null) {
                                this.videoTimeStampUpdater.setVideoTimeStamp(this.videoPresentationTime);
                            }
                            /*is 4K live mode
                            if (LiveEventUtils.is4KLiveModeOn) {
                                LVBTsMuxer.getInstance().addTsVideoTrack(this.mMediaExtractor.getVideoFormat(), buffer);
                                LVBTsMuxer.getInstance().addTsAudioTrack(this.mConvertingCallback.getAudioFormat());
                                int offset = this.mMediaExtractor.isKeyFrame() ? LVBTsMuxer.getInstance().getCsdLength() : 0;
                                info.set(offset, sampleSize - offset, this.mVideoPresentationTime, this.mMediaExtractor.isKeyFrame() ? 1 : 0);
                                LVBTsMuxer.getInstance().writeTsVideoFrame(buffer, offset, sampleSize, info);
                            }
                            */
                            this.videoDecoder.queueInputBuffer(videoInIndex, 0, sampleSize, this.videoPresentationTime, 0);
                            this.mediaExtractor.advance();
                            this.decoderEntryIndex++;
                        }
                    }
                    int outIndex = this.videoDecoder.dequeueOutputBuffer(info, 10000);
                    switch (outIndex) {
                        case -3:
                            break;
                        case -2:
                            break;
                        case -1:
                            if (outputTimeoutCount < 15 || outputTimeoutCount % 15 == 0) {
                            }
                            outputTimeoutCount++;
                            break;
                        default:
                            this.videoDecoder.releaseOutputBuffer(outIndex, true);
                            this.decoderOutIndex++;
                            cnt++;
                            break;
                    }
                    if ((info.flags & 4) != 0) {
                        return;
                    }
                }else if(trackIndex == 200){
                    Log.d(MainActivity.TAG, "Audio Data");
                    this.audioBuffer.clear();
                    sampleSize = this.mediaExtractor.readFrameSampleData(this.audioBuffer);
                    long sampleTime = this.mediaExtractor.getSampleTime();
                    if (sampleSize < 0) {
                        info.set(0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    } else {
                        info.set(0, sampleSize, sampleTime, MediaCodec.BUFFER_FLAG_KEY_FRAME);
                        this.mediaExtractor.advance();
                    }
                    /*is 4K live mode
                    if (LiveEventUtils.is4KLiveModeOn) {
                        LVBTsMuxer.getInstance().writeTsAudioFrame(this.audioBuffer, info);
                    }
                    */
                    this.convertingCallback.setAudioData(this.audioBuffer, info);
                }else {
                    Log.w(MainActivity.TAG, "invalid track index: " + trackIndex);
                }
            }
        }

        public void run(){
            mediaExtractor = new MediaExtractor();
            if(mediaExtractor.setDataSource(filepath)){
                MediaFormat videoFormat = mediaExtractor.getVideoFormat();
                Log.i(MainActivity.TAG, "VideoFormat: " + videoFormat);
                if(videoFormat == null)
                    return;
                String videoMIME = videoFormat.getString("mime");

                try {
                    videoDecoder = new CustomMediaCodec(videoMIME);
                    videoDecoder.configure(videoFormat, surface, null, 0);
                    videoDecoder.start();
                    MediaFormat audioFormat = mediaExtractor.getAudioFormat();
                    Log.i(MainActivity.TAG, "AudioFormat: " + audioFormat);

                    if(audioFormat != null){
                        sampleRate = audioFormat.getInteger("sample-rate");
                        channel = audioFormat.getInteger("channel-count");
                        String audioMIME = audioFormat.getString("mime");
                        audioFormat.setInteger("aac-profile", 2);
                        audioFormat = makeAACCodecSpecificData(2, sampleRate, channel);
                        convertingCallback.setAudioFormat(audioFormat);
                        if(audioBuffer == null){
                            audioBuffer = ByteBuffer.allocate(4096);
                        }
                    }

                    decodeAndPlay();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    static class CustomMediaCodec{

        private MediaCodec mediaCodec;
        private boolean isStop;

        public CustomMediaCodec(String videoMIME) throws IOException {
            mediaCodec = MediaCodec.createDecoderByType(videoMIME);
            isStop = false;
        }

        public void configure(@Nullable MediaFormat var1, @Nullable Surface var2, @Nullable MediaCrypto var3, int var4) {
            synchronized(this){}

            try {
                if (this.isStop) {
                } else {
                    this.mediaCodec.configure(var1, var2, var3, var4);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public ByteBuffer[] getInputBuffers() {
            synchronized(this){}

            return mediaCodec.getInputBuffers();
        }

        public final void start() {
            synchronized(this){}

            try {
                this.mediaCodec.start();
                this.isStop = false;
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        public final void stop() {
            synchronized(this){}

            try {
                if (this.isStop) {
                } else {
                    try {
                        this.setIsStop(true);
                        this.mediaCodec.stop();
                    } catch (IllegalStateException var4) {
                    }
                }
            } finally {
            }

        }

        public final void release() {
            synchronized(this){}
            try {
                this.mediaCodec.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public final void queueInputBuffer(int var1, int var2, int var3, long var4, int var6) throws MediaCodec.CryptoException {
            if (this.isStop) {
            } else {
                try {
                    this.mediaCodec.queueInputBuffer(var1, var2, var3, var4, var6);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

        }

        public final int dequeueInputBuffer(long var1) {
            synchronized(this){}
            return mediaCodec.dequeueInputBuffer(var1);
        }

        public int dequeueOutputBuffer(MediaCodec.BufferInfo info, int i) {
            synchronized(this){}
            return mediaCodec.dequeueOutputBuffer(info, i);
        }

        public void releaseOutputBuffer(int outIndex, boolean b) {
            synchronized(this){}
            mediaCodec.releaseOutputBuffer(outIndex, b);
        }

        public void setIsStop(boolean var1) {
            this.isStop = var1;
        }
    }

    public static class VrotData {
        private float mPitch;
        private float mRoll;
        private long mTimeStamp;
        private float mYaw;

        public VrotData() {
            this.mYaw = 0.0F;
            this.mPitch = 0.0F;
            this.mRoll = 0.0F;
            this.mTimeStamp = 0L;
        }

        public VrotData(float var1, float var2, float var3) {
            this.mYaw = var1;
            this.mPitch = var2;
            this.mRoll = var3;
            this.mTimeStamp = 0L;
        }

        public VrotData(long var1, float var3, float var4, float var5) {
            this.mYaw = var3;
            this.mPitch = var4;
            this.mRoll = var5;
            this.mTimeStamp = var1;
        }

        public boolean compare(VrotData var1) {
            boolean var2;
            if (this.mYaw == var1.mYaw && this.mPitch == var1.mPitch && this.mRoll == var1.mRoll && this.mTimeStamp == var1.mTimeStamp) {
                var2 = true;
            } else {
                var2 = false;
            }

            return var2;
        }

        public float getPitch() {
            return this.mPitch;
        }

        public float getRoll() {
            return this.mRoll;
        }

        public long getTimeStamp() {
            return this.mTimeStamp;
        }

        public float getYaw() {
            return this.mYaw;
        }

        public void set(float var1, float var2, float var3) {
            this.mYaw = var1;
            this.mPitch = var2;
            this.mRoll = var3;
        }

        public void set(long var1, float var3, float var4, float var5) {
            this.mYaw = var3;
            this.mPitch = var4;
            this.mRoll = var5;
            this.mTimeStamp = var1;
        }

        public String toString() {
            return "TimeStamp: " + this.mTimeStamp + "  Yaw: " + this.mYaw + " Pitch: " + this.mPitch + " Roll: " + this.mRoll;
        }
    }

    interface ConvertingCallback {
        MediaFormat getAudioFormat();

        void muxerStopped();

        void setAudioData(ByteBuffer var1, MediaCodec.BufferInfo var2);

        void setAudioFormat(MediaFormat var1);
    }

    interface VideoTimeStampUpdater {
        void setVideoTimeStamp(long var1);
    }
}