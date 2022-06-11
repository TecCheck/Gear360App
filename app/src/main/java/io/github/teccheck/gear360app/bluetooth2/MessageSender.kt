package io.github.teccheck.gear360app.bluetooth2

import android.util.Log
import io.github.teccheck.gear360app.bluetooth.BTMessages
import io.github.teccheck.gear360app.bluetooth.BTMessages.*

private const val TAG = "MessageSender"

class MessageSender(val sender: Sender) {

    fun sendPhoneInfo() {
        Log.d(TAG, "sendPhoneInfo")
        val versionName = "1.2.00.8"
        val message = BTMessages.BTInfoMsg(
            BTMessages.IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE,
            "100",
            BTMessages.IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE,
            "100",
            versionName,
            false
        )
        sendCommand(message)
    }

    fun sendWidgetInfoRequest() {
        sendCommand(BTWidgetInfoMsg())
    }

    fun sendDateTimeRequest() {
        sendCommand(BTDateTimeMsg(IDS.DATE_TIME_REQUEST_TITLE))
    }

    fun sendChangeModeRequest() {

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

    // unused
    fun sendCameraConfigInfoRequest() {
        sendCommand(BTConfigInfoMsg("success", "100"))
    }

    // not working
    fun sendFirmwareInstallRequest() {
        sendCommand(BTFWInstallMsg())
    }

    fun sendLiveViewRequest() {
        sendCommand(BTCommandMsg("cmd-req", "execute", IDS.COMMAND_REQUEST_DESCRIPTION_LIVEVIEW))
    }

    private fun sendCommand(btMessage: BTMessages.BTMessage) {
        sender.send(204, btMessage.toJSON().toString().encodeToByteArray())
    }

    interface Sender {
        fun send(channelId: Int, data: ByteArray);
    }

}