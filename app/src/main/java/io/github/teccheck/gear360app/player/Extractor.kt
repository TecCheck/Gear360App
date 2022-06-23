package io.github.teccheck.gear360app.player

import android.util.Log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.extractor.*
import com.google.android.exoplayer2.extractor.Extractor
import com.google.android.exoplayer2.util.MimeTypes
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

private const val TAG = "Extractor"

private const val TAG_TTTS = "TTTS"
private const val TAG_VID_0 = "VID0"
private const val TAG_VD_00 = "VD00"

private const val TAG_AUD_0 = "AUD0"
private const val TAG_AU_00 = "AU00"

private const val TAG_VR_00 = "VR00"

private const val TAG_LIST = "LIST"

private const val TAG_MOVI = "movi"

private const val TAG_VIDEO_FRAME_START = "00VD"
private const val TAG_AUDIO_FRAME_START = "00AU"
private const val TAG_VROT_FRAME_START = "00VR"

private const val TRACK_ID_VIDEO = 0

class Extractor : Extractor {

    private var extractorOutput: ExtractorOutput? = null

    private var currentState = ExtractorState.READ_MAIN_HEADER
    private var currentChunkType = ChunkType.UNKNOWN
    private var bytesRemainingInCurrentChunk = 0

    private val headerSize = 0
    private var videoBitRate = 1
    private var videoGOP = 1
    private var videoTimeStampScale = 1
    private var audioTimeStampScale = 0
    private var frameSize = 0
    private var frameType = 2

    private var frameTimeStamp: Long = 0

    private var fps = 0.0f

    private val frameHeader = ByteArray(5)

    override fun init(output: ExtractorOutput) {
        extractorOutput = output
    }

    @Throws(IOException::class)
    override fun sniff(input: ExtractorInput): Boolean {
        Log.d(TAG, "Sniff ${input.position} - ${input.length}")
        return false
    }

    @Throws(IOException::class)
    override fun read(input: ExtractorInput, seekPosition: PositionHolder): Int {
        when (currentState) {
            ExtractorState.READ_MAIN_HEADER -> {
                readMainHeader(input)

                // This is important. Don't leave this call out
                extractorOutput?.seekMap(SeekMap.Unseekable(C.TIME_UNSET))

                extractorOutput?.endTracks()
                currentState = ExtractorState.READ_FRAME_TAG
                return Extractor.RESULT_CONTINUE
            }

            ExtractorState.READ_FRAME_TAG -> {
                readFrameTag(input)
                currentState = ExtractorState.READ_FRAME_HEADER
                return Extractor.RESULT_CONTINUE
            }

            ExtractorState.READ_FRAME_HEADER -> {
                readFrameHead(input)
                currentState = ExtractorState.READ_FRAME_DATA
                return Extractor.RESULT_CONTINUE
            }
            ExtractorState.READ_FRAME_DATA -> {
                val done = readFrameData(input)
                if (done) currentState = ExtractorState.READ_FRAME_TAG
            }
        }

        return Extractor.RESULT_CONTINUE
    }

    override fun seek(position: Long, timeUs: Long) {
        Log.d(TAG, "Seek $position - $timeUs")
    }

    override fun release() {
        Log.d(TAG, "Release")
    }

    private fun readMainHeader(input: ExtractorInput) {

        var width = 0
        var height = 0

        var videoCodecType = 0
        var audioCodecType = -1
        var channel = 0
        var sampleRate = 0

        var type = 0
        var headerSize = 0

        var doRead = true

        do {
            val tag = readString(input, 4)
            Log.d(TAG, "Main header part $tag")

            var size: Int
            var subTag: String

            when (tag) {
                TAG_TTTS -> {
                    size = readInt(input)
                    type = readInt(input)
                    headerSize += 12
                }

                TAG_VID_0 -> {
                    size = readInt(input)

                    input.skip(16)

                    videoCodecType = readInt(input)
                    videoBitRate = readInt(input)
                    videoGOP = readInt(input)

                    input.skip(4)

                    videoTimeStampScale = readInt(input)

                    subTag = readString(input, 4)
                    Log.d(TAG, "Video sub tag: $subTag")

                    if (!subTag.contains(TAG_VD_00)) {
                        headerSize = -1
                        doRead = false
                    } else {
                        input.skip(4)
                        fps = readInt(input).toFloat() / readInt(input).toFloat()

                        input.skip(24)
                        width = readInt(input)
                        height = readInt(input)
                        input.skip(8)

                        headerSize += size + 4
                    }
                }

                TAG_AUD_0 -> {
                    size = readInt(input)
                    channel = readInt(input)

                    val nAudioInfo = readInt(input)
                    audioTimeStampScale = readInt(input)

                    subTag = readString(input, 4)
                    Log.d(TAG, "Audio sub tag: $subTag")

                    if (!subTag.contains(TAG_AU_00)) {
                        headerSize = -1
                        doRead = false
                    } else {
                        input.skip(4)
                        audioCodecType = readInt(input)
                        input.skip(32)

                        sampleRate = readInt(input)
                        input.skip(20)

                        headerSize += size + 4
                    }
                }

                TAG_VR_00 -> {
                    size = readInt(input)
                    input.skip(4)

                    subTag = readString(input, 4)
                    Log.d(TAG, "SubTag: $subTag")

                    if (!subTag.contains(TAG_VR_00)) {
                        headerSize = -1
                        doRead = false
                    } else {
                        input.skip(8)
                        headerSize += size + 4
                    }
                }

                TAG_LIST -> {
                    input.skip(4)
                    headerSize += 8
                }

                TAG_MOVI -> {
                    headerSize += 4
                    doRead = false
                }
            }
        } while (doRead)

        if (type and 1 != 0) {
            val mimeType = when (videoCodecType) {
                0 -> "video/raw"
                1 -> MimeTypes.VIDEO_H265
                else -> MimeTypes.VIDEO_UNKNOWN
            }

            Log.d(TAG, "Video mime type: $mimeType")
            Log.d(TAG, "FPS: $fps")

            val format = Format.Builder()
                .setWidth(width)
                .setHeight(height)
                .setSampleMimeType(mimeType)
                .setId(TRACK_ID_VIDEO)
                .setFrameRate(fps)
                .setMaxInputSize(1000000)

            val track = extractorOutput?.track(TRACK_ID_VIDEO, C.TRACK_TYPE_VIDEO)
            track?.format(format.build())
        }

        /*
        if (type and 2 != 0) {
            audioFormat = MediaFormat()

            when (audioCodecType) {
                0 -> audioFormat?.setString("mime", "audio/raw")
                1 -> audioFormat?.setString("mime", "audio/mp4a-latm")
                else -> audioFormat?.setString("mime", AUDIO_CODEC_UNKNOWN)
            }

            audioFormat?.setInteger("channel-count", channel)
            audioFormat?.setInteger("sample-rate", sampleRate)
            audioFormat?.setLong("durationUs", 0)
        }
         */
    }

    private fun readFrameTag(input: ExtractorInput) {
        var frameTag: String

        while (true) {
            frameTag = readString(input, 4)

            if (!frameTag.contains(TAG_TTTS)) {
                currentChunkType = when (frameTag) {
                    TAG_VIDEO_FRAME_START -> ChunkType.VIDEO
                    TAG_AUDIO_FRAME_START -> ChunkType.AUDIO
                    TAG_VROT_FRAME_START -> ChunkType.VROT
                    else -> ChunkType.UNKNOWN
                }
                return
            }

            input.skip(headerSize - 4)
        }
    }

    private fun readFrameHead(input: ExtractorInput) {
        frameSize = readInt(input)
        frameTimeStamp = readLong(input)
        bytesRemainingInCurrentChunk = frameSize

        if (currentChunkType == ChunkType.VROT) {
            Log.w(TAG, "ChunkType is VROT")
            input.skip(24)
        } else {
            input.peekFully(frameHeader, 0, frameHeader.size)
            frameType = frameHeader[4].toInt()
            input.resetPeekPosition()
        }
    }

    private fun readFrameData(input: ExtractorInput): Boolean {
        when (currentChunkType) {
            ChunkType.VIDEO -> return readFrameSampleData(input)
            ChunkType.AUDIO -> {}
            ChunkType.VROT -> {}
            ChunkType.UNKNOWN -> Log.w(TAG, "Invalid ChunkType")
        }

        return true
    }

    private fun readFrameSampleData(input: ExtractorInput): Boolean {
        val track = extractorOutput?.track(TRACK_ID_VIDEO, C.TRACK_TYPE_VIDEO)

        val readCount = track?.sampleData(input, bytesRemainingInCurrentChunk, false)
        if (readCount != null) bytesRemainingInCurrentChunk -= readCount

        val done = bytesRemainingInCurrentChunk == 0
        if (done) {
            val sampleTime = getSampleTime()

            val flags = if (isKeyFrame()) C.BUFFER_FLAG_KEY_FRAME else 0
            track?.sampleMetadata(sampleTime, flags, frameSize - 5, 0, null)
        }

        return done
    }

    private fun getSampleTime(): Long {
        return if (currentChunkType == ChunkType.VIDEO) {
            frameTimeStamp * 1000000 / videoTimeStampScale.toLong()
        } else frameTimeStamp * 1000000 / audioTimeStampScale.toLong()
    }

    private fun isKeyFrame(): Boolean {
        return frameType == 64 || frameType == 38
    }

    private fun readString(input: ExtractorInput, len: Int): String {
        val buffer = ByteArray(len)
        input.readFully(buffer, 0, len)
        return String(buffer)
    }

    private fun readInt(input: ExtractorInput): Int {
        val buffer = ByteArray(4)
        input.readFully(buffer, 0, buffer.size)

        return ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).int
    }

    private fun readLong(input: ExtractorInput): Long {
        val buffer = ByteArray(8)
        input.readFully(buffer, 0, buffer.size)

        return ByteBuffer.wrap(buffer).order(ByteOrder.BIG_ENDIAN).long
    }
}