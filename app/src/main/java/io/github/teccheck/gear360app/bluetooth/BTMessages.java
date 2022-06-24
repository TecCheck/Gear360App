package io.github.teccheck.gear360app.bluetooth;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BTMessages {

    public static final String TAG = "G360_" + BTMessages.class.getSimpleName();

    //Some Strings used for Bluetooth communication
    public static class IDS {

        public static final String DATE_TIME_REQUEST_DESCRIPTION = "Message structure in JSON for Date-Time request";
        public static final String DATE_TIME_REQUEST_MSGID = "date-time-req";
        public static final String DATE_TIME_REQUEST_TITLE = "Date-Time request Message";
        public static final String DATE_TIME_RESOPNSE_DESCRIPTION = "Message structure in JSON for Date-Time response";
        public static final String DATE_TIME_RESOPNSE_TITLE = "Date-Time response Message";
        public static final String DATE_TIME_RESPONSE_MSGID = "date-time-rsp";

        public static final String DEVICE_INFO_WIFI_DIRECT_ENUM_FALSE = "false";

        public static final String REMOTE_SHOT_REQUEST_DESCRIPTION = "Message structure in JSON for remote shot request";
        public static final String REMOTE_SHOT_REQUEST_MSGID = "shot-req";
        public static final String REMOTE_SHOT_REQUEST_TITLE = "Remote shot request Message";

        public static final String REMOTE_SHOT_RESPONSE_DESCRIPTION = "Message structure in JSON for remote shot response";
        public static final String REMOTE_SHOT_RESPONSE_MSGID = "shot-rst";
        public static final String REMOTE_SHOT_RESPONSE_TITLE = "Remote shot response Message";
    }

    public interface JsonSerializable {
        void fromJSON(Object obj) throws JSONException;

        Object toJSON() throws JSONException;
    }

    public static class BTMessage implements JsonSerializable {

        @Override
        public void fromJSON(Object obj) throws JSONException {

        }

        @Override
        public Object toJSON() throws JSONException {
            return null;
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

        public JSONObject toJSON() throws JSONException {
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
}
