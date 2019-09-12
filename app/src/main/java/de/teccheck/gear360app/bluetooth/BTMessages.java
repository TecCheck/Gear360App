package de.teccheck.gear360app.bluetooth;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.teccheck.gear360app.Gear360Settings;
import de.teccheck.gear360app.MainActivity;
import de.teccheck.gear360app.Utils;

public class BTMessages {

    public static final String TAG = "G360_" + BTMessages.class.getSimpleName();

    //Some Strings used for Bluetooth communication
    public static class IDS {

        //some final strings for Bluetooth communication

        public static final String CAMERA_CONFIG_INFO_REQUEST_DESCRIPTION = "Message structure in JSON for camera config";
        public static final String CAMERA_CONFIG_INFO_REQUEST_MSGID = "config-info";
        public static final String CAMERA_CONFIG_INFO_REQUEST_TITLE = "Camera Config information Message";

        public static final String CAMERA_CONFIG_INFO_RESPONSE_DESCRIPTION = "Message structure in JSON for camera config info response";
        public static final String CAMERA_CONFIG_INFO_RESPONSE_MSGID = "config-info";
        public static final String CAMERA_CONFIG_INFO_RESPONSE_TITLE = "Camera Config information Message";

        public static final String COMMAND_REQUEST_DESCRIPTION = "Message structure in JSON for Command request";
        public static final String COMMAND_REQUEST_DESCRIPTION_APP = "app";
        public static final String COMMAND_REQUEST_DESCRIPTION_AUTOSHARE = "autoshare";
        public static final String COMMAND_REQUEST_DESCRIPTION_AUTOTRANSFER = "autotransfer";
        public static final String COMMAND_REQUEST_DESCRIPTION_BROADCAST = "broadcast";
        public static final String COMMAND_REQUEST_DESCRIPTION_BTML = "btml";
        public static final String COMMAND_REQUEST_DESCRIPTION_BT_OFF = "bt-off";
        public static final String COMMAND_REQUEST_DESCRIPTION_CONFIG = "config";
        public static final String COMMAND_REQUEST_DESCRIPTION_FORMAT = "format";
        public static final String COMMAND_REQUEST_DESCRIPTION_FWDOWNLOAD = "fw-download";
        public static final String COMMAND_REQUEST_DESCRIPTION_LIVEVIEW = "liveview";
        public static final String COMMAND_REQUEST_DESCRIPTION_ML = "mobilelink";
        public static final String COMMAND_REQUEST_DESCRIPTION_POWER_SAVE_OFF = "power-save-off";
        public static final String COMMAND_REQUEST_DESCRIPTION_PROSUGGEST = "pro-suggest";
        public static final String COMMAND_REQUEST_DESCRIPTION_Q_AUTOSHARE = "q-autoshare";
        public static final String COMMAND_REQUEST_DESCRIPTION_REMOTE_RELEASE = "remote-release";
        public static final String COMMAND_REQUEST_DESCRIPTION_REMOTE_SHOT = "remote-shot";
        public static final String COMMAND_REQUEST_DESCRIPTION_RESET = "reset";
        public static final String COMMAND_REQUEST_DESCRIPTION_RVF = "rvf";
        public static final String COMMAND_REQUEST_DESCRIPTION_SELECTIVEPUSH = "selectivepush";
        public static final String COMMAND_REQUEST_DESCRIPTION_SELECT_SCREEN = "as-select";
        public static final String COMMAND_REQUEST_DESCRIPTION_TIME_OUT = "timeout";

        public static final String COMMAND_REQUEST_ENUM_DISCONN = "disconn";
        public static final String COMMAND_REQUEST_ENUM_DISMISS = "dismiss";
        public static final String COMMAND_REQUEST_ENUM_EXECUTE = "execute";
        public static final String COMMAND_REQUEST_ENUM_GET = "get";
        public static final String COMMAND_REQUEST_ENUM_SET = "set";
        public static final String COMMAND_REQUEST_MSGID = "cmd-req";
        public static final String COMMAND_REQUEST_TITLE = "Command request Message";
        public static final String COMMAND_REQUEST_TYPE = "object";

        public static final String COMMAND_RESPONSE_DESCRIPTION = "Message structure in JSON for Command response";
        public static final String COMMAND_RESPONSE_DESCRIPTION_ALREADY_EXECUTED = "105";
        public static final int COMMAND_RESPONSE_DESCRIPTION_ALREADY_EXECUTED_NUMBER = 105;
        public static final String COMMAND_RESPONSE_DESCRIPTION_BAD_JSON_FORMAT = "101";
        public static final int COMMAND_RESPONSE_DESCRIPTION_BAD_JSON_FORMAT_NUMBER = 101;
        public static final int COMMAND_RESPONSE_DESCRIPTION_BATTERY_LOW_NUMBER = 201;
        public static final int COMMAND_RESPONSE_DESCRIPTION_CARD_LOCK_NUMBER = 204;
        public static final int COMMAND_RESPONSE_DESCRIPTION_DCF_FULL_NUMBER = 216;
        public static final String COMMAND_RESPONSE_DESCRIPTION_FAIL_NORMAL = "103";
        public static final int COMMAND_RESPONSE_DESCRIPTION_FAIL_NORMAL_NUMBER = 103;
        public static final int COMMAND_RESPONSE_DESCRIPTION_FILE_SAVING_NUMBER = 206;
        public static final int COMMAND_RESPONSE_DESCRIPTION_HDMI_ERROR_NUMBER = 210;
        public static final String COMMAND_RESPONSE_DESCRIPTION_MEMORY_FULL = "104";
        public static final int COMMAND_RESPONSE_DESCRIPTION_MEMORY_FULL_NUMBER = 213;
        public static final int COMMAND_RESPONSE_DESCRIPTION_MOVIE_ERROR_NUMBER = 212;
        public static final int COMMAND_RESPONSE_DESCRIPTION_NOT_SUPPORTED_DURING_FOTA = 218;
        public static final int COMMAND_RESPONSE_DESCRIPTION_NO_CARD_ERROR_NUMBER = 208;
        public static final int COMMAND_RESPONSE_DESCRIPTION_NO_PHOTO_ERROR_NUMBER = 209;
        public static final int COMMAND_RESPONSE_DESCRIPTION_OVERHEATING_ERROR = 221;
        public static final String COMMAND_RESPONSE_DESCRIPTION_SUCCESS = "100";
        public static final int COMMAND_RESPONSE_DESCRIPTION_SUCCESS_NUMBER = 100;
        public static final int COMMAND_RESPONSE_DESCRIPTION_UNKNOWN_ERROR_NUMBER = 200;
        public static final int COMMAND_RESPONSE_DESCRIPTION_USB_ERROR_NUMBER = 211;
        public static final String COMMAND_RESPONSE_DESCRIPTION_WRONG_COMMAND = "102";
        public static final int COMMAND_RESPONSE_DESCRIPTION_WRONG_COMMAND_NUMBER = 102;

        public static final String COMMAND_RESPONSE_ENUM_FAILURE = "failure";
        public static final String COMMAND_RESPONSE_ENUM_SUCCESS = "success";
        public static final String COMMAND_RESPONSE_MSGID = "cmd-rsp";
        public static final String COMMAND_RESPONSE_TITLE = "Command response Message";
        public static final String COMMAND_RESPONSE_TYPE = "object";
        public static final String COMMAND_RESPONSE_TYPE_NUMBER = "number";

        public static final String DATE_TIME_REQUEST_DESCRIPTION = "Message structure in JSON for Date-Time request";
        public static final String DATE_TIME_REQUEST_MSGID = "date-time-req";
        public static final String DATE_TIME_REQUEST_TITLE = "Date-Time request Message";
        public static final String DATE_TIME_RESOPNSE_DESCRIPTION = "Message structure in JSON for Date-Time response";
        public static final String DATE_TIME_RESOPNSE_TITLE = "Date-Time response Message";
        public static final String DATE_TIME_RESPONSE_MSGID = "date-time-rsp";

        public static final String DEVICE_DESCRIPTION_URL_DESC = "Message structure in JSON for Device Description URL";
        public static final String DEVICE_DESCRIPTION_URL_MSGID = "device-desc-url";
        public static final String DEVICE_DESCRIPTION_URL_TITLE = "Device Description URL Message";

        public static final String DEVICE_INFO_MSGID = "info";
        public static final String DEVICE_INFO_REQUEST_DESCRIPTION = "Message structure in JSON for Phone Device information";
        public static final String DEVICE_INFO_REQUEST_TITLE = "Phone Device information Message";
        public static final String DEVICE_INFO_RESOPNSE_DESCRIPTION = "Message structure in JSON for Camera Device info response";
        public static final String DEVICE_INFO_RESOPNSE_TITLE = "Camera Device info response Message";

        public static final String DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE = "false";
        public static final String DEVICE_INFO_WIFI_DIRECT_ENUM_TRUE = "true";
        public static final String DEVICE_INFO_WIFI_DIRECT_SSID_TYPE = "string";

        public static final String FIRMWARE_DOWNLOAD_COMPLETE_INFO_MSGID = "fw-download-complete-info";
        public static final String FIRMWARE_DOWNLOAD_REQUEST_DESCRIPTION = "Message structure in JSON for FW Server information";
        public static final String FIRMWARE_DOWNLOAD_REQUEST_TITLE = "Phone FW Server information Message";

        public static final String FIRMWARE_INFO_REQUEST_DESCRIPTION = "Message structure in JSON for firmware version";
        public static final String FIRMWARE_INFO_REQUEST_TITLE = "Firmware version info Message";

        public static final String FIRMWARE_INSTALL_REQUEST_DESCRIPTION = "Message structure in JSON for Install FW request";
        public static final String FIRMWARE_INSTALL_REQUEST_TITLE = "Install FW Request Message";

        public static final String HARDWARE_INFO_REQUEST_MSGID = "bigdata-req";
        public static final String HARDWARE_INFO_RESPONSE_MSGID = "hw-bigdata";

        public static final String REMOTE_NOTIFICATION_HEAT_TURN_OFF = "heat-turnoff";
        public static final String REMOTE_NOTIFICATION_HEAT_WARNING = "heat-warning";
        public static final String REMOTE_NOTIFICATION_PAUSE_RECORDING = "pause-recording";
        public static final String REMOTE_NOTIFICATION_PAUSE_TIMELAPSE_RECORD = "pause-timelapse-rec";
        public static final String REMOTE_NOTIFICATION_RECORDING = "recording";
        public static final String REMOTE_NOTIFICATION_RESUME_RECORDING = "resume-recording";
        public static final String REMOTE_NOTIFICATION_RESUME_TIMELAPSE_RECORD = "resume-timelapse-rec";
        public static final String REMOTE_NOTIFICATION_TIMELAPSE_RECORD = "timelapse-rec";
        public static final String REMOTE_NOTIFICATION_TITLE = "notify";

        public static final String REMOTE_RELEASE_CODE_PRESSLOCK_AF = "Press-Lock-AF";
        public static final String REMOTE_RELEASE_CODE_PRESSLOCK_END = "Press-Lock-end";
        public static final String REMOTE_RELEASE_CODE_PRESSLOCK_START = "Press-Lock-start";
        public static final String REMOTE_RELEASE_CODE_PUSH = "Normal-push";
        public static final String REMOTE_RELEASE_CODE_RECORD_END = "Record-end";
        public static final String REMOTE_RELEASE_CODE_RECORD_START = "Record-start";
        public static final String REMOTE_RELEASE_CODE_RELEASE = "Normal-release";

        public static final String REMOTE_RELEASE_REQUEST_DESCRIPTION = "Message structure in JSON for remote release request";
        public static final String REMOTE_RELEASE_REQUEST_MSGID = "remote-release-req";
        public static final String REMOTE_RELEASE_REQUEST_TITLE = "Remote release request Message";
        public static final String REMOTE_RELEASE_RESPONSE_DESCRIPTION = "Message structure in JSON for remote release response";
        public static final String REMOTE_RELEASE_RESPONSE_MSGID = "remote-release-rsp";
        public static final String REMOTE_RELEASE_RESPONSE_TITLE = "Remote release response Message";

        public static final String REMOTE_SHOT_CODE_PUSH = "normal-push";
        public static final String REMOTE_SHOT_CODE_RECORD_END = "record-end";
        public static final String REMOTE_SHOT_CODE_RECORD_START = "record-start";
        public static final String REMOTE_SHOT_CODE_RELEASE = "normal-release";
        public static final String REMOTE_SHOT_RECORD_PAUSE = "record pause";
        public static final String REMOTE_SHOT_RECORD_RESUME = "record resume";
        public static final String REMOTE_SHOT_REQUEST_DESCRIPTION = "Message structure in JSON for remote shot request";
        public static final String REMOTE_SHOT_REQUEST_MSGID = "shot-req";
        public static final String REMOTE_SHOT_REQUEST_TITLE = "Remote shot request Message";

        public static final String REMOTE_SHOT_RESPONSE_DESCRIPTION = "Message structure in JSON for remote shot response";
        public static final String REMOTE_SHOT_RESPONSE_MSGID = "shot-rst";
        public static final String REMOTE_SHOT_RESPONSE_TITLE = "Remote shot response Message";

        public static final String REMOTE_SHOT_TYPE = "string";

        public static final String WIDGET_INFO_REQUEST_DESCRIPTION = "Message structure in JSON for Widget Info request";
        public static final String WIDGET_INFO_REQUEST_MSGID = "widget-info-req";
        public static final String WIDGET_INFO_REQUEST_TITLE = "Widget info request Message";
        public static final String WIDGET_INFO_RESPONSE_MSGID = "widget-info-rsp";
        public static final String WIDGET_INFO_UPDATE_DESCRIPTION = "Message structure in JSON for Widget Info update";
        public static final String WIDGET_INFO_UPDATE_MSGID = "widget-info-update";
        public static final String WIDGET_INFO_UPDATE_TITLE = "Widget info update Message";
    }

    public static class DISchemaConst {
        public static final String MESSAGE_REQUEST_DESCRIPTION_COMMAND = "Message structure in JSON for Command request";
        public static final String MESSAGE_REQUEST_DESCRIPTION_QUICK_AUTOSHARE = "Message structure in JSON for q-Autoshare request";
        public static final String MESSAGE_REQUEST_DESCRIPTION_WIDGET = "Message structure in JSON for Widget Info request";
        public static final String MESSAGE_REQUEST_PROPERTIES_ACTION_DESCRIPTION = "as-activate";
        public static final String MESSAGE_REQUEST_PROPERTIES_ACTION_ENUM_DISMISS = "dismiss";
        public static final String MESSAGE_REQUEST_PROPERTIES_ACTION_ENUM_EXECUTE = "execute";
        public static final String MESSAGE_REQUEST_PROPERTIES_MSG_ID_COMMAND = "cmd-req";
        public static final String MESSAGE_REQUEST_PROPERTIES_MSG_ID_QUICK_AUTOSHARE = "q-autoshare-req";
        public static final String MESSAGE_REQUEST_PROPERTIES_MSG_ID_WIDGET = "widget-info-req";
        public static final String MESSAGE_REQUEST_TITLE_COMMAND = "Command request Message";
        public static final String MESSAGE_REQUEST_TITLE_QUICK_AUTOSHARE = "q-Autoshare request Message";
        public static final String MESSAGE_REQUEST_TITLE_WIDGET = "Widget info request Message";
        public static final String MESSAGE_RESPONSE_DESCRIPTION_COMMAND = "Message structure in JSON for Command response";
        public static final String MESSAGE_RESPONSE_DESCRIPTION_QUICK_AUTOSHARE = "Message structure in JSON for q-Autoshare response";
        public static final String MESSAGE_RESPONSE_DESCRIPTION_WIDGET = "Message structure in JSON for Widget Info response";
        public static final String MESSAGE_RESPONSE_PROPERTIES_MSG_ID_COMMAND = "cmd-rsp";
        public static final String MESSAGE_RESPONSE_PROPERTIES_MSG_ID_QUICK_AUTOSHARE = "q-autoshare-rsp";
        public static final String MESSAGE_RESPONSE_PROPERTIES_MSG_ID_WIDGET = "widget-info-rsp";
        public static final String MESSAGE_RESPONSE_PROPERTIES_RESULT_FAILURE = "failure";
        public static final String MESSAGE_RESPONSE_PROPERTIES_RESULT_SUCCESS = "success";
        public static final String MESSAGE_RESPONSE_TITLE_COMMAND = "Command response Message";
        public static final String MESSAGE_RESPONSE_TITLE_QUICK_AUTOSHARE = "q-Autoshare response Message";
        public static final String MESSAGE_RESPONSE_TITLE_WIDGET = "Widget info response Message";
        public static final String MESSAGE_TYPE_NUMBER = "number";
        public static final String MESSAGE_TYPE_OBJECT = "object";
        public static final String MESSAGE_TYPE_STRING = "string";

        public static class ShotType {
            public static final String CONTINUOUS = "continuous";
            public static final String MOVIE = "movie";
            public static final String SINGLE = "single";
        }

        public static class StatusCode {
            public static final String ALREADY_EXECUTED = "105";
            public static final String BAD_JSON_FORMAT = "101";
            public static final String BATTERY_LOW = "201";
            public static final String FAIL_NORMAL = "103";
            public static final String FILE_SAVING = "206";
            public static final String MEMORY_FULL = "104";
            public static final String NORMAL_ERROR = "206";
            public static final String SUCCESS = "100";
            public static final String UNKNOWN_ERROR = "200";
            public static final String WRONG_COMMAND = "102";
        }
    }

    public interface JsonSerializable {
        void fromJSON(Object obj) throws JSONException;

        Object toJSON() throws JSONException;
    }
    
    public static class BTMessage implements JsonSerializable{

        @Override
        public void fromJSON(Object obj) throws JSONException {
            
        }

        @Override
        public Object toJSON() throws JSONException {
            return null;
        }
    }

    public static final class BTCheckMsgId extends BTMessage {
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        String mMsgId = "";

        public Object toJSON() throws JSONException {
            return null;
        }

        public void fromJSON(Object obj) throws JSONException {
            this.mMsgId = ((JSONObject) obj).getJSONObject("properties").getString("msgId");
        }

        public String getMsgId() {
            return this.mMsgId;
        }
    }

    public static final class BTCommandMsg extends BTMessage {
        public static final String ACTION = "action";
        public static final String CAP_MODE = "Mode";
        public static final String DESCRIPTION = "description";
        public static final String ENUM = "enum";
        public static final String ITEMS = "items";
        public static final String LOOP_TIME = "loop-time";
        public static final String MODE = "mode";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String RCODE = "r-code";
        public static final String RESULT = "result";
        public static final String TIMER = "timer";
        public static final String TIMER_ALTERNATE = "Timer";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String VALUE = "value";
        String mBeepValue;
        String mDescription;
        String mDescriptionValue;
        String mEnumValue;
        String mItem;
        String mItemValue;
        String mLoopingVideoRecordingTime;
        String mModeDescriptionValue;
        String mMsgId;
        int mRcodeDescriptionValue;
        String mSubTitle;
        String mTimerValue;
        String mTitle;
        String mType;

        public BTCommandMsg() {
            this.mTitle = "";
            this.mDescription = "";
            this.mType = "";
            this.mEnumValue = "";
            this.mDescriptionValue = "";
            this.mModeDescriptionValue = "";
            this.mTimerValue = "";
            this.mBeepValue = "";
            this.mItem = "";
            this.mItemValue = "";
            this.mRcodeDescriptionValue = 0;
            this.mMsgId = "";
            this.mLoopingVideoRecordingTime = "";
            this.mSubTitle = "";
        }

        public BTCommandMsg(String descriptionValue) {
            this.mTitle = "";
            this.mDescription = "";
            this.mType = "";
            this.mEnumValue = "";
            this.mDescriptionValue = "";
            this.mModeDescriptionValue = "";
            this.mTimerValue = "";
            this.mBeepValue = "";
            this.mItem = "";
            this.mItemValue = "";
            this.mRcodeDescriptionValue = 0;
            this.mMsgId = "";
            this.mLoopingVideoRecordingTime = "";
            this.mSubTitle = "";
            this.mDescription = "Message structure in JSON for Command request";
            this.mDescriptionValue = descriptionValue;
        }

        public BTCommandMsg(String msgId, String enumValue, String descriptionValue, String itemName, String itemValue) {
            this.mTitle = "";
            this.mDescription = "";
            this.mType = "";
            this.mEnumValue = "";
            this.mDescriptionValue = "";
            this.mModeDescriptionValue = "";
            this.mTimerValue = "";
            this.mBeepValue = "";
            this.mItem = "";
            this.mItemValue = "";
            this.mRcodeDescriptionValue = 0;
            this.mMsgId = "";
            this.mLoopingVideoRecordingTime = "";
            this.mSubTitle = "";
            if (msgId.equals("cmd-req")) {
                this.mTitle = "Command request Message";
                this.mDescription = "Message structure in JSON for Command request";
                this.mMsgId = "cmd-req";
                this.mSubTitle = "action";
                this.mItem = itemName;
                this.mItemValue = itemValue;
            }
            this.mType = "object";
            this.mEnumValue = enumValue;
            this.mDescriptionValue = descriptionValue;
        }

        public BTCommandMsg(String msgId, String enumValue, String descriptionValue, String value) {
            this.mTitle = "";
            this.mDescription = "";
            this.mType = "";
            this.mEnumValue = "";
            this.mDescriptionValue = "";
            this.mModeDescriptionValue = "";
            this.mTimerValue = "";
            this.mBeepValue = "";
            this.mItem = "";
            this.mItemValue = "";
            this.mRcodeDescriptionValue = 0;
            this.mMsgId = "";
            this.mLoopingVideoRecordingTime = "";
            this.mSubTitle = "";
            if (msgId.equals("cmd-rsp")) {
                this.mTitle = "Command response Message";
                this.mDescription = "Message structure in JSON for Command response";
                this.mMsgId = msgId;
                this.mSubTitle = "result";
                this.mEnumValue = enumValue;
                this.mDescriptionValue = descriptionValue;
                this.mItemValue = value;
            }
        }

        public BTCommandMsg(String msgId, String enumValue, String descriptionValue) {
            this.mTitle = "";
            this.mDescription = "";
            this.mType = "";
            this.mEnumValue = "";
            this.mDescriptionValue = "";
            this.mModeDescriptionValue = "";
            this.mTimerValue = "";
            this.mBeepValue = "";
            this.mItem = "";
            this.mItemValue = "";
            this.mRcodeDescriptionValue = 0;
            this.mMsgId = "";
            this.mLoopingVideoRecordingTime = "";
            this.mSubTitle = "";

            switch (msgId) {
                case "cmd-req": {
                    this.mTitle = "Command request Message";
                    this.mDescription = "Message structure in JSON for Command request";
                    this.mMsgId = "cmd-req";
                    this.mSubTitle = "action";
                    break;
                }
                case "cmd-rsp": {
                    this.mTitle = "Command response Message";
                    this.mDescription = "Message structure in JSON for Command response";
                    this.mMsgId = "cmd-rsp";
                    this.mSubTitle = "result";
                    break;
                }
                case "bigdata-req": {
                    this.mTitle = "Command request Message";
                    this.mDescription = "Message structure in JSON for Command request";
                    this.mMsgId = "bigdata-req";
                    this.mSubTitle = "action";
                    break;
                }
            }
            this.mType = "object";
            this.mEnumValue = enumValue;
            this.mDescriptionValue = descriptionValue;
        }
        
        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", this.mType);
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            JSONObject action_json = new JSONObject();
            action_json.put("enum", this.mEnumValue);
            action_json.put("description", this.mDescriptionValue);
            properties_json.put(this.mSubTitle, action_json);
            if (this.mMsgId.equals("cmd-rsp")) {
                JSONObject rcode_json = new JSONObject();
                rcode_json.put("type", "number");
                rcode_json.put("description", 100);
                properties_json.put("r-code", rcode_json);
            }
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            JSONObject properties_json = ((JSONObject) obj).getJSONObject("properties");
            JSONObject sub_json = new JSONObject();
            this.mMsgId = properties_json.getString("msgId");

            switch (mMsgId){
                case IDS.FIRMWARE_DOWNLOAD_COMPLETE_INFO_MSGID:
                    sub_json = properties_json.getJSONObject("action");
                    break;
                case "cmd-req":
                    sub_json = properties_json.getJSONObject("action");
                    break;
                case "cmd-rsp":
                    sub_json = properties_json.getJSONObject("result");
                    if (!properties_json.isNull("r-code")) {
                        this.mRcodeDescriptionValue = properties_json.getJSONObject("r-code").getInt("description");
                        break;
                    }
                    break;
            }

            this.mEnumValue = sub_json.getString("enum");
            if (!sub_json.isNull("description")) {
                this.mDescriptionValue = sub_json.getString("description");
            }

            if (this.mEnumValue.equals(IDS.COMMAND_REQUEST_ENUM_SET)) {
                JSONObject item_json = new JSONObject();
                JSONObject mode_json = new JSONObject();
                JSONObject timer_json = new JSONObject();
                JSONObject loopTime_json = new JSONObject();
                JSONObject beep_json = new JSONObject();
                item_json = sub_json.getJSONObject("items");
                if (!item_json.isNull(MODE)) {
                    this.mModeDescriptionValue = item_json.getJSONObject(MODE).getString("description");
                    if (Gear360Settings.confMode != null) {
                        Gear360Settings.confMode.setDefaultValue(this.mModeDescriptionValue);
                    }
                } else if (!item_json.isNull("Mode")) {
                    this.mModeDescriptionValue = item_json.getJSONObject("Mode").getString("description");
                    if (Gear360Settings.confMode != null) {
                        Gear360Settings.confMode.setDefaultValue(this.mModeDescriptionValue);
                    }
                }
                if (!item_json.isNull("timer")) {
                    this.mTimerValue = item_json.getJSONObject("timer").getString("description");
                    if (Gear360Settings.confTimer != null) {
                        Gear360Settings.confTimer.setDefaultValue(this.mTimerValue);
                    }
                }
                if (!item_json.isNull(TIMER_ALTERNATE)) {
                    this.mTimerValue = item_json.getJSONObject(TIMER_ALTERNATE).getString("description");
                    if (Gear360Settings.confTimer != null) {
                        Gear360Settings.confTimer.setDefaultValue(this.mTimerValue);
                    }
                }
                if (!item_json.isNull(LOOP_TIME)) {
                    this.mLoopingVideoRecordingTime = item_json.getJSONObject(LOOP_TIME).getString("description");
                    if (Gear360Settings.confLoopingVideoTime != null) {
                        Gear360Settings.confLoopingVideoTime.setDefaultValue(this.mLoopingVideoRecordingTime);
                    }
                }
                if (!item_json.isNull(BTConfigInfoMsg.BEEP)) {
                    this.mBeepValue = item_json.getJSONObject(BTConfigInfoMsg.BEEP).getString("description");
                    if (Gear360Settings.confBeep != null) {
                        Gear360Settings.confBeep.setDefaultValue(this.mBeepValue);
                    }
                }
            }
        }

        public String getMsgId() {
            return this.mMsgId;
        }

        public String getEnum() {
            return this.mEnumValue;
        }

        public String getDescription() {
            return this.mDescriptionValue;
        }

        public String getModeDescriptionValue() {
            return this.mModeDescriptionValue;
        }

        public int getRcodeDescription() {
            return this.mRcodeDescriptionValue;
        }

        public String getTimerValue() {
            return this.mTimerValue;
        }

        public String getLoopTimeValue() {
            return this.mLoopingVideoRecordingTime;
        }
    }

    public static final class BTConfigInfoMsg extends BTMessage {
        public static final String AUTO_POWER_OFF = "Auto Power Off";
        public static final String BEEP = "Beep";
        public static final String COUNT = "count";
        public static final String DEFAULT = "default";
        public static final String DESCRIPTION = "description";
        public static final String ENUM = "enum";
        public static final String FORMAT = "Format";
        public static final String FUNCTION = "functions";
        public static final String ISO = "ISO";
        public static final String ISO_LIMIT = "ISO Limit";
        public static final String ITEMS = "items";
        public static final String LED_INDICATOR = "Led indicator";
        public static final String LENS_MODE = "Lens Mode";
        public static final String LOOPING_VIDEO_RECORDING_TIME = "Looping Video Recording Time";
        public static final String MODE = "Mode";
        public static final String MOVIE_SIZE = "Movie Size";
        public static final String MOVIE_SIZE_DUAL = "Size(Dual)";
        public static final String MOVIE_SIZE_SINGLE = "Size(Single)";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String RESET = "Reset";
        public static final String RESULT = "result";
        public static final String R_CODE = "r-code";
        public static final String SHARPNESS = "Sharpness";
        public static final String SUBTITLE = "sub-title";
        public static final String TIMER = "timer";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String VALUE = "value";
        public static final String VIDEO_OUT = "Video out";
        public static final String WHITE_BALANCE = "White Balance";
        public static final String WIND_CUT = "Wind Cut";
        String mDescription;
        String mDescriptionValue;
        String mEnumValue;
        String mMsgId;
        String mTitle;

        public BTConfigInfoMsg() {
            this.mTitle = "Camera Config information Message";
            this.mDescription = IDS.CAMERA_CONFIG_INFO_RESPONSE_DESCRIPTION;
            this.mMsgId = "config-info";
            this.mDescriptionValue = "";
            this.mEnumValue = "";
        }

        public BTConfigInfoMsg(String enumValue, String descriptionValue) {
            this.mTitle = "";
            this.mDescription = "";
            this.mMsgId = "";
            this.mDescriptionValue = "";
            this.mEnumValue = "";
            this.mTitle = "Camera Config information Message";
            this.mDescription = IDS.CAMERA_CONFIG_INFO_RESPONSE_DESCRIPTION;
            this.mMsgId = "config-info";
            this.mEnumValue = enumValue;
            this.mDescriptionValue = descriptionValue;
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            if (this.mMsgId.equals("config-info")) {
                JSONObject result_json = new JSONObject();
                result_json.put("enum", this.mEnumValue);
                result_json.put("description", this.mDescriptionValue);
                properties_json.put("result", result_json);
            }
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            
            JSONObject functions_json = ((JSONObject) obj).getJSONObject("properties").getJSONObject("functions");
            int count = functions_json.getInt("count");
            JSONObject items_json = functions_json.getJSONObject("items");
            for (int i = 1; i <= count; i++) {
                JSONObject config_json = items_json.getJSONObject(Integer.toString(i));
                String name = config_json.getString(SUBTITLE);
                String value = config_json.getString("value");
                String defaultValue = config_json.getString("default");
                String[] values = value.split(",");
                if (name.equalsIgnoreCase(WHITE_BALANCE)) {
                    Gear360Settings.confWhiteBalance.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(ISO)) {
                    Gear360Settings.confISO.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(MOVIE_SIZE)) {
                    Gear360Settings.confMovieSize.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(BEEP)) {
                    Gear360Settings.confBeep.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(LED_INDICATOR)) {
                    Gear360Settings.confLedIndicator.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(AUTO_POWER_OFF)) {
                    Gear360Settings.confAutoPowerOff.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(VIDEO_OUT)) {
                    Gear360Settings.confVideoOut.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(FORMAT)) {
                    Gear360Settings.confFormat.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(RESET)) {
                    Gear360Settings.confReset.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(LOOPING_VIDEO_RECORDING_TIME)) {
                    Gear360Settings.confLoopingVideoTime.setConf(name, values, defaultValue);
                } else if (name.contains(MOVIE_SIZE_DUAL)) {
                    Gear360Settings.confMovieSizeDual.setConf(name, values, defaultValue);
                } else if (name.contains(MOVIE_SIZE_SINGLE)) {
                    Gear360Settings.confMovieSizeSingle.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(LENS_MODE)) {
                    Gear360Settings.confLensMode.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(ISO_LIMIT)) {
                    Gear360Settings.confIsoLimit.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(SHARPNESS)) {
                    Gear360Settings.confSharpness.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase(WIND_CUT)) {
                    Gear360Settings.confWindCut.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase("Mode")) {
                    Gear360Settings.confMode.setConf(name, values, defaultValue);
                } else if (name.equalsIgnoreCase("timer")) {
                    Gear360Settings.confTimer.setConf(name, values, defaultValue);
                }
            }
        }
    }

    public static final class BTDateTimeMsg extends BTMessage {
        public static final String DATE = "date";
        public static final String DESCRIPTION = "description";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String REGION = "region";
        public static final String SUMMER = "summer";
        public static final String TIME = "time";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        String mDescription = "";
        String mDescription_Data = "";
        String mDescription_Region = "";
        String mDescription_Summer = IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE;
        String mDescription_Time = "";
        String mMsgId = "";
        String mTitle = "";

        public BTDateTimeMsg(String msgId) {
            if (msgId.equals(IDS.DATE_TIME_REQUEST_MSGID)) {
                this.mTitle = IDS.DATE_TIME_REQUEST_TITLE;
                this.mDescription = IDS.DATE_TIME_REQUEST_DESCRIPTION;
                this.mMsgId = IDS.DATE_TIME_REQUEST_MSGID;
            } else {
                if (msgId.equals(IDS.DATE_TIME_RESPONSE_MSGID)) {
                    this.mTitle = IDS.DATE_TIME_RESOPNSE_TITLE;
                    this.mDescription = IDS.DATE_TIME_RESOPNSE_DESCRIPTION;
                    this.mMsgId = IDS.DATE_TIME_RESPONSE_MSGID;
                }
            }
            String date = new SimpleDateFormat("yyy/MM/dd", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
            this.mDescription_Data = date;
            this.mDescription_Time = time;
            TimeZone tzone = TimeZone.getDefault();
            String time_zone = tzone.getDisplayName();
            boolean summer = tzone.useDaylightTime();
            String tzoneId = tzone.getID();
            String timeZone = new SimpleDateFormat("Z", Locale.getDefault()).format(Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault()).getTime());
            this.mDescription_Region = timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
            if (summer) {
                this.mDescription_Summer = "true";
            } else {
                this.mDescription_Summer = IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE;
            }
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            if (!this.mMsgId.equals(IDS.DATE_TIME_REQUEST_MSGID) && this.mMsgId.equals(IDS.DATE_TIME_RESPONSE_MSGID)) {
                JSONObject date_json = new JSONObject();
                date_json.put("type", "string");
                date_json.put("description", this.mDescription_Data);
                JSONObject time_json = new JSONObject();
                time_json.put("type", "string");
                time_json.put("description", this.mDescription_Time);
                JSONObject region_json = new JSONObject();
                region_json.put("type", "string");
                region_json.put("description", this.mDescription_Region);
                JSONObject summer_json = new JSONObject();
                summer_json.put("type", "string");
                summer_json.put("description", this.mDescription_Summer);
                properties_json.put(DATE, date_json);
                properties_json.put("time", time_json);
                properties_json.put("region", region_json);
                properties_json.put(SUMMER, summer_json);
            }
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            JSONObject properties_json = ((JSONObject) obj).getJSONObject("properties");
            this.mMsgId = properties_json.getString("msgId");
            if (!properties_json.isNull(DATE)) {
                this.mDescription_Data = properties_json.getJSONObject(DATE).getString("description");
            }
            if (!properties_json.isNull("time")) {
                this.mDescription_Time = properties_json.getJSONObject("time").getString("description");
            }
        }

        public String getMsgId() {
            return this.mMsgId;
        }

        public String getDate() {
            return this.mDescription_Data;
        }

        public String getTime() {
            return this.mDescription_Time;
        }
    }

    public static final class BTFWDownloadMsg extends BTMessage {
        public static final String DESCRIPTION = "description";
        public static final String MSG_ID = "msgId";
        public static final String MSG_ID_VALUE = "fw-server-info";
        public static final String PROPERTIES = "properties";
        public static final String RESULT = "result";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String URL_S = "server-url";
        String mDescription = "";
        String mMsgId = "";
        String mTitle = "";
        String mserverURLVal = "";

        public BTFWDownloadMsg(String serverURL) {
            this.mserverURLVal = serverURL;
            this.mTitle = IDS.FIRMWARE_DOWNLOAD_REQUEST_TITLE;
            this.mDescription = IDS.FIRMWARE_DOWNLOAD_REQUEST_DESCRIPTION;
            this.mMsgId = MSG_ID_VALUE;
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            JSONObject serverURLJSON = new JSONObject();
            serverURLJSON.put("type", "string");
            serverURLJSON.put("description", this.mserverURLVal);
            properties_json.put(URL_S, serverURLJSON);
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            throw new JSONException(">> This Class doesn't support fromJSON. If you want support, implement it <<");
        }

        public String getMsgId() {
            return this.mMsgId;
        }
    }

    //unused
    public static final class BTFWInfoMsg extends BTMessage {
        public static final String DESCRIPTION = "description";
        public static final String MSG_ID = "msgId";
        public static final String MSG_ID_VALUE = "firmware-version-info";
        public static final String PROPERTIES = "properties";
        public static final String RESULT = "result";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String URL_S = "download-url";
        public static final String VERSION = "server-version";
        String mDescription = "";
        String mFirmwareUrlVal = "";
        String mFirmwareVersionVal = "";
        String mMsgId = "";
        String mTitle = "";

        public BTFWInfoMsg(String firmwareVersion, String firmwareUrl) {
            this.mFirmwareUrlVal = firmwareUrl;
            this.mFirmwareVersionVal = firmwareVersion;
            this.mTitle = IDS.FIRMWARE_INFO_REQUEST_TITLE;
            this.mDescription = IDS.FIRMWARE_INFO_REQUEST_DESCRIPTION;
            this.mMsgId = MSG_ID_VALUE;
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            JSONObject version_json = new JSONObject();
            version_json.put("type", "string");
            version_json.put("description", this.mFirmwareVersionVal);
            JSONObject fwurl_json = new JSONObject();
            fwurl_json.put("type", "string");
            fwurl_json.put("description", this.mFirmwareUrlVal);
            properties_json.put(VERSION, version_json);
            properties_json.put(URL_S, fwurl_json);
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            throw new JSONException(">> This Class doesn't support fromJSON. If you want support, implement it <<");
        }

        public String getMsgId() {
            return this.mMsgId;
        }
    }

    public static final class BTFWInstallMsg extends BTMessage {
        public static final String DESCRIPTION = "description";
        public static final String MSG_ID = "msgId";
        public static final String MSG_ID_VALUE = "install-req";
        public static final String PROPERTIES = "properties";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        String mDescription;
        String mMsgId;
        String mTitle;

        public BTFWInstallMsg() {
            this.mTitle = "";
            this.mDescription = "";
            this.mMsgId = "";
            this.mTitle = IDS.FIRMWARE_INSTALL_REQUEST_TITLE;
            this.mDescription = IDS.FIRMWARE_INSTALL_REQUEST_DESCRIPTION;
            this.mMsgId = MSG_ID_VALUE;
        }

        public BTFWInstallMsg(String msgId) {
            this.mTitle = "";
            this.mDescription = "";
            this.mMsgId = "";
            this.mTitle = IDS.FIRMWARE_INSTALL_REQUEST_TITLE;
            this.mDescription = IDS.FIRMWARE_INSTALL_REQUEST_DESCRIPTION;
            this.mMsgId = msgId;
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            throw new JSONException(">> This Class doesn't support fromJSON. If you want support, implement it <<");
        }

        public String getMsgId() {
            return this.mMsgId;
        }
    }

    public static final class BTInfoMsg extends BTMessage {
        public static final String APP_VERSION = "app-version";
        public static final String BOARD_REVISION = "board-revision";
        public static final String BTDEVICENAME = "device-name";
        public static final String CAMERA_WIFI_DIRECT_MAC = "wifi-direct-mac";
        public static final String CAMERA_WIFI_MAC = "wifi-mac";
        public static final String CHANNEL = "channel";
        public static final String CH_NEGOTIATION_WA = "ch-negotiation-wa";
        public static final String DEFAULTURL = "bt-fota-test-url";
        public static final String DESCRIPTION = "description";
        public static final String ENUM = "enum";
        public static final String FW_TYPE = "fw-type";
        public static final String MODELNAME = "model-name";
        public static final String MODELVERSION = "model-version";
        public static final String MSG_ID = "msgId";
        public static final String NFC = "nfc";
        public static final String OP_MODE = "op-mode";
        public static final String OP_MODE_DESC_RETAIL = "retail";
        public static final String OP_MODE_DESC_USER = "user";
        public static final String PROPERTIES = "properties";
        public static final String RESULT = "result";
        public static final String SECURITYTYPE = "security-type";
        public static final String SERIAL_NUMBER = "serial-number";
        public static final String SOFTAPPSWORD = "softap-psword";
        public static final String SOFTAPSSID = "softap-ssid";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static final String UNIQUE_NUMBER = "unique-number";
        public static final String WIFIDIRECT = "wifi-direct";
        public static final String WIFIDIRECTSSID = "wifi-direct-ssid";
        public static final String WIFIMACADDRESS = "wifi-mac-address";
        String ch_negotiation_wa_value = "";
        String mAppVersion = "";
        String mBoardRevision = "";
        String mChannel = "";
        String mDescription = "";
        String mDescriptionValue = "";
        String mDescription_DeviceName = "";
        String mDescription_WifiMac = "";
        String mEnumValue = "";
        String mFwType = "";
        String mModelName = "";
        String mModelVersion = "";
        String mNFCDescriptionValue = "";
        String mNFCEnumValue = "";
        String mOpMode = "user";
        String mSecurityType = "";
        String mSerialNumber = "";
        String mSoftApPsword = "";
        String mSoftApSSID = "";
        String mSubTitle = "";
        String mTitle = "";
        String mUniqueNumber = "";
        String mWifiDirectSSID = "";
        String mWifiMac = "";
        String mWifidirectMac = "";
        String mfwDownloadURL = "";

        public BTInfoMsg(){

        }

        public BTInfoMsg(String nfcenumValue, String nfcdescriptionValue, String enumValue, String descriptionValue, String mVersionName, boolean isRetailMode) {
            if (enumValue.equals("true") || enumValue.equals(IDS.DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE)) {
                this.mTitle = IDS.DEVICE_INFO_REQUEST_TITLE;
                this.mDescription = IDS.DEVICE_INFO_REQUEST_DESCRIPTION;
                this.mSubTitle = WIFIDIRECT;
                this.mDescription_WifiMac = Utils.getWifiMacAddress();
                this.mDescription_DeviceName = BTService.BTName;
            } else if (enumValue.equals("success") || enumValue.equals("failure")) {
                this.mTitle = IDS.DEVICE_INFO_RESOPNSE_TITLE;
                this.mDescription = IDS.DEVICE_INFO_RESOPNSE_DESCRIPTION;
                this.mSubTitle = "result";
            }
            this.mAppVersion = mVersionName;
            this.mEnumValue = enumValue;
            this.mDescriptionValue = descriptionValue;
            this.mNFCEnumValue = nfcenumValue;
            this.mNFCDescriptionValue = nfcdescriptionValue;
            if (isRetailMode) {
                this.mOpMode = OP_MODE_DESC_RETAIL;
            } else {
                this.mOpMode = "user";
            }
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", IDS.DEVICE_INFO_MSGID);
            JSONObject direct_json = new JSONObject();
            direct_json.put("enum", this.mEnumValue);
            JSONObject negotiation_wa_json = new JSONObject();
            negotiation_wa_json.put("description", this.ch_negotiation_wa_value);
            direct_json.put(CH_NEGOTIATION_WA, negotiation_wa_json);
            properties_json.put(this.mSubTitle, direct_json);
            if (!this.mDescription_WifiMac.isEmpty()) {
                JSONObject wifi_mac = new JSONObject();
                wifi_mac.put("type", "string");
                wifi_mac.put("description", this.mDescription_WifiMac);
                properties_json.put(WIFIMACADDRESS, wifi_mac);
            }
            if (!this.mDescription_DeviceName.isEmpty()) {
                JSONObject bt_device_name = new JSONObject();
                bt_device_name.put("type", "string");
                bt_device_name.put("description", this.mDescription_DeviceName);
                properties_json.put(BTDEVICENAME, bt_device_name);
            }
            if (!this.mAppVersion.isEmpty()) {
                JSONObject app_version = new JSONObject();
                app_version.put("type", "string");
                app_version.put("description", this.mAppVersion);
                properties_json.put(APP_VERSION, app_version);
            }
            JSONObject op_mode = new JSONObject();
            op_mode.put("type", "string");
            op_mode.put("description", this.mOpMode);
            properties_json.put(OP_MODE, op_mode);
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            JSONObject board_json;
            JSONObject properties_json = ((JSONObject) obj).getJSONObject("properties");
            if (properties_json.isNull(MODELVERSION)) {
                this.mModelName = "NX1";
                this.mModelVersion = "0.99";
            } else {
                JSONObject model_name = properties_json.getJSONObject(MODELNAME);
                JSONObject model_version = properties_json.getJSONObject(MODELVERSION);
                this.mModelName = model_name.getString("description");
                this.mModelVersion = model_version.getString("description");
            }
            if (properties_json.isNull(DEFAULTURL)) {
                this.mfwDownloadURL = "NONE";
            } else {
                this.mfwDownloadURL = properties_json.getJSONObject(DEFAULTURL).getString("description");
                //ReceivedWifiApInfo.getInstance().setfwDownloadURL(this.mfwDownloadURL);
            }
            if (properties_json.isNull(WIFIDIRECTSSID)) {
                this.mSoftApSSID = properties_json.getJSONObject(SOFTAPSSID).getString("description");
                if (!properties_json.isNull(SOFTAPPSWORD)) {
                    this.mSoftApPsword = properties_json.getJSONObject(SOFTAPPSWORD).getString("description");
                }
                if (!properties_json.isNull(SECURITYTYPE)) {
                    this.mSecurityType = properties_json.getJSONObject(SECURITYTYPE).getString("description");
                }
            } else {
                this.mWifiDirectSSID = properties_json.getJSONObject(WIFIDIRECTSSID).getString("description");
                if (!properties_json.isNull("channel")) {
                    this.mChannel = properties_json.getJSONObject("channel").getString("description");
                }
            }
            if (!properties_json.isNull(BOARD_REVISION)) {
                board_json = properties_json.getJSONObject(BOARD_REVISION);
                if (board_json != null) {
                    this.mBoardRevision = board_json.getString("description");
                    //ReceivedWidgetInfo.getInstance().setBoardRevison(this.mBoardRevision);
                }
            }
            if (!properties_json.isNull(CAMERA_WIFI_MAC)) {
                JSONObject wifiMac_json = properties_json.getJSONObject(CAMERA_WIFI_MAC);
                if (wifiMac_json != null) {
                    this.mWifiMac = wifiMac_json.getString("description");
                    //ReceivedWidgetInfo.getInstance().setCameraWifiMac(this.mWifiMac);
                }
            }
            if (!properties_json.isNull(CAMERA_WIFI_DIRECT_MAC)) {
                JSONObject mWifidirectMac_json = properties_json.getJSONObject(CAMERA_WIFI_DIRECT_MAC);
                if (mWifidirectMac_json != null) {
                    this.mWifidirectMac = mWifidirectMac_json.getString("description");
                    //ReceivedWidgetInfo.getInstance().setCameraWifiDirectMac(this.mWifidirectMac);
                }
            }
            if (!properties_json.isNull(FW_TYPE)) {
                board_json = properties_json.getJSONObject(FW_TYPE);
                if (board_json != null) {
                    this.mFwType = board_json.getString("description");
                }
            }
            if (!properties_json.isNull(SERIAL_NUMBER)) {
                board_json = properties_json.getJSONObject(SERIAL_NUMBER);
                if (board_json != null) {
                    this.mSerialNumber = board_json.getString("description");
                }
            }
            if (!properties_json.isNull(UNIQUE_NUMBER)) {
                board_json = properties_json.getJSONObject(UNIQUE_NUMBER);
                if (board_json != null) {
                    this.mUniqueNumber = board_json.getString("description");
                }
            }
        }

        public String getFwType() {
            return this.mFwType;
        }

        public String getModelName() {
            return this.mModelName;
        }

        public String getModelVersion() {
            return this.mModelVersion;
        }

        public String getChannel() {
            return this.mChannel;
        }

        public String getWifiDirectSSID() {
            return this.mWifiDirectSSID;
        }

        public String getSoftApSSID() {
            return this.mSoftApSSID;
        }

        public String getSoftApPsword() {
            return this.mSoftApPsword;
        }

        public String getSecurityType() {
            return this.mSecurityType;
        }

        public String getSerialNumber() {
            return this.mSerialNumber;
        }

        public String getUniqueNumber() {
            return this.mUniqueNumber;
        }
    }

    //unused
    public static final class BTReleaseMsg extends BTMessage {
        public static final String DESCRIPTION = "description";
        public static final String ENUM = "enum";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String RESULT = "result";
        public static final String R_CODE = "r-code";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        String mDescription = "";
        String mDescriptionValue = "";
        String mEnumValue = "";
        String mMsgId = "";
        int mRcodeDescriptionValue = 0;
        String mTitle = "";

        public BTReleaseMsg(String msgId, String descriptionValue) {
            if (msgId.equals(IDS.REMOTE_RELEASE_REQUEST_MSGID)) {
                this.mTitle = IDS.REMOTE_RELEASE_REQUEST_TITLE;
                this.mDescription = IDS.REMOTE_RELEASE_REQUEST_DESCRIPTION;
                this.mMsgId = IDS.REMOTE_RELEASE_REQUEST_MSGID;
            } else if (msgId.equals(IDS.REMOTE_RELEASE_RESPONSE_MSGID)) {
                this.mTitle = IDS.REMOTE_RELEASE_RESPONSE_TITLE;
                this.mDescription = IDS.REMOTE_RELEASE_RESPONSE_DESCRIPTION;
                this.mMsgId = IDS.REMOTE_RELEASE_RESPONSE_MSGID;
            }
            this.mDescriptionValue = descriptionValue;
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            if (this.mMsgId.equals(IDS.REMOTE_RELEASE_REQUEST_MSGID)) {
                JSONObject rCode_json = new JSONObject();
                rCode_json.put("type", "string");
                rCode_json.put("description", this.mDescriptionValue);
                properties_json.put("r-code", rCode_json);
            } else if (this.mMsgId.equals(IDS.REMOTE_RELEASE_RESPONSE_MSGID)) {
            }
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            JSONObject properties_json = ((JSONObject) obj).getJSONObject("properties");
            this.mMsgId = properties_json.getString("msgId");
            JSONObject result_json = null;
            if (this.mMsgId.equals(IDS.REMOTE_RELEASE_REQUEST_MSGID)) {
                result_json = properties_json.getJSONObject("r-code");
            } else if (this.mMsgId.equals(IDS.REMOTE_RELEASE_RESPONSE_MSGID)) {
                result_json = properties_json.getJSONObject("result");
                if (!properties_json.isNull("r-code")) {
                    this.mRcodeDescriptionValue = properties_json.getJSONObject("r-code").getInt("description");
                }
            }
            if (result_json != null) {
                this.mEnumValue = result_json.getString("enum");
                this.mDescriptionValue = result_json.getString("description");
            }
        }

        public String getMsgId() {
            return this.mMsgId;
        }

        public String getEnum() {
            return this.mEnumValue;
        }

        public String getDescription() {
            return this.mDescriptionValue;
        }

        public int getRcodeDescription() {
            return this.mRcodeDescriptionValue;
        }
    }

    public static final class BTShotMsg extends BTMessage {
        public static final String CAPTURABLE_COUNT = "capturable-count";
        public static final String DESCRIPTION = "description";
        public static final String ENUM = "enum";
        public static final String EXTENSION_INFO = "extension-info";
        public static final String ITEMS = "items";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String RECORDABLE_TIME = "recordable-time";
        public static final String RESULT = "result";
        public static final String R_CODE = "r-code";
        public static final String SHOT = "shot";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        public static int mCapturableCount = 0;
        public static int mRecordableTime = 0;
        String mDescription = "";
        String mDescriptionValue = "";
        String mEnumValue = "";
        String mMsgId = "";
        int mRcodeDescriptionValue = 0;
        String mTitle = "";

        public BTShotMsg(String msgId, String descriptionValue) {
            if (msgId.equals(IDS.REMOTE_SHOT_REQUEST_MSGID)) {
                this.mTitle = IDS.REMOTE_SHOT_REQUEST_TITLE;
                this.mDescription = IDS.REMOTE_SHOT_REQUEST_DESCRIPTION;
                this.mMsgId = IDS.REMOTE_SHOT_REQUEST_MSGID;
            } else if (msgId.equals(IDS.REMOTE_SHOT_RESPONSE_MSGID)) {
                this.mTitle = IDS.REMOTE_SHOT_RESPONSE_TITLE;
                this.mDescription = IDS.REMOTE_SHOT_RESPONSE_DESCRIPTION;
                this.mMsgId = IDS.REMOTE_SHOT_RESPONSE_MSGID;
            }
            this.mDescriptionValue = descriptionValue;
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            if (this.mMsgId.equals(IDS.REMOTE_SHOT_REQUEST_MSGID)) {
                JSONObject shot_json = new JSONObject();
                shot_json.put("type", "string");
                shot_json.put("description", this.mDescriptionValue);
                properties_json.put("items", shot_json);
            } else if (this.mMsgId.equals(IDS.REMOTE_SHOT_RESPONSE_MSGID)) {
            }
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
            JSONObject properties_json = ((JSONObject) obj).getJSONObject("properties");
            this.mMsgId = properties_json.getString("msgId");
            JSONObject result_json = null;
            if (this.mMsgId.equals(IDS.REMOTE_SHOT_REQUEST_MSGID)) {
                result_json = properties_json.getJSONObject("r-code");
            } else if (this.mMsgId.equals(IDS.REMOTE_SHOT_RESPONSE_MSGID)) {
                result_json = properties_json.getJSONObject("result");
                if (!properties_json.isNull("r-code")) {
                    this.mRcodeDescriptionValue = properties_json.getJSONObject("r-code").getInt("description");
                }
                if (!properties_json.isNull(EXTENSION_INFO)) {
                    JSONObject info;
                    JSONObject extensionInfo_json = properties_json.getJSONObject(EXTENSION_INFO);
                    if (!extensionInfo_json.isNull("recordable-time")) {
                        info = extensionInfo_json.getJSONObject("recordable-time");
                        if (info != null) {
                            mRecordableTime = info.getInt("description");
                            //Trace.m8098d(Tag.BT, "BTJsonSerializable==>> mRecordableTime = " + mRecordableTime);
                            //ReceivedWidgetInfo.getInstance().setRecordableTime(mRecordableTime);
                        }
                    }
                    if (!extensionInfo_json.isNull("capturable-count")) {
                        info = extensionInfo_json.getJSONObject("capturable-count");
                        if (info != null) {
                            mCapturableCount = info.getInt("description");
                            //Trace.m8098d(Tag.BT, "BTJsonSerializable==>> mCapturableCount = " + mCapturableCount);
                            //ReceivedWidgetInfo.getInstance().setCapturableCount(mCapturableCount);
                        }
                    }
                }
            }
            if (result_json != null) {
                this.mEnumValue = result_json.getString("enum");
                this.mDescriptionValue = result_json.getString("description");
            }
        }

        public String getMsgId() {
            return this.mMsgId;
        }

        public String getEnum() {
            return this.mEnumValue;
        }

        public String getDescription() {
            return this.mDescriptionValue;
        }

        public int getRcodeDescription() {
            return this.mRcodeDescriptionValue;
        }
    }

    public static final class BTWidgetGPSMsg extends BTMessage {
        public static final String ARRAY = "array";
        public static final String DESCRIPTION = "description";
        public static final String ENUM = "enum";
        public static final String GPS = "gps";
        public static final String ITEMS = "items";
        public static final String LIST = "list";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String RESULT = "result";
        public static final String TITLE = "title";
        public static final String TYPE = "type";
        String mDescription = "";
        String mGpsDescVal = "";
        String mGpsEnumVal = "";
        String mMsgId = "";
        String mResultDesc = "";
        String mResultEnumVal = "";
        String mTitle = "";

        public BTWidgetGPSMsg(String msgId, String enumVal, String resultDesc, String gpsEnum, String gpsDesc) {
            if (msgId.equals("widget-info-req")) {
                this.mTitle = "Widget info request Message";
                this.mDescription = "Message structure in JSON for Widget Info request";
                this.mMsgId = "widget-info-req";
            } else if (msgId.equalsIgnoreCase("widget-info-rsp")) {
                this.mTitle = DISchemaConst.MESSAGE_RESPONSE_TITLE_WIDGET;
                this.mDescription = DISchemaConst.MESSAGE_RESPONSE_DESCRIPTION_WIDGET;
                this.mMsgId = "widget-info-rsp";
            }
            this.mResultEnumVal = enumVal;
            this.mResultDesc = resultDesc;
            this.mGpsEnumVal = gpsEnum;
            this.mGpsDescVal = gpsDesc;
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            if (!this.mMsgId.equals("widget-info-req") && this.mMsgId.equals("widget-info-rsp")) {
                JSONObject result_json = new JSONObject();
                result_json.put("enum", this.mResultEnumVal);
                result_json.put("description", this.mResultDesc);
                JSONObject gps_json = new JSONObject();
                gps_json.put("enum", this.mGpsEnumVal);
                gps_json.put("description", this.mGpsDescVal);
                JSONObject items_json = new JSONObject();
                items_json.put("result", result_json);
                items_json.put("gps", gps_json);
                JSONObject list_json = new JSONObject();
                list_json.put("type", ARRAY);
                list_json.put("items", items_json);
                properties_json.put("list", list_json);
            }
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) throws JSONException {
        }

        public String getMsgId() {
            return this.mMsgId;
        }
    }

    public static final class BTWidgetInfoMsg extends BTMessage {
        public static final String AUTO_POWEROFF = "auto-poweroff";
        public static final String AVAILABLE_MEMORY = "available-memory";
        public static final String BATTERY = "battery";
        public static final String BATTERY_STATE = "battery-state";
        public static final String CAPTURABLE_COUNT = "capturable-count";
        public static final String CAPTURE_STATE = "capture-state";
        public static final String DESCRIPTION = "description";
        public static final String ENUM = "enum";
        public static final String FREE_MEMORY = "free-memory";
        public static final String ITEMS = "items";
        public static final String LIST = "list";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String RECORDABLE_TIME = "recordable-time";
        public static final String RECORD_STATE = "record-state";
        public static final String RESULT = "result";
        public static final String SAVED_IMAGE = "saved-image";
        public static final String SAVED_VIDEO = "saved-video";
        public static final String TITLE = "title";
        public static final String TOTAL_MEMORY = "total-memory";
        public static final String TYPE = "type";
        public static final String USED_MEMORY = "used-memory";
        public static String mAutoPowerOff = "";
        public static String mBatteryStateValue = "";
        public static int mBatteryValue = 0;
        public static int mCapturableCount = 0;
        public static String mCaptureState = "";
        public static int mFreeMemoryValue = 0;
        public static String mRecordState = "";
        public static int mRecordableTime = 0;
        public static int mSavedImageValue = 0;
        public static int mSavedVideoValue = 0;
        public static int mTotalMemoryValue = 0;
        public static int mUsedMemoryValue = 0;
        String mDescription;
        String mDescriptionValue;
        String mEnumValue;
        String mMsgId;
        String mTitle;

        public BTWidgetInfoMsg() {
            this.mTitle = "";
            this.mDescription = "";
            this.mMsgId = "";
            this.mDescriptionValue = "";
            this.mEnumValue = "";
            this.mTitle = "Widget info request Message";
            this.mDescription = "Message structure in JSON for Widget Info request";
            this.mMsgId = "widget-info-req";
        }

        public Object toJSON() throws JSONException {
            JSONObject json = new JSONObject();
            json.put("title", this.mTitle);
            json.put("description", this.mDescription);
            json.put("type", "object");
            JSONObject properties_json = new JSONObject();
            properties_json.put("msgId", this.mMsgId);
            json.put("properties", properties_json);
            return json;
        }

        public void fromJSON(Object obj) {
            try {
                JSONObject properties_json = ((JSONObject) obj).getJSONObject("properties");
                this.mMsgId = properties_json.getString("msgId");
                if ((this.mMsgId.equals("widget-info-rsp") || this.mMsgId.equals(IDS.WIDGET_INFO_UPDATE_MSGID)) && !properties_json.isNull("list")) {
                    JSONObject list_json = properties_json.getJSONObject("list");
                    if (!list_json.isNull("items")) {
                        JSONObject memory_json;
                        JSONObject item_json = list_json.getJSONObject("items");
                        if (!item_json.isNull("result")) {
                            JSONObject result_json = item_json.getJSONObject("result");
                            if (result_json != null) {
                                this.mEnumValue = result_json.getString("enum");
                                this.mDescriptionValue = result_json.getString("description");
                            }
                        }
                        if (!item_json.isNull(BATTERY)) {
                            JSONObject battery_json = item_json.getJSONObject(BATTERY);
                            if (battery_json != null) {
                                mBatteryValue = battery_json.getInt("description");
                                Gear360Settings.batteryInfo = mBatteryValue;
                            }
                        }
                        if (!item_json.isNull(BATTERY_STATE)) {
                            JSONObject batteryState_json = item_json.getJSONObject(BATTERY_STATE);
                            if (batteryState_json != null) {
                                mBatteryStateValue = batteryState_json.getString("description");
                                switch (mBatteryStateValue){
                                    case "charge":
                                        Gear360Settings.batteryState = "On";
                                        break;
                                    case "no-charge":
                                        Gear360Settings.batteryState = "Erase";
                                        break;
                                    case "normal":
                                        Gear360Settings.batteryState = "Off";
                                        break;
                                    case "error-cf":
                                        Gear360Settings.batteryState = "CF_Error";
                                        break;
                                    case "error-temp":
                                        Gear360Settings.batteryState = "TEMP_Error";
                                        break;
                                }
                            }
                        }
                        if (!item_json.isNull(TOTAL_MEMORY)) {
                            memory_json = item_json.getJSONObject(TOTAL_MEMORY);
                            if (memory_json != null) {
                                mTotalMemoryValue = memory_json.getInt("description");
                                Gear360Settings.totalMemoryInfo = mTotalMemoryValue;
                            }
                        }
                        if (!item_json.isNull(USED_MEMORY)) {
                            memory_json = item_json.getJSONObject(USED_MEMORY);
                            if (memory_json != null) {
                                mUsedMemoryValue = memory_json.getInt("description");
                                Gear360Settings.usedMemoryInfo = mUsedMemoryValue;
                            }
                        }
                        if (!item_json.isNull(FREE_MEMORY)) {
                            memory_json = item_json.getJSONObject(FREE_MEMORY);
                            if (memory_json != null) {
                                mFreeMemoryValue = memory_json.getInt("description");
                                Gear360Settings.freeMemoryInfo = mFreeMemoryValue;
                            }
                        }
                        if (!item_json.isNull(RECORD_STATE)) {
                            memory_json = item_json.getJSONObject(RECORD_STATE);
                            if (memory_json != null) {
                                mRecordState = memory_json.getString("description");
                                Gear360Settings.recordState = mRecordState;
                                Gear360Settings.recordNotPauseState = mRecordState;
                            }
                        }
                        if (!item_json.isNull(CAPTURE_STATE)) {
                            memory_json = item_json.getJSONObject(CAPTURE_STATE);
                            if (memory_json != null) {
                                mCaptureState = memory_json.getString("description");
                                Gear360Settings.captureState =mCaptureState;
                            }
                        }
                        if (!item_json.isNull(AUTO_POWEROFF)) {
                            memory_json = item_json.getJSONObject(AUTO_POWEROFF);
                            if (!(memory_json == null || Gear360Settings.confAutoPowerOff == null)) {
                                mAutoPowerOff = memory_json.getString("description");
                                Gear360Settings.confAutoPowerOff.setDefaultValue(mAutoPowerOff);
                            }
                        }
                        if (!item_json.isNull("recordable-time")) {
                            memory_json = item_json.getJSONObject("recordable-time");
                            if (memory_json != null) {
                                mRecordableTime = memory_json.getInt("description");
                                Gear360Settings.recordableTime = mRecordableTime;
                            }
                        }
                        if (!item_json.isNull("capturable-count")) {
                            memory_json = item_json.getJSONObject("capturable-count");
                            if (memory_json != null) {
                                mCapturableCount = memory_json.getInt("description");
                                Gear360Settings.capturableCount = mCapturableCount;
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public String getMsgId() {
            return this.mMsgId;
        }

        public String getEnum() {
            return this.mEnumValue;
        }

        public String getDescription() {
            return this.mDescriptionValue;
        }

        public int getAvailableMemory() {
            return mFreeMemoryValue;
        }

        public int getAvailableBattery() {
            return mBatteryValue;
        }
    }

    //originally named DeviceDescriptinUrlMsg
    public static final class DeviceDescriptionUrlMsg extends BTMessage {
        public static final String DESCRIPTION = "description";
        public static final String MSG_ID = "msgId";
        public static final String PROPERTIES = "properties";
        public static final String URL = "url";
        String mURL = "";

        public Object toJSON() throws JSONException {
            return null;
        }

        public void fromJSON(Object obj) throws JSONException {
            this.mURL = ((JSONObject) obj).getJSONObject("properties").getJSONObject("url").getString("description");
            Gear360Settings.deviceDescriptionURL = this.mURL;
        }
    }

    public static final class NotificationMsg extends BTMessage {
        public static final String DESCRIPTION = "description";
        public static final String MSG_ID = "msgId";
        public static final String NOTIFY = "notify";
        public static final String PROPERTIES = "properties";
        public static final String VALUE = "value";
        String mNotify = "";
        int recordValue = 0;

        public Object toJSON() throws JSONException {
            return null;
        }

        public void fromJSON(Object obj) throws JSONException {
            JSONObject noti_json = ((JSONObject) obj).getJSONObject("properties").getJSONObject("notify");
            this.mNotify = noti_json.getString("description");
            if (!noti_json.isNull("value")) {
                this.recordValue = noti_json.getInt("value");
            }
        }

        public String getNotification() {
            return this.mNotify;
        }

        public int getRecordValue() {
            return this.recordValue;
        }
    }
}
