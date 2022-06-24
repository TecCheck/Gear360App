package io.github.teccheck.gear360app.bluetooth2

import android.annotation.SuppressLint
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object MessageKeys {
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val TYPE = "type"
    const val MSGID = "msgId"
    const val PROPERTIES = "properties"
    const val OBJECT = "object"
}

object MessageIds {
    const val DEVICE_INFO = "info"
    const val CONFIG_INFO = "config-info"
    const val WIDGET_INFO_REQ = "widget-info-req"
    const val WIDGET_INFO_RSP = "widget-info-rsp"
    const val DATE_TIME_REQ = "date-time-req"
    const val DATE_TIME_RSP = "date-time-rsp"
    const val COMMAND_REQ = "cmd-req"
    const val COMMAND_RSP = "cmd-rsp"
    const val SHOT_REQ = "shot-req"
    const val SHOT_RSP = "shot-rst"
    const val DEVICE_DESC_URL = "device-desc-url"
    const val BIGDATA_REQ = "bigdata-req"
}

abstract class BTMessage(
    private val title: String,
    private val description: String,
    private val type: String
) {
    open fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(MessageKeys.TITLE, title)
        jsonObject.put(MessageKeys.DESCRIPTION, description)
        jsonObject.put(MessageKeys.TYPE, type)
        return jsonObject
    }

    companion object {
        fun getJsonProperty(value: String): JSONObject {
            return JSONObject()
                .put(MessageKeys.TYPE, "string")
                .put(MessageKeys.DESCRIPTION, value)
        }

        fun getJsonProperty(value: Any, type: String): JSONObject {
            return JSONObject()
                .put(MessageKeys.TYPE, type)
                .put(MessageKeys.DESCRIPTION, value)
        }
    }
}

class BTInfoMsg(
    private val wifiDirect: Boolean,
    private val wifiMacAddress: String,
    private val deviceName: String,
    private val appVersion: String,
    private val retailMode: Boolean
) : BTMessage(
    "Phone Device information Message",
    "Message structure in JSON for Phone Device information",
    "object"
) {
    override fun toJson(): JSONObject {
        val jsonObject = super.toJson()

        val properties = JSONObject()
        properties.put(MessageKeys.MSGID, MessageIds.DEVICE_INFO)
        properties.put(
            "wifi-direct",
            JSONObject()
                .put("enum", wifiDirect.toString())
                .put(
                    "ch-negotiation-wa",
                    JSONObject().put(MessageKeys.DESCRIPTION, "5G-GO")
                )
        )
        properties.put("wifi-mac-address", getJsonProperty(wifiMacAddress))
        properties.put("device-name", getJsonProperty(deviceName))
        properties.put("app-version", getJsonProperty(appVersion))

        val opMode = if (retailMode) "retail" else "user"
        properties.put("op-mode", getJsonProperty(opMode))

        jsonObject.put(MessageKeys.PROPERTIES, properties)
        return jsonObject
    }
}

class BTConfigMsg(
    title: String,
    description: String,
    type: String,
    val configs: List<Gear360Configs.Config>
) : BTMessage(title, description, type) {

    companion object {
        fun fromJson(jsonObject: JSONObject): BTConfigMsg {
            val title = jsonObject.getString(MessageKeys.TITLE)
            val description = jsonObject.getString(MessageKeys.DESCRIPTION)
            val type = jsonObject.getString(MessageKeys.TYPE)

            val properties = jsonObject.getJSONObject(MessageKeys.PROPERTIES)

            val functions = properties.getJSONObject("functions")
            val count: Int = functions.getInt("count")
            val items: JSONObject = functions.getJSONObject("items")

            val configs = mutableListOf<Gear360Configs.Config>()

            for (i in 1..count) {
                val config: JSONObject = items.getJSONObject(i.toString())
                val name = config.getString("sub-title")
                val defaultValue = config.getString("default")
                val values = config.getString("value").split(",".toRegex()).toTypedArray()

                configs.add(Gear360Configs.Config(name, defaultValue, values))
            }

            return BTConfigMsg(title, description, type, configs)
        }
    }
}

class BTInfoRsp(
    title: String,
    description: String,
    type: String,
    val modelName: String,
    val modelVersion: String,
    val channel: Int,
    val wifiDirectMac: String,
    val apSSID: String,
    val apPassword: String,
    val boardRevision: String,
    val serialNumber: String,
    val uniqueNumber: String,
    val wifiMac: String,
    val btMac: String,
    val btFotaTestUrl: String,
    val fwType: Int
) : BTMessage(title, description, type) {
    companion object {
        fun fromJson(jsonObject: JSONObject): BTInfoRsp {
            val title = jsonObject.getString(MessageKeys.TITLE)
            val description = jsonObject.getString(MessageKeys.DESCRIPTION)
            val type = jsonObject.getString(MessageKeys.TYPE)

            val properties = jsonObject.getJSONObject(MessageKeys.PROPERTIES)

            return BTInfoRsp(
                title,
                description,
                type,
                properties.getJSONObject("model-name").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("model-version").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("channel").getInt(MessageKeys.DESCRIPTION),
                properties.getJSONObject("wifi-direct-mac").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("softap-ssid").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("softap-psword").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("board-revision").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("serial-number").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("unique-number").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("wifi-mac").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("bt-mac").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("bt-fota-test-url").getString(MessageKeys.DESCRIPTION),
                properties.getJSONObject("fw-type").getInt(MessageKeys.DESCRIPTION),
            )
        }
    }
}

class BTWidgetReq : BTMessage(
    "Widget info request Message",
    "Message structure in JSON for Widget Info request",
    "object"
) {
    override fun toJson(): JSONObject {
        val jsonObject = super.toJson()

        val properties = JSONObject().put(MessageKeys.MSGID, MessageIds.WIDGET_INFO_REQ)
        jsonObject.put(MessageKeys.PROPERTIES, properties)

        return jsonObject
    }

    companion object {
        fun fromJson(jsonObject: JSONObject): BTWidgetReq {
            return BTWidgetReq()
        }
    }
}

class BTWidgetRsp(
    title: String,
    description: String,
    type: String,
    val gear360Status: Gear360Status
) : BTMessage(title, description, type) {

    companion object {
        fun fromJson(jsonObject: JSONObject): BTWidgetRsp {
            val title = jsonObject.getString(MessageKeys.TITLE)
            val description = jsonObject.getString(MessageKeys.DESCRIPTION)
            val type = jsonObject.getString(MessageKeys.TYPE)

            val properties = jsonObject.getJSONObject(MessageKeys.PROPERTIES)
            val items = properties.getJSONObject("list").getJSONObject("items")

            val battery = items.getJSONObject("battery").getInt(MessageKeys.DESCRIPTION)
            val batteryState =
                items.getJSONObject("battery-state").getString(MessageKeys.DESCRIPTION)
            val totalMemory = items.getJSONObject("total-memory").getInt(MessageKeys.DESCRIPTION)
            val usedMemory = items.getJSONObject("used-memory").getInt(MessageKeys.DESCRIPTION)
            val freeMemory = items.getJSONObject("free-memory").getInt(MessageKeys.DESCRIPTION)
            val recordState = items.getJSONObject("record-state").getString(MessageKeys.DESCRIPTION)
            val captureState =
                items.getJSONObject("capture-state").getString(MessageKeys.DESCRIPTION)
            val autoPowerOff =
                items.getJSONObject("auto-poweroff").getString(MessageKeys.DESCRIPTION)
            val recordableTime =
                items.getJSONObject("recordable-time").getInt(MessageKeys.DESCRIPTION)
            val capturableCount =
                items.getJSONObject("capturable-count").getInt(MessageKeys.DESCRIPTION)

            val status = Gear360Status(
                battery,
                batteryState,
                totalMemory,
                usedMemory,
                freeMemory,
                recordState,
                captureState,
                autoPowerOff,
                recordableTime,
                capturableCount
            )

            return BTWidgetRsp(title, description, type, status)
        }
    }
}

class BTCommandReq(private val action: Action) : BTMessage(
    "Command request Message",
    "Message structure in JSON for Command request",
    MessageKeys.OBJECT
) {
    override fun toJson(): JSONObject {
        val jsonObject = super.toJson()

        val properties = JSONObject()
        properties.put(MessageKeys.MSGID, MessageIds.COMMAND_REQ)
        properties.put("action", action.toJson())

        jsonObject.put(MessageKeys.PROPERTIES, properties)

        return jsonObject
    }

    open class Action(private val enum: String, private val description: String) {
        open fun toJson(): JSONObject {
            val jsonObject = JSONObject()
            jsonObject.put("enum", enum)
            jsonObject.put(MessageKeys.DESCRIPTION, description)
            return jsonObject
        }
    }

    class ConfigAction(
        enum: String,
        description: String,
        private val configName: String,
        private val configValue: String
    ) :
        Action(enum, description) {

        constructor(configName: String, configValue: String) : this(
            "execute",
            "config",
            configName,
            configValue
        )

        override fun toJson(): JSONObject {
            val jsonObject = super.toJson()

            val config = JSONObject()
            config.put(MessageKeys.DESCRIPTION, configValue)

            val items = JSONObject()
            items.put(configName, config)

            jsonObject.put("items", items)
            return jsonObject
        }
    }
}

class BTDateTimeReq : BTMessage(
    "Date-Time request Message",
    "Message structure in JSON for Date-Time request",
    "object"
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): BTDateTimeReq {
            return BTDateTimeReq()
        }
    }
}

class BTDateTimeRsp : BTMessage(
    "Date-Time response Message",
    "Message structure in JSON for Date-Time response",
    "object"
) {
    @SuppressLint("SimpleDateFormat")
    override fun toJson(): JSONObject {
        val jsonObject = super.toJson()

        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyy/MM/dd")
        val timeFormat = SimpleDateFormat("HH:mm:ss")
        val zoneFormat = SimpleDateFormat("Z")

        val zone = zoneFormat.format(date)
        val region = zone.substring(0, 3) + ":" + zone.substring(3, 5)

        val isSummerTimer = TimeZone.getDefault().useDaylightTime()

        val properties = JSONObject()
            .put(MessageKeys.MSGID, MessageIds.DATE_TIME_REQ)
            .put("date", getJsonProperty(dateFormat.format(date)))
            .put("time", getJsonProperty(timeFormat.format(date)))
            .put("region", getJsonProperty(region))
            .put("summer", getJsonProperty(isSummerTimer.toString()))

        jsonObject.put(MessageKeys.PROPERTIES, properties)

        return jsonObject
    }
}

class BTShotReq(private val mode: String) : BTMessage(
    "Remote shot request Message",
    "Message structure in JSON for remote shot request",
    "object"
) {
    override fun toJson(): JSONObject {
        val jsonObject = super.toJson()

        val items = JSONObject()
            .put(MessageKeys.TYPE, "string")
            .put(MessageKeys.DESCRIPTION, mode)

        val properties = JSONObject()
            .put(MessageKeys.MSGID, MessageIds.SHOT_REQ)
            .put("items", items);

        jsonObject.put(MessageKeys.PROPERTIES, properties)

        return jsonObject
    }
}

class BTShotRsp(
    val resultEnum: String,
    val resultType: String,
    val resultCode: Int,
    val recordableTime: Int,
    val capturableCount: Int
) : BTMessage(
    "Remote shot response Message",
    "Message structure in JSON for remote shot response",
    "object"
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): BTShotRsp {
            val properties = jsonObject.getJSONObject(MessageKeys.PROPERTIES)

            val result = properties.getJSONObject("result")
            val resultEnum = result.getString("enum")
            val resultType = result.getString(MessageKeys.DESCRIPTION)

            val rCode = properties.getJSONObject("r-code")
            val resultCode = rCode.getInt("description")

            val info = properties.getJSONObject("extension-info")
            val recordableTime =
                info.getJSONObject("recordable-time").getInt(MessageKeys.DESCRIPTION)

            val capturableCount =
                info.getJSONObject("capturable-count").getInt(MessageKeys.DESCRIPTION)

            return BTShotRsp(resultEnum, resultType, resultCode, recordableTime, capturableCount)
        }
    }
}