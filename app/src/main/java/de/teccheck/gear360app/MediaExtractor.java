package de.teccheck.gear360app;

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

import static de.teccheck.gear360app.LiveTestActivity.*;

public class MediaExtractor{

    private static final int BUFFER_SIZE = 786432;
    private static final String TTT_FILE_TAG = "TTTS";
    private static final String VIDEO_CODEC_UNKNOWN = "video/unknown";
    private static final String VIDEO_FRAME_STRAT_TAG = "00VD";
    private static final String AUDIO_CODEC_UNKNOWN = "audio/unknown";
    private static final String AUDIO_FRAME_START_TAG = "00AU";
    private static final String VROT_FRAME_START_TAG = "00VR";

    Socket socket;
    BufferedOutputStream outputStream;
    BufferedInputStream inputStream;
    private MediaFormat videoFormat = new MediaFormat();
    private MediaFormat audioFormat = new MediaFormat();
    private boolean isEndofStream;
    private boolean shouldAdvanced = false;
    private boolean isFirst = true;
    private int frameIndicator;
    private int headerSize = 0;
    private int videoBitRate = 1;
    private int videoGOP = 1;
    private int videoTimeStampScale = 1;
    private int audioTimeStampScale;
    private int videoMaxBufferSize = -1;
    private int frameSize = 0;
    private int frameType = 2;
    private int videoFrameCnt = 0;
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
        this.isEndofStream = true;
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (Throwable e) {
                //
            }
        }
        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (Throwable e2) {
                //
            }
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (Throwable e22) {
                //
            }
        }
        this.buffer = null;
        this.headers = null;
        this.frameHeader = null;
        System.gc();
    }

    public void advance() {
        shouldAdvanced = true;
    }

    public boolean isKeyFrame() {
        if (this.frameType == 64 || this.frameType == 38) {
            return true;
        }
        return false;
    }

    public boolean setDataSource(String stringurl) {
        try {
            URL url = new URL(stringurl);

            String host = url.getHost();
            String file = url.getFile();
            int port = url.getPort();

            if(socket == null){
                socket = new Socket(Proxy.NO_PROXY);
                socket.setReceiveBufferSize(BUFFER_SIZE);
            }

            String s = "GET " + file + " HTTP/1.1\r\nUser-Agent: Android Linux\r\nHost: "+ host + ":" + port + "\r\nConnection: Keep-Alive\r\n\r\n";

            Log.i(MainActivity.TAG, "\r\n" + s);
            socket.setSoTimeout(0);
            socket.connect(new InetSocketAddress(host, port));
            outputStream = new BufferedOutputStream(socket.getOutputStream());
            outputStream.write(s.getBytes());
            outputStream.flush();

            if(inputStream != null){
                inputStream.close();
                inputStream = null;
            }

            inputStream = new BufferedInputStream(socket.getInputStream(), BUFFER_SIZE);

            String h = readHeader();
            Log.i(MainActivity.TAG, "readHeader(): " + h);

            if (readMetaData() < 0) {
                return false;
            }

            shouldAdvanced = true;
            isFirst = true;
            startMs = 0;
            currentMs = 0;
            presentationMs = 0;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            isEndofStream = true;
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        // // // // // // //
        return false;
        // // // // // // //
    }

    public int dropFrame() throws IOException {
        int size = this.frameSize - 5;
        int cnt = 0;
        while (size > 0) {
            if (this.isEndofStream) {
                return cnt;
            }
            long c;
            if (size < BUFFER_SIZE) {
                c = (long) this.inputStream.read(this.buffer, 0, size);
            } else {
                c = (long) this.inputStream.read(this.buffer);
            }
            if (c < 0) {
                this.isEndofStream = true;
                return -1;
            }
            size = (int) (((long) size) - c);
            cnt = (int) (((long) cnt) + c);
        }
        return cnt;
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
        return this.frameIndicator;
    }

    public long getSampleTime() {
        if (this.frameIndicator == 100) {
            return (this.frameTimeStamp * 1000000) / ((long) this.videoTimeStampScale);
        }
        return (this.frameTimeStamp * 1000000) / ((long) this.audioTimeStampScale);
    }

    public MediaFormat getVideoFormat() {
        return videoFormat;
    }

    public MediaFormat getAudioFormat() {
        return audioFormat;
    }

    public VrotData getVrotData() {
        return this.vrotData;
    }

    private String readHeader() {
        String hd = "";
        String temp = "";
        do {
            try {
                char c = (char) this.inputStream.read();
                hd = hd + c;
                if (c == '\r' || c == '\n') {
                    temp = temp + c;
                } else {
                    temp = "";
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } while (!temp.contentEquals("\r\n\r\n"));
        return hd;
    }

    private String readByteToString() {
        int loop = 0;
        try {
            byte[] temp = new byte[4];
            while (loop < 4) {
                int btemp = inputStream.read();
                if (btemp < 0) {
                    isEndofStream = true;
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
            isEndofStream = true;
        }
        return null;
    }

    private int readMetaData() /*throws IOException*/ {
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
            Log.d(MainActivity.TAG, "Header: " + header);
            if(header == null){
                headerSize = -1;
            }else {
                int size;
                String read;
                int version;
                String caseName;

                switch (header){
                    case TTT_FILE_TAG:
                        Log.d(MainActivity.TAG, "case TTT");
                        size = readByteToInt();
                        type = readByteToInt();
                        headerSize += 12;

                    case "VID0":
                        Log.d(MainActivity.TAG, "case VID0");
                        caseName = readByteToString();
                        size = readByteToInt();
                        int transblocknumber = readByteToInt();
                        int transblocktype = readByteToInt();
                        int encodeWidth = readByteToInt();
                        int encodeHeight = readByteToInt();
                        videoCodecType = readByteToInt();
                        this.videoBitRate = readByteToInt();
                        this.videoGOP = readByteToInt();
                        int goptype = readByteToInt();
                        this.videoTimeStampScale = readByteToInt();
                        read = readByteToString();
                        Log.d(MainActivity.TAG, "Read: " + read);
                        if (read == null || !read.contains("VD00")) {
                            this.headerSize = -1;
                            stopRead = true;
                        } else {
                            version = readByteToInt();
                            this.fps = ((float) readByteToInt()) / ((float) readByteToInt());
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
                        Log.d(MainActivity.TAG, "case AUD0");
                        caseName = readByteToString();
                        size = readByteToInt();
                        channel = readByteToInt();
                        int nAudioInfo = readByteToInt();
                        this.audioTimeStampScale = readByteToInt();
                        read = readByteToString();
                        Log.d(MainActivity.TAG, "Read: " + read);

                        Log.d(MainActivity.TAG, "case AUD0 if statement incomming");
                        if (read == null || !read.contains("AU00")) {
                            Log.d(MainActivity.TAG, "into the if");
                            this.headerSize = -1;
                            stopRead = true;
                            //continue;
                        } else {

                            Log.i(MainActivity.TAG, "into the else");
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
                            this.headerSize += size + 4;
                            //continue;
                        }
                    case "VRO0":
                        Log.d(MainActivity.TAG, "case VRO0");
                        //caseName = readByteToString();
                        size = readByteToInt();
                        int nVRChannel = readByteToInt();
                        read = readByteToString();
                        Log.d(MainActivity.TAG, "Read: " + read);

                        if (read == null || !read.contains("VR00")) {
                            this.headerSize = -1;
                            stopRead = true;
                            //continue;
                        } else {
                            version = readByteToInt();
                            readByteToInt();
                            this.headerSize += size + 4;
                            //continue;
                        }
                    case "LIST":
                        Log.d(MainActivity.TAG, "case LIST");
                        this.headerSize += 8;
                    case "movi":
                        Log.d(MainActivity.TAG, "case movi");
                        this.headerSize += 4;
                        stopRead = true;
                        break;
                    default:
                        Log.d(MainActivity.TAG, "default");
                        this.headerSize = -1;
                        stopRead = true;
                }

                Log.d(MainActivity.TAG, "Widht: " + width + ", Height: " + height + ", VideoCodecType: " + videoCodecType + ", AudioCodecType: " + audioCodecType);
                Log.d(MainActivity.TAG, "Channel: " + channel + ", Sample Rate: " + sampleRate + ", Type: " + type + ", HeaderSize: " + headerSize);

                if ((type & 1) != 0) {
                    this.videoFormat.setInteger("width", width);
                    this.videoFormat.setInteger("height", height);
                    Log.d(MainActivity.TAG, "videoCodecType: " + videoCodecType);
                    switch (videoCodecType) {
                        case 0:
                            this.videoFormat.setString("mime", "video/raw");
                            break;
                        case 1:
                            this.videoFormat.setString("mime", "video/hevc");
                            break;
                        case 2:
                            break;
                        default:
                            this.videoFormat.setString("mime", VIDEO_CODEC_UNKNOWN);
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
                    audioFormat.setInteger("channel-count", channel);
                    Log.d(MainActivity.TAG, "audioCodecType: " + audioCodecType);
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
                return this.headerSize;
            }

        }while (!stopRead);

        if ((type & 1) == 0) {
            this.videoFormat = null;
        } else {
            this.videoFormat.setInteger("width", width);
            this.videoFormat.setInteger("height", height);
            Log.d(MainActivity.TAG, "videoCodecType: " + videoCodecType);
            switch (videoCodecType) {
                case 0:
                    this.videoFormat.setString("mime", "video/raw");
                    break;
                case 1:
                    this.videoFormat.setString("mime", "video/hevc");
                    break;
                case 2:
                    break;
                default:
                    this.videoFormat.setString("mime", VIDEO_CODEC_UNKNOWN);
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
            this.audioFormat = null;
        } else {
            audioFormat.setInteger("channel-count", channel);
            Log.d(MainActivity.TAG, "audioCodecType: " + audioCodecType);
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
        Log.d(MainActivity.TAG, "videoFormat: " + videoFormat);
        Log.d(MainActivity.TAG, "audioFormat: " + audioFormat);
        return this.headerSize;
    }

    private int readByteToInt() {
        int data = 0;
        int loop = 0;
        while (loop < 4) {
            try {
                int btemp = inputStream.read();
                if (btemp < 0) {
                    isEndofStream = true;
                    frameIndicator = -100;
                    break;
                }
                data = (int) (((long) (data << 8)) + ((long) btemp));
                loop++;
            } catch (Exception e) {
                e.printStackTrace();
                this.isEndofStream = true;
            }
        }
        if (loop == 4) {
            return data;
        }
        return -1;
    }

    private synchronized void readFrameHead() {
        try {
            this.frameSize = readByteToInt();
            long timestamp = parselongfrombyte(new byte[]{(byte) this.inputStream.read(), (byte) this.inputStream.read(), (byte) this.inputStream.read(), (byte) this.inputStream.read(), (byte) this.inputStream.read(), (byte) this.inputStream.read(), (byte) this.inputStream.read(), (byte) this.inputStream.read()});
            this.frameTimeStamp = timestamp;
            this.presentationMs = (1000 * timestamp) / ((long) this.videoTimeStampScale);
            if (this.presentationMs == 0) {
                this.isFirst = true;
            }
            if (this.frameIndicator == 300) {
                this.yaw = ((float) readByteToInt()) / ((float) readByteToInt());
                this.pitch = ((float) readByteToInt()) / ((float) readByteToInt());
                this.roll = ((float) readByteToInt()) / ((float) readByteToInt());
                this.vrotData = new VrotData((1000000 * timestamp) / ((long) this.videoTimeStampScale), this.yaw, this.pitch, this.roll);
            } else {
                for (int k = 0; k < 5; k++) {
                    int check = this.inputStream.read();
                    if (check == -1) {
                        this.isEndofStream = true;
                        this.frameIndicator = -100;
                    }
                    this.frameHeader[k] = (byte) check;
                }
                this.frameType = this.frameHeader[4];
            }
        } catch (Throwable e) {
        }
    }

    private synchronized int readStream() {
        String frameTag;
        while (true) {
            try {
                byte[] temp = new byte[4];
                int btemp = this.inputStream.read();
                if (btemp < 0) {
                    this.isEndofStream = true;
                    this.frameIndicator = -100;
                }
                temp[0] = (byte) btemp;
                btemp = this.inputStream.read();
                if (btemp < 0) {
                    this.isEndofStream = true;
                    this.frameIndicator = -100;
                }
                temp[1] = (byte) btemp;
                btemp = this.inputStream.read();
                if (btemp < 0) {
                    this.isEndofStream = true;
                    this.frameIndicator = -100;
                }
                temp[2] = (byte) btemp;
                btemp = this.inputStream.read();
                if (btemp < 0) {
                    this.isEndofStream = true;
                    this.frameIndicator = -100;
                }
                temp[3] = (byte) btemp;
                frameTag = new String(temp);
                if (!frameTag.contains(TTT_FILE_TAG)) {
                    break;
                }
                long skip = 0;
                try {
                    skip = this.inputStream.skip((long) (this.headerSize - 4));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } catch (Throwable e2) {
                this.isEndofStream = true;
            }
        }
        if (frameTag.contains(VIDEO_FRAME_STRAT_TAG)) {
            this.frameIndicator = 100;
        } else if (frameTag.contains(AUDIO_FRAME_START_TAG)) {
            this.frameIndicator = 200;
        } else if (frameTag.contains(VROT_FRAME_START_TAG)) {
            this.frameIndicator = 300;
        } else {
            this.isEndofStream = true;
            this.frameIndicator = -100;
        }
        this.shouldAdvanced = false;
        return this.frameIndicator;
    }

    private synchronized int readFrame(ByteBuffer input) throws IOException {
        int i;
        if (this.isEndofStream) {
            i = -100;
        } else {
            input.put(this.frameHeader);
            int cnt = 5;
            int size = this.frameSize - 5;
            while (size > 0) {
                if (this.isEndofStream) {
                    cnt = -100;
                    break;
                }
                if (size < BUFFER_SIZE) {
                    i = this.inputStream.read(this.buffer, 0, size);
                } else {
                    i = this.inputStream.read(this.buffer);
                }
                if (i <= 0) {
                    this.isEndofStream = true;
                    break;
                }
                input.put(this.buffer, 0, i);
                cnt += i;
                size -= i;
            }
            this.videoFrameCnt++;
            i = cnt;
        }
        return i;
    }

    private long parselongfrombyte(byte[] arr) {
        return ByteBuffer.wrap(arr).order(ByteOrder.BIG_ENDIAN).getLong();
    }
}
