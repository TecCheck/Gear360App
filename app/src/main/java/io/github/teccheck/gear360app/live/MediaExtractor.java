package io.github.teccheck.gear360app.live;

import android.media.MediaFormat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// This extracts video, audio and other data (vrot) from the stream
public class MediaExtractor {

    private static final String TAG = "MediaExtractor";

    private static final int BUFFER_SIZE = 786432;
    private static final String TTT_FILE_TAG = "TTTS";

    private static final String VIDEO_CODEC_UNKNOWN = "video/unknown";
    private static final String AUDIO_CODEC_UNKNOWN = "audio/unknown";

    private static final String VIDEO_FRAME_START_TAG = "00VD";
    private static final String AUDIO_FRAME_START_TAG = "00AU";
    private static final String VROT_FRAME_START_TAG = "00VR";

    Socket socket;
    BufferedOutputStream outputStream;
    BufferedInputStream inputStream;

    private MediaFormat videoFormat = new MediaFormat();
    private MediaFormat audioFormat = new MediaFormat();

    private boolean isEndOfStream;
    private boolean shouldAdvanced = false;

    private int frameIndicator;
    private int headerSize = 0;
    private int videoBitRate = 1;
    private int videoGOP = 1;
    private int videoTimeStampScale = 1;
    private int audioTimeStampScale;
    private int videoMaxBufferSize = -1;
    private int frameSize = 0;
    private int frameType = 2;
    private int videoFrameCount = 0;
    private int startMs;
    private int currentMs;

    private long presentationMs = 0;
    private long frameTimeStamp;

    private float fps = 0.0f;
    private float pitch = 0.0f;
    private float roll = 0.0f;
    private float yaw = 0.0f;

    private byte[] frameHeader = new byte[5];
    private byte[] headers = new byte[250];
    private byte[] buffer = new byte[BUFFER_SIZE];

    private VrotData vrotData;

    public void release() {
        isEndOfStream = true;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        buffer = null;
        headers = null;
        frameHeader = null;
        System.gc();
    }

    public void advance() {
        shouldAdvanced = true;
    }

    public boolean isKeyFrame() {
        return frameType == 64 || frameType == 38;
    }

    public boolean setDataSource(String url) {
        try {
            URL urlObj = new URL(url);

            String host = urlObj.getHost();
            String file = urlObj.getFile();
            int port = urlObj.getPort();

            if (socket == null) {
                socket = new Socket(Proxy.NO_PROXY);
                socket.setReceiveBufferSize(BUFFER_SIZE);
            }

            String s = "GET " + file + " HTTP/1.1\r\nUser-Agent: Android Linux\r\nHost: " + host + ":" + port + "\r\nConnection: Keep-Alive\r\n\r\n";

            Log.i(TAG, "\r\n" + s);
            socket.setSoTimeout(0);
            socket.connect(new InetSocketAddress(host, port));
            outputStream = new BufferedOutputStream(socket.getOutputStream());
            outputStream.write(s.getBytes());
            outputStream.flush();

            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }

            inputStream = new BufferedInputStream(socket.getInputStream(), BUFFER_SIZE);

            String h = readHeader();
            Log.i(TAG, "readHeader(): " + h);

            if (readMetaData() < 0) {
                return false;
            }

            shouldAdvanced = true;
            startMs = 0;
            currentMs = 0;
            presentationMs = 0;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            isEndOfStream = true;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return false;
    }

    public int dropFrame() throws IOException {
        int targetSize = frameSize - 5;
        int totalReadCount = 0;

        while (targetSize > 0) {
            if (isEndOfStream) {
                return totalReadCount;
            }

            int readCount;
            if (targetSize < BUFFER_SIZE) {
                readCount = inputStream.read(buffer, 0, targetSize);
            } else {
                readCount = inputStream.read(buffer);
            }

            if (readCount < 0) {
                isEndOfStream = true;
                return -1;
            }

            targetSize -= readCount;
            totalReadCount += readCount;
        }

        return totalReadCount;
    }

    public int readFrameSampleData(ByteBuffer input) {
        if (input == null) {
            return -1;
        }
        try {
            return readFrame(input);
        } catch (Throwable e) {
            return -1;
        }
    }

    public int getSampleIndex() {
        if (shouldAdvanced) {
            readStream();
            readFrameHead();
        }
        return frameIndicator;
    }

    public long getSampleTime() {
        if (frameIndicator == 100) {
            return (frameTimeStamp * 1000000) / ((long) videoTimeStampScale);
        }
        return (frameTimeStamp * 1000000) / ((long) audioTimeStampScale);
    }

    public MediaFormat getVideoFormat() {
        return videoFormat;
    }

    public MediaFormat getAudioFormat() {
        return audioFormat;
    }

    public VrotData getVrotData() {
        return vrotData;
    }

    private String readHeader() {
        StringBuilder hd = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        do {
            try {
                char c = (char) inputStream.read();
                hd.append(c);
                if (c == '\r' || c == '\n') {
                    temp.append(c);
                } else {
                    temp = new StringBuilder();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } while (!temp.toString().contentEquals("\r\n\r\n"));
        return hd.toString();
    }

    private String readByteToString() {
        int loop = 0;
        try {
            byte[] temp = new byte[4];
            while (loop < 4) {
                int btemp = inputStream.read();
                if (btemp < 0) {
                    isEndOfStream = true;
                    frameIndicator = -100;
                    break;
                }
                temp[loop] = (byte) btemp;
                loop++;
            }
            if (loop == 4) {
                return new String(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            isEndOfStream = true;
        }
        return null;
    }

    private int readMetaData() {
        int width = 0;
        int height = 0;
        int videoCodecType = 0;
        int channel = 0;
        int audioCodecType = -1;
        int sampleRate = 0;
        int type = 0;

        boolean stopRead = false;

        do {
            String header = readByteToString();
            Log.d(TAG, "Header: " + header);
            if (header == null) {
                headerSize = -1;
            } else {
                int size;
                String read;
                int version;
                String caseName;

                switch (header) {
                    case TTT_FILE_TAG:
                        Log.d(TAG, "case TTT");
                        size = readByteToInt();
                        type = readByteToInt();
                        headerSize += 12;

                    case "VID0":
                        Log.d(TAG, "case VID0");

                        caseName = readByteToString();
                        size = readByteToInt();

                        int transblocknumber = readByteToInt();
                        int transblocktype = readByteToInt();
                        int encodeWidth = readByteToInt();
                        int encodeHeight = readByteToInt();

                        videoCodecType = readByteToInt();
                        videoBitRate = readByteToInt();
                        videoGOP = readByteToInt();

                        int goptype = readByteToInt();

                        videoTimeStampScale = readByteToInt();

                        read = readByteToString();
                        Log.d(TAG, "Read: " + read);

                        if (read == null || !read.contains("VD00")) {
                            headerSize = -1;
                            stopRead = true;
                        } else {
                            version = readByteToInt();
                            fps = ((float) readByteToInt()) / ((float) readByteToInt());

                            float horiAngle = ((float) readByteToInt()) / ((float) readByteToInt());
                            float vertiAngle = ((float) readByteToInt()) / ((float) readByteToInt());
                            float lensInfo = ((float) readByteToInt()) / ((float) readByteToInt());

                            width = readByteToInt();
                            height = readByteToInt();

                            int posX = readByteToInt();
                            readByteToInt();
                            headerSize += size + 4;
                        }
                        
                    case "AUD0":
                        Log.d(TAG, "case AUD0");
                        caseName = readByteToString();
                        size = readByteToInt();
                        channel = readByteToInt();

                        int nAudioInfo = readByteToInt();
                        audioTimeStampScale = readByteToInt();

                        read = readByteToString();
                        Log.d(TAG, "Read: " + read);

                        Log.d(TAG, "case AUD0 if statement incomming");
                        if (read == null || !read.contains("AU00")) {
                            Log.d(TAG, "into the if");
                            headerSize = -1;
                            stopRead = true;
                            //continue;
                        } else {
                            Log.i(TAG, "into the else");
                            version = readByteToInt();
                            audioCodecType = readByteToInt();

                            int nAudioBps = readByteToInt();
                            int nAChannel = readByteToInt();
                            int horiAngleNumCh1 = readByteToInt();
                            float horiAngleCh1 = ((float) horiAngleNumCh1) / ((float) readByteToInt());
                            int horiAngleNumCh2 = readByteToInt();
                            float horiAngleCh2 = ((float) horiAngleNumCh1) / ((float) readByteToInt());
                            int sampleRateCh1 = readByteToInt();
                            int virtAngleNumCh1 = readByteToInt();
                            float virtAngleCh1 = ((float) virtAngleNumCh1) / ((float) readByteToInt());
                            int virtAngleNumCh2 = readByteToInt();
                            float virtAngleCh2 = ((float) virtAngleNumCh1) / ((float) readByteToInt());
                            int sampleRateCh2 = readByteToInt();

                            sampleRate = sampleRateCh1;
                            headerSize += size + 4;
                            //continue;
                        }
                        
                    case "VRO0":
                        Log.d(TAG, "case VRO0");
                        caseName = readByteToString();
                        size = readByteToInt();

                        int nVRChannel = readByteToInt();

                        read = readByteToString();
                        Log.d(TAG, "Read: " + read);

                        if (read == null || !read.contains("VR00")) {
                            headerSize = -1;
                            stopRead = true;
                            //continue;
                        } else {
                            version = readByteToInt();
                            readByteToInt();
                            headerSize += size + 4;
                            //continue;
                        }
                        
                    case "LIST":
                        Log.d(TAG, "case LIST");
                        headerSize += 8;
                        
                    case "movi":
                        Log.d(TAG, "case movi");
                        headerSize += 4;
                        stopRead = true;
                        break;
                        
                    default:
                        Log.d(TAG, "default");
                        headerSize = -1;
                        stopRead = true;
                }

                Log.d(TAG, "Widht: " + width + ", Height: " + height + ", VideoCodecType: " + videoCodecType + ", AudioCodecType: " + audioCodecType);
                Log.d(TAG, "Channel: " + channel + ", Sample Rate: " + sampleRate + ", Type: " + type + ", HeaderSize: " + headerSize);

                if ((type & 1) != 0) {
                    // Has video data
                    videoFormat.setInteger("width", width);
                    videoFormat.setInteger("height", height);
                    Log.d(TAG, "videoCodecType: " + videoCodecType);
                    switch (videoCodecType) {
                        case 0:
                            videoFormat.setString("mime", "video/raw");
                            break;
                        case 1:
                            videoFormat.setString("mime", "video/hevc");
                            break;
                        case 2:
                            break;
                        default:
                            videoFormat.setString("mime", VIDEO_CODEC_UNKNOWN);
                            break;
                    }
                    videoFormat.setInteger("bitrate", videoBitRate);
                    videoFormat.setInteger("i-frame-interval", videoGOP);
                    videoFormat.setFloat("frame-rate", fps);
                    videoMaxBufferSize = 1000000;
                    videoFormat.setInteger("max-input-size", videoMaxBufferSize);
                    videoFormat.setLong("durationUs", 0);
                } else {
                    videoFormat = null;
                }

                if ((type & 2) != 0) {
                    // Has audio data
                    audioFormat.setInteger("channel-count", channel);
                    Log.d(TAG, "audioCodecType: " + audioCodecType);
                    switch (audioCodecType) {
                        case 0:
                            audioFormat.setString("mime", "audio/raw");

                            break;
                        case 1:
                            audioFormat.setString("mime", "audio/mp4a-latm");
                            break;
                        default:
                            audioFormat.setString("mime", AUDIO_CODEC_UNKNOWN);
                            break;
                    }
                    audioFormat.setInteger("sample-rate", sampleRate);
                    audioFormat.setLong("durationUs", 0);
                } else {
                    audioFormat = null;
                }
                return headerSize;
            }

        } while (!stopRead);

        if ((type & 1) == 0) {
            videoFormat = null;
        } else {
            videoFormat.setInteger("width", width);
            videoFormat.setInteger("height", height);
            Log.d(TAG, "videoCodecType: " + videoCodecType);
            switch (videoCodecType) {
                case 0:
                    videoFormat.setString("mime", "video/raw");
                    break;
                case 1:
                    videoFormat.setString("mime", "video/hevc");
                    break;
                case 2:
                    break;
                default:
                    videoFormat.setString("mime", VIDEO_CODEC_UNKNOWN);
                    break;
            }
            videoFormat.setInteger("bitrate", videoBitRate);
            videoFormat.setInteger("i-frame-interval", videoGOP);
            videoFormat.setFloat("frame-rate", fps);
            videoMaxBufferSize = 1000000;
            videoFormat.setInteger("max-input-size", videoMaxBufferSize);
            videoFormat.setLong("durationUs", 0);
        }

        if ((type & 2) == 0) {
            audioFormat = null;
        } else {
            audioFormat.setInteger("channel-count", channel);
            Log.d(TAG, "audioCodecType: " + audioCodecType);
            switch (audioCodecType) {
                case 0:
                    audioFormat.setString("mime", "audio/raw");

                    break;
                case 1:
                    audioFormat.setString("mime", "audio/mp4a-latm");
                    break;
                default:
                    audioFormat.setString("mime", AUDIO_CODEC_UNKNOWN);
                    break;
            }
            audioFormat.setInteger("sample-rate", sampleRate);
            audioFormat.setLong("durationUs", 0);
        }
        
        Log.d(TAG, "videoFormat: " + videoFormat);
        Log.d(TAG, "audioFormat: " + audioFormat);
        
        return headerSize;
    }

    private int readByteToInt() {
        int data = 0;
        int loop = 0;
        while (loop < 4) {
            try {
                int btemp = inputStream.read();
                if (btemp < 0) {
                    isEndOfStream = true;
                    frameIndicator = -100;
                    break;
                }
                data = (int) (((long) (data << 8)) + ((long) btemp));
                loop++;
            } catch (Exception e) {
                e.printStackTrace();
                isEndOfStream = true;
            }
        }
        if (loop == 4) {
            return data;
        }
        return -1;
    }

    private synchronized void readFrameHead() {
        try {
            frameSize = readByteToInt();
            long timestamp = parseLongFromByte(new byte[]{(byte) inputStream.read(), (byte) inputStream.read(), (byte) inputStream.read(), (byte) inputStream.read(), (byte) inputStream.read(), (byte) inputStream.read(), (byte) inputStream.read(), (byte) inputStream.read()});
            frameTimeStamp = timestamp;
            presentationMs = (1000 * timestamp) / ((long) videoTimeStampScale);

            if (frameIndicator == 300) {
                yaw = ((float) readByteToInt()) / ((float) readByteToInt());
                pitch = ((float) readByteToInt()) / ((float) readByteToInt());
                roll = ((float) readByteToInt()) / ((float) readByteToInt());
                vrotData = new VrotData((1000000 * timestamp) / ((long) videoTimeStampScale), yaw, pitch, roll);
            } else {
                for (int k = 0; k < 5; k++) {
                    int check = inputStream.read();
                    if (check == -1) {
                        isEndOfStream = true;
                        frameIndicator = -100;
                    }
                    frameHeader[k] = (byte) check;
                }
                frameType = frameHeader[4];
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private synchronized int readStream() {
        String frameTag;
        while (true) {
            try {
                byte[] temp = new byte[4];
                int btemp = inputStream.read();
                if (btemp < 0) {
                    isEndOfStream = true;
                    frameIndicator = -100;
                }
                temp[0] = (byte) btemp;
                btemp = inputStream.read();
                if (btemp < 0) {
                    isEndOfStream = true;
                    frameIndicator = -100;
                }
                temp[1] = (byte) btemp;
                btemp = inputStream.read();
                if (btemp < 0) {
                    isEndOfStream = true;
                    frameIndicator = -100;
                }
                temp[2] = (byte) btemp;
                btemp = inputStream.read();
                if (btemp < 0) {
                    isEndOfStream = true;
                    frameIndicator = -100;
                }
                temp[3] = (byte) btemp;
                frameTag = new String(temp);
                if (!frameTag.contains(TTT_FILE_TAG)) {
                    break;
                }
                long skip = 0;
                try {
                    skip = inputStream.skip((long) (headerSize - 4));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } catch (Throwable e2) {
                isEndOfStream = true;
            }
        }

        if (frameTag.contains(VIDEO_FRAME_START_TAG)) {
            frameIndicator = 100;
        } else if (frameTag.contains(AUDIO_FRAME_START_TAG)) {
            frameIndicator = 200;
        } else if (frameTag.contains(VROT_FRAME_START_TAG)) {
            frameIndicator = 300;
        } else {
            isEndOfStream = true;
            frameIndicator = -100;
        }

        shouldAdvanced = false;
        return frameIndicator;
    }

    private synchronized int readFrame(ByteBuffer input) throws IOException {
        int readCount;

        if (isEndOfStream) {
            readCount = -100;

        } else {
            input.put(frameHeader);

            int count = 5;
            int size = frameSize - 5;

            while (size > 0) {

                if (isEndOfStream) {
                    count = -100;
                    break;
                }

                if (size < BUFFER_SIZE) {
                    readCount = inputStream.read(buffer, 0, size);
                } else {
                    readCount = inputStream.read(buffer);
                }

                if (readCount <= 0) {
                    isEndOfStream = true;
                    break;
                }

                input.put(buffer, 0, readCount);
                count += readCount;
                size -= readCount;
            }

            videoFrameCount++;
            readCount = count;
        }
        return readCount;
    }

    private long parseLongFromByte(byte[] arr) {
        return ByteBuffer.wrap(arr).order(ByteOrder.BIG_ENDIAN).getLong();
    }
}
