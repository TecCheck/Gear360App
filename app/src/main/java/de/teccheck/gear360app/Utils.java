package de.teccheck.gear360app;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.StringReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static final String TAG = "G360_" + Utils.class.getSimpleName();

    public static String getPrettyJsonStringFromByteData(String json) {
        JsonSyntaxException exception;
        Gson mGsonParser = new GsonBuilder().setLenient().setPrettyPrinting().create();
        JsonParser mJsonParser = new JsonParser();

        JsonReader jsonReader = new JsonReader(new StringReader(json));
        jsonReader.setLenient(true);
        try {
            try {
                return mGsonParser.toJson(mJsonParser.parse(json));
            } catch (JsonSyntaxException e) {
                try {
                    return mGsonParser.toJson(mJsonParser.parse(String.valueOf(jsonReader)));
                } catch (JsonSyntaxException exception2) {
                    return json;
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return mGsonParser.toJson(mJsonParser.parse(String.valueOf(jsonReader)));
        }
    }

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

    public static void connectWifi(Context context){
        connectWifi("AP_Gear 360(7D:66:D2)", "79606006", false, context);
    }

    public static void connectWifi(String ssid, String passwd, boolean open, Context context){

        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration configuration = null;

        //look for wifi network
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                configuration = i;
                break;
            }
        }

        //if not found create a new one and add it
        if(configuration == null){
            configuration = new WifiConfiguration();
            configuration.SSID = "\"" + ssid + "\"";
            if(open){
                //without password
                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }else {
                // with wpa password
                configuration.preSharedKey = "\""+ passwd +"\"";
            }
            wifiManager.addNetwork(configuration);
        }

        // connect to the network
        wifiManager.disconnect();
        wifiManager.enableNetwork(configuration.networkId, true);
        wifiManager.reconnect();
    }

}
