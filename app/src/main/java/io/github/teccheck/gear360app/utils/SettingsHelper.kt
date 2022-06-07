package io.github.teccheck.gear360app.utils

import android.content.Context
import android.content.SharedPreferences

private const val key_device_address = "last_connected_device"

class SettingsHelper constructor(context: Context) {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("prefs", 0)
    }

    fun edit(): Editor {
        return Editor(sharedPreferences.edit())
    }

    fun getLastConnectedDeviceAddress(): String? {
        return sharedPreferences.getString(key_device_address, null)
    }

    inner class Editor constructor(private val editor: SharedPreferences.Editor) {

        fun setLastConnectedDeviceAddress(address: String) {
            editor.putString(key_device_address, address)
        }

        fun save() {
            editor.apply()
        }
    }

}