package de.teccheck.gear360app.player;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;

public class MyMediaExtractorFactory implements ExtractorsFactory {
    @Override
    public Extractor[] createExtractors() {
        return new Extractor[0];
    }
}
