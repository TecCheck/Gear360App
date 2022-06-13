package io.github.teccheck.gear360app.bluetooth2

import io.github.teccheck.gear360app.bluetooth.BTMessages.BTConfigInfoMsg
import org.json.JSONObject

private const val KEY_TITLE = "title"
private const val KEY_DESCRIPTION = "description"
private const val KEY_TYPE = "type"
const val KEY_MSGID = "msgId"
const val KEY_PROPERTIES = "properties"

private const val DEVICE_INFO_MSGID = "info"
private const val DEVICE_INFO_TYPE = "object"
private const val DEVICE_INFO_REQUEST_DESCRIPTION =
    "Message structure in JSON for Phone Device information"
private const val DEVICE_INFO_REQUEST_TITLE = "Phone Device information Message"

const val CONFIG_INFO_MSGID = "config-info"

abstract class BTMessage(
    private val title: String,
    private val description: String,
    private val type: String
) {
    open fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(KEY_TITLE, title)
        jsonObject.put(KEY_DESCRIPTION, description)
        jsonObject.put(KEY_TYPE, type)
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
    DEVICE_INFO_REQUEST_TITLE,
    DEVICE_INFO_REQUEST_DESCRIPTION,
    DEVICE_INFO_TYPE
) {

    override fun toJson(): JSONObject {
        val jsonObject = super.toJson()

        val properties = JSONObject()
        properties.put(KEY_MSGID, DEVICE_INFO_MSGID)
        properties.put(
            "wifi-direct",
            JSONObject()
                .put("enum", wifiDirect.toString())
                .put(
                    "ch-negotiation-wa",
                    JSONObject().put(KEY_DESCRIPTION, "5G-GO")
                )
        )
        properties.put("wifi-mac-address", getJsonProperty(wifiMacAddress))
        properties.put("device-name", getJsonProperty(deviceName))
        properties.put("app-version", getJsonProperty(appVersion))

        val opMode = if (retailMode) "retail" else "user"
        properties.put("op-mode", getJsonProperty(opMode))

        jsonObject.put(KEY_PROPERTIES, properties)
        return jsonObject
    }

    private fun getJsonProperty(value: String): JSONObject {
        return JSONObject()
            .put(KEY_TYPE, "string")
            .put(KEY_DESCRIPTION, value)
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
            val title = jsonObject.getString(KEY_TITLE)
            val description = jsonObject.getString(KEY_DESCRIPTION)
            val type = jsonObject.getString(KEY_TYPE)

            val properties = jsonObject.getJSONObject(KEY_PROPERTIES)
            val msgId = properties.getString(KEY_MSGID)

            val functions = properties.getJSONObject("functions")
            val count: Int = functions.getInt("count")
            val items: JSONObject = functions.getJSONObject("items")

            val configs = mutableListOf<Gear360Configs.Config>()

            for (i in 1..count) {
                val config: JSONObject = items.getJSONObject(i.toString())
                val name = config.getString(BTConfigInfoMsg.SUBTITLE)
                val defaultValue = config.getString("default")
                val values = config.getString("value").split(",".toRegex()).toTypedArray()

                configs.add(Gear360Configs.Config(name, defaultValue, values))
            }

            return BTConfigMsg(title, description, type, configs)
        }
    }
}