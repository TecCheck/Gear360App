package io.github.teccheck.gear360app.player

enum class ExtractorState {
    READ_MAIN_HEADER,
    READ_FRAME_TAG,
    READ_FRAME_HEADER,
    READ_FRAME_DATA
}