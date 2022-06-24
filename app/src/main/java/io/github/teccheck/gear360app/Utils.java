package io.github.teccheck.gear360app;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Locale;

public class Utils {

    public static final String TAG = "G360_" + Utils.class.getSimpleName();

    public static String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (intf.getName().equalsIgnoreCase(interfaceName)) {
                    byte[] mac = intf.getHardwareAddress();
                    if (mac == null) {
                        return "";
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte aMac : mac) {
                        buf.append(String.format(Locale.getDefault(), "%02X:", aMac));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
