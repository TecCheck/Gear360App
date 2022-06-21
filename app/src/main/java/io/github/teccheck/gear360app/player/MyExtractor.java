package io.github.teccheck.gear360app.player;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;

import java.io.IOException;

public class MyExtractor implements Extractor {
    @Override
    public boolean sniff(ExtractorInput input) throws IOException {
        return false;
    }

    @Override
    public void init(ExtractorOutput output) {

    }

    @Override
    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException {
        return Extractor.RESULT_CONTINUE;
    }

    @Override
    public void seek(long position, long timeUs) {

    }

    @Override
    public void release() {

    }
}
