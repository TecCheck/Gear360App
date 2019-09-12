package de.teccheck.gear360app.bluetooth;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import de.teccheck.gear360app.Gear360Settings;
import de.teccheck.gear360app.Utils;

import static de.teccheck.gear360app.bluetooth.BTMessages.*;

public class BTMessageHandler {

    public static final String TAG = "G360_" + BTMessageHandler.class.getSimpleName();

    public static void conformAndResponseCommandJson(byte[] data) {
        JSONObject jSONObject = null;
        String msgId = null;

        try {
            jSONObject = new JSONObject(new String(data));
            BTCheckMsgId checkMsgId = new BTCheckMsgId();
            checkMsgId.fromJSON(jSONObject);
            msgId = checkMsgId.getMsgId();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Non JSON: " + new String(data));
        }

        if(jSONObject != null && msgId != null){
            switch (msgId){
                case IDS.REMOTE_SHOT_RESPONSE_MSGID:
                    handleRemoteShotReponse(jSONObject);
                    break;
                case IDS.HARDWARE_INFO_RESPONSE_MSGID:
                    handleHardwareInfoResponse(jSONObject);
                    break;
                case IDS.CAMERA_CONFIG_INFO_RESPONSE_MSGID:
                    handleCameraConfigInfoResponse(jSONObject);
                    break;
                case IDS.COMMAND_REQUEST_MSGID:
                    handleCommandMessage(jSONObject);
                    break;
                case IDS.DEVICE_INFO_MSGID:
                    handleCameraInfoResponse(jSONObject);
                default:
                    handleDefault(jSONObject);
                    break;
            }
            BTMessageLogger.logIncommingMessage(jSONObject);
        }
    }

    private static void handleDefault(JSONObject jsonObject){
        Log.i(TAG, "Message: \n" + Utils.getPrettyJsonStringFromByteData(jsonObject.toString()));
    }

    private static void handleRemoteShotReponse(JSONObject jsonObject){
        Log.i(TAG, "RemoteShotMessage: \n" + Utils.getPrettyJsonStringFromByteData(jsonObject.toString()));
        BTShotMsg message = new BTShotMsg(IDS.HARDWARE_INFO_RESPONSE_MSGID, "record");
        try {
            message.fromJSON(jsonObject);
            Log.i(TAG, "Desc: " + message.getDescription());
            if(message.getDescription().equals("capture") || message.getDescription().equals("recording")|| message.getDescription().equals("record")){
                Gear360Settings.isRecording = true;
                Log.i(TAG, "recording");
            }else {
                Gear360Settings.isRecording = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void handleHardwareInfoResponse(JSONObject jsonObject){
        Log.i(TAG, "HardwareInfoMessage: \n" + Utils.getPrettyJsonStringFromByteData(jsonObject.toString()));
    }

    private static void handleCameraConfigInfoResponse(JSONObject jsonObject){
        Log.i(TAG, "CameraConfigInfoMessage: \n" + Utils.getPrettyJsonStringFromByteData(jsonObject.toString()));
        BTConfigInfoMsg message = new BTConfigInfoMsg();
        try {
            message.fromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void handleCameraInfoResponse(JSONObject jsonObject){
        Log.i(TAG, "CameraInfoMessage: \n" + Utils.getPrettyJsonStringFromByteData(jsonObject.toString()));
        BTInfoMsg message = new BTInfoMsg();
        try {
            message.fromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Gear360Settings.fwType = message.getFwType();
        Gear360Settings.modelName = message.getModelName();
        Gear360Settings.modelVersion = message.getModelVersion();
        Gear360Settings.channel = message.getChannel();
        Gear360Settings.wifiDirectSSID = message.getWifiDirectSSID();
        Gear360Settings.softApSSID = message.getSoftApSSID();
        Gear360Settings.softApPsword = message.getSoftApPsword();
        Gear360Settings.securityType = message.getSecurityType();
        Gear360Settings.serialNum = message.getSerialNumber();
        Gear360Settings.uniqueNum = message.getUniqueNumber();
    }

    private static void handleCommandMessage(JSONObject jsonObject){
        Log.i(TAG, "CommandMessage: \n" + Utils.getPrettyJsonStringFromByteData(jsonObject.toString()));
        try {
            BTCommandMsg message = new BTCommandMsg();
            message.fromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
