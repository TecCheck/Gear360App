package io.github.teccheck.gear360app.bluetooth

data class Gear360Status(
    val battery: Int,
    val batteryState: String,
    val totalMemory: Int,
    val usedMemory: Int,
    val freeMemory: Int,
    val recordState: String,
    val captureState: String,
    val autoPowerOff: String,
    val recordableTime: Int,
    val capturableCount: Int
) {
    fun isRecording(): Boolean {
        // FIXME
        return false
    }
}