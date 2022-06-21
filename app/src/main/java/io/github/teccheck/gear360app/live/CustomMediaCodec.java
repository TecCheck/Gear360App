package io.github.teccheck.gear360app.live;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

// This is more or less just a wrapper for MediaCodec with exception catching
public class CustomMediaCodec {

    private MediaCodec mediaCodec;
    private boolean isStop;

    public CustomMediaCodec(String videoMIME) throws IOException {
        mediaCodec = MediaCodec.createDecoderByType(videoMIME);
        isStop = false;
    }

    public void configure(MediaFormat format, Surface surface, MediaCrypto crypto, int flags) {
        try {
            if (!this.isStop) {
                this.mediaCodec.configure(format, surface, crypto, flags);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ByteBuffer[] getInputBuffers() {
        return mediaCodec.getInputBuffers();
    }

    public final void start() {
        try {
            this.mediaCodec.start();
            this.isStop = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void stop() {
        if (!this.isStop) {
            try {
                this.setIsStop(true);
                this.mediaCodec.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public final void release() {
        try {
            this.mediaCodec.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void queueInputBuffer(int index, int offset, int size, long presentationTimeUs, int flags) throws MediaCodec.CryptoException {
        if (!this.isStop) {
            try {
                this.mediaCodec.queueInputBuffer(index, offset, size, presentationTimeUs, flags);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public final int dequeueInputBuffer(long timeoutUs) {
        return mediaCodec.dequeueInputBuffer(timeoutUs);
    }

    public int dequeueOutputBuffer(MediaCodec.BufferInfo info, int timeoutUs) {
        return mediaCodec.dequeueOutputBuffer(info, timeoutUs);
    }

    public void releaseOutputBuffer(int outIndex, boolean render) {
        mediaCodec.releaseOutputBuffer(outIndex, render);
    }

    public void setIsStop(boolean stop) {
        this.isStop = stop;
    }
}