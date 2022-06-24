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
