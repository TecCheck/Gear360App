package io.github.teccheck.gear360app.bluetooth2

import io.github.teccheck.gear360app.Utils
import io.github.teccheck.gear360app.bluetooth.BTMessages
import io.github.teccheck.gear360app.bluetooth.BTMessages.*

private const val TAG = "MessageSender"

class MessageSender(private val sender: Sender) {

    fun sendPhoneInfo() {
        val wifiMac = Utils.getWifiMacAddress()
        val versionName = "1.2.00.8"
        sendCommand2(BTInfoMsg(false, wifiMac, "test", versionName, false))
    }

    fun sendWidgetInfoRequest() {
        sendCommand2(BTWidgetReq())
    }

    fun sendDateTimeRequest() {
        sendCommand(BTDateTimeMsg(IDS.DATE_TIME_REQUEST_TITLE))
    }

    fun sendChangeMode(mode: CameraMode) {
        sendConfigChangeCommand(ConfigConstants.MODE, mode.value)
    }

    fun sendChangeLoopingVideoTime(timer: LoopingVideoTime) {
        sendConfigChangeCommand(ConfigConstants.LOOPING_VIDEO_TIME, timer.value)
    }

    fun sendSetLedIndicators(active: Boolean) {
        val value = if (active) ConfigConstants.LED_ON else ConfigConstants.LED_OFF
        sendConfigChangeCommand(ConfigConstants.LED_INDICATOR, value)
    }

    fun sendChangeTimerTimer(time: TimerTime) {
        sendConfigChangeCommand(ConfigConstants.TIMER, time.value)
    }

    fun sendChangeBeepVolume(volume: BeepVolume) {
        sendConfigChangeCommand(ConfigConstants.BEEP, volume.value)
    }

    fun sendChangePowerOffTime(time: AutoPowerOffTime) {
        sendConfigChangeCommand(ConfigConstants.AUTO_POWER_OFF, time.value)
    }

    private fun sendConfigChangeCommand(configName: String, configValue: String) {
        val action = BTCommandReq.ConfigAction(configName, configValue)
        val message = BTCommandReq(action)
        sendCommand2(message)
    }

    fun sendShotRequest(isPhotoMode: Boolean, isRecording: Boolean) {
        if (isPhotoMode) {
            sendCaptureRequest()
        } else if (!isPhotoMode) {
            if (isRecording)
                sendRecordStopRequest()
            else
                sendRecordStartRequest()
        }
    }

    // makes a photo if camera is in photo mode (does not work in video mode)
    private fun sendCaptureRequest() {
        sendCommand(BTShotMsg(IDS.REMOTE_SHOT_REQUEST_MSGID, "capture"))
    }

    // starts recording if camera is in video mode (does not work in photo mode)
    private fun sendRecordStartRequest() {
        sendCommand(BTShotMsg(IDS.REMOTE_SHOT_REQUEST_MSGID, "record"))
    }

    // stops the recording if camera is recording (does not work in photo mode)
    private fun sendRecordStopRequest() {
        sendCommand(BTShotMsg(IDS.REMOTE_SHOT_REQUEST_MSGID, "record stop"))
    }

    fun sendLiveViewRequest() {
        val action = BTCommandReq.Action("execute", "liveview")
        val message = BTCommandReq(action)
        sendCommand2(message)
    }

    private fun sendCommand(btMessage: BTMessages.BTMessage) {
        sender.send(204, btMessage.toJSON().toString().encodeToByteArray())
    }

    private fun sendCommand2(message: BTMessage) {
        sender.send(204, message.toJson().toString().encodeToByteArray())
    }

    interface Sender {
        fun send(channelId: Int, data: ByteArray)
    }

}