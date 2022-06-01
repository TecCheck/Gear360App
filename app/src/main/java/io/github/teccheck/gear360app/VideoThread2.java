package io.github.teccheck.gear360app;

import android.media.MediaFormat;
import android.media.MediaExtractor;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class VideoThread2 extends Thread{

    private static final int BUFFER_SIZE = 786432;

    LiveTestActivity.CustomMediaCodec videoDecoder = null;
    MediaExtractor mediaExtractor;
    LiveTestActivity.ConvertingCallback convertingCallback = null;
    LiveTestActivity.VideoTimeStampUpdater videoTimeStampUpdater = null;
    ByteBuffer audioBuffer = null;
    private HashMap<Long, LiveTestActivity.VrotData> vrotList;
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

    public void init(Surface surface, String filePath, LiveTestActivity.ConvertingCallback convertingCallback) {
        this.surface = surface;
        this.filepath = filePath;
        this.hasReleased = false;
        this.convertingCallback = convertingCallback;
        this.videoPresentationTime = 0;
        this.vrotList = new HashMap();
    }

    public void run(){
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(filepath);
            int tracks = extractor.getTrackCount();

            MediaFormat videoFormat = extractor.getTrackFormat(100);
            ByteBuffer inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}