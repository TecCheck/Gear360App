package io.github.teccheck.gear360app.bluetooth;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.teccheck.gear360app.Gear360Settings;
import io.github.teccheck.gear360app.Utils;

import static io.github.teccheck.gear360app.bluetooth.BTMessages.*;

public class BTMessageSender {

    public static final String TAG = "G360_" + BTMessageSender.class.getSimpleName();

    public static void sendMessage(BTMessage message, BTService service, int channel) {
        try {
            Log.d(TAG, "Sending: \n" + Utils.getPrettyJsonStringFromByteData(message.toJSON().toString()));
            service.send(channel, message.toJSON().toString());
            BTMessageLogger.logOutgoingMessage((JSONObject) message.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendShotRequest(BTService service) {
        if (Gear360Settings.isPhotoMode()) {
            sendCaptureRequest(service);
        } else if (Gear360Settings.isVideoMode()) {
            if (Gear360Settings.isRecording)
                sendRecordStopRequest(service);
            else
                sendRecordStartRequest(service);
        }
    }

    //makes a photo if camera is in photo mode (does not work in video mode)
    public static void sendCaptureRequest(BTService service) {
        BTShotMsg message = new BTShotMsg(IDS.REMOTE_SHOT_REQUEST_MSGID, "capture");
        sendMessage(message, service, 204);
    }

    //starts recording if camera is in video mode (does not work in photo mode)
    public static void sendRecordStartRequest(BTService service) {
        BTShotMsg message = new BTShotMsg(IDS.REMOTE_SHOT_REQUEST_MSGID, "record");
        sendMessage(message, service, 204);
    }

    //stops the recording if camera is recording
    public static void sendRecordStopRequest(BTService service) {
        Log.i(TAG, "stopping");
        BTShotMsg message = new BTShotMsg(IDS.REMOTE_SHOT_REQUEST_MSGID, "record stop");
        sendMessage(message, service, 204);
    }

    //gives some infos about the phone to the camera
    public static void sendPhoneInfo(BTService service){
        String versionName = "1.2.00.8";
        BTInfoMsg message = new BTInfoMsg(IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE, "100", IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE, "100", versionName,false);
        sendMessage(message,service, 204);
    }

    public static void sendWidgetInfoRequest(BTService service){
        BTWidgetInfoMsg message = new BTWidgetInfoMsg();
        sendMessage(message,service, 204);
    }

    public static void sendDateTimeRequest(BTService service){
        BTDateTimeMsg messsage = new BTDateTimeMsg(IDS.DATE_TIME_REQUEST_TITLE);
        sendMessage(messsage, service, 204);
    }

    //no use right now
    public static void sendCameraConfigInfoRequest(BTService service) {
        Log.d(TAG, "sendCameraConfigInfoRequest");
        BTConfigInfoMsg message = new BTConfigInfoMsg("success", "100");
        sendMessage(message, service, 204);
    }

    //not working
    public static void sendFirmwareInstallRequest(BTService service){
        Log.d(TAG, "sendFirmwareInstallRequest");
        BTFWInstallMsg message = new BTFWInstallMsg();
        sendMessage(message, service, 204);
    }

    public static void sendLiveViewRequest(BTService service){
        Log.d(TAG, "sendLiveViewRequest");
        BTCommandMsg message = new BTCommandMsg("cmd-req", "execute", IDS.COMMAND_REQUEST_DESCRIPTION_LIVEVIEW);
        sendMessage(message,service,204);
    }
}
