package io.github.teccheck.gear360app.bluetooth2

import org.json.JSONObject

object MessageKeys {
    const val TITLE = "title"
    const val DESCRIPTION = "description"
    const val TYPE = "type"
    const val MSGID = "msgId"
    const val PROPERTIES = "properties"
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

    private fun getJsonProperty(value: String): JSONObject {
        return JSONObject()
            .put(MessageKeys.TYPE, "string")
            .put(MessageKeys.DESCRIPTION, value)
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