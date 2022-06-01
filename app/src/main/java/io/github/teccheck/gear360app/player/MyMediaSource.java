package io.github.teccheck.gear360app.player;

import android.os.Handler;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.PlayerId;
import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;

public class MyMediaSource implements MediaSource {

    @Override
    public void addEventListener(Handler handler, MediaSourceEventListener eventListener) {

    }

    @Override
    public void removeEventListener(MediaSourceEventListener eventListener) {

    }

    @Override
    public void addDrmEventListener(Handler handler, DrmSessionEventListener eventListener) {

    }

    @Override
    public void removeDrmEventListener(DrmSessionEventListener eventListener) {

    }

    @Override
    public MediaItem getMediaItem() {
        return null;
    }

    @Override
    public void prepareSource(MediaSourceCaller caller, @Nullable TransferListener mediaTransferListener, PlayerId playerId) {

    }

    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {

    }

    @Override
    public void enable(MediaSourceCaller caller) {

    }

    @Override
    public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator, long startPositionUs) {
        return null;
    }

    @Override
    public void releasePeriod(MediaPeriod mediaPeriod) {

    }

    @Override
    public void disable(MediaSourceCaller caller) {

    }

    @Override
    public void releaseSource(MediaSourceCaller caller) {

    }
}
