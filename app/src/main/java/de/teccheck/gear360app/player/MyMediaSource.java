package de.teccheck.gear360app.player;

import android.os.Handler;
import androidx.annotation.Nullable;

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
    public void prepareSource(SourceInfoRefreshListener listener, @Nullable TransferListener mediaTransferListener) {

    }

    @Override
    public void maybeThrowSourceInfoRefreshError() throws IOException {

    }

    @Override
    public MediaPeriod createPeriod(MediaPeriodId id, Allocator allocator, long startPositionUs) {
        return null;
    }

    @Override
    public void releasePeriod(MediaPeriod mediaPeriod) {

    }

    @Override
    public void releaseSource(SourceInfoRefreshListener listener) {

    }
}
