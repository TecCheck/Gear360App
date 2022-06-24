package io.github.teccheck.gear360app.utils

import android.content.Context
import android.net.wifi.WifiManager

object WifiUtils {

    fun getMacAddress(context: Context): String {
        val man = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return man.connectionInfo.macAddress ?: return ""
    }

}