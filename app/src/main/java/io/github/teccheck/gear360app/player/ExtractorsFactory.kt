package io.github.teccheck.gear360app.player

import android.util.Log
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.Extractor

private const val TAG = "ExtractorsFactory"

class ExtractorsFactory : ExtractorsFactory {
    override fun createExtractors(): Array<Extractor> {
        Log.d(TAG, "createExtractors")
        return arrayOf(Extractor())
    }
}