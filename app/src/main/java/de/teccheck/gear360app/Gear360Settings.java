package de.teccheck.gear360app;

import java.util.ArrayList;
import java.util.Arrays;

public class Gear360Settings {

    public static final String TAG = "G360_" + Gear360Settings.class.getSimpleName();

    public static boolean isRecording = false;

    //Wifi
    public static String channel = "";
    public static String deviceDescriptionURL = "";
    public static String securityType = "";
    public static String softApPsword = "";
    public static String softApSSID = "";
    public static String wifiDirectSSID = "";
    public static String fwDownloadURL = "";

    //Widget
    public static int batteryInfo = 0;
    public static String batteryState = "";
    public static String boardRevison = "";
    public static String cameraWifiDirectMac = "";
    public static String cameraWifiMac = "";
    public static int capturableCount = 0;
    public static String captureState = "";
    public static int freeMemoryInfo = 0;
    public static String recordNotPauseState = "";
    public static String recordState = "";
    public static int recordableTime = 0;
    public static int recordedTime = 0;
    public static int totalMemoryInfo = 0;
    public static int totalRecordTime = 0;
    public static int usedMemoryInfo = 0;
    
    //Model Info
    public static final String MODEL_NAME_GEAR_360 = "SM-C200";
    public static final String MODEL_NAME_GLOBE = "SM-R210";
    public static final String MODEL_NAME_USER = "Gear 360";
    public static String fwType = "";
    public static String modelName = "";
    public static String modelVersion = "";
    public static String serialNum = "";
    public static String uniqueNum = "";

    //Config
    public static final String MODE_PHOTO = "Photo";
    public static final String MODE_VIDEO = "Video";
    public static final String MODE_TIME_LAPSE = "Time Lapse";
    public static final String MODE_LOOPING_VIDEO = "Looping Video";

    public static Config confAutoPowerOff = new Config();
    public static Config confBeep = new Config();
    public static Config confFormat = new Config();
    public static Config confISO = new Config();
    public static Config confIsoLimit = new Config();
    public static Config confLedIndicator = new Config();
    public static Config confLensMode = new Config();
    public static Config confLoopingVideoTime = new Config();
    public static Config confMode = new Config();
    public static Config confMovieSize = new Config();
    public static Config confMovieSizeDual = new Config();
    public static Config confMovieSizeSingle = new Config();
    public static Config confReset = new Config();
    public static Config confSharpness = new Config();
    public static Config confTimer = new Config();
    public static Config confVideoOut = new Config();
    public static Config confWhiteBalance = new Config();
    public static Config confWindCut = new Config();

    public static class Config {
        public String defaultValue = "";
        public String name = "";
        public ArrayList<String> values = new ArrayList<>();

        public void setConf(String name, String[] values, String defaultValue){
            setConf(name, getArrayList(values), defaultValue);
        }

        public void setConf(String name, ArrayList<String> values, String defaultValue){
            this.name = name;
            this.values = values;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<String> getValues() {
            return values;
        }

        public void setValues(ArrayList<String> values) {
            this.values = values;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getAsString(){
            String out = "";
            if(values.isEmpty()){
                out += "isEmpty";
            }else {
                for (String s :values) {
                    out += " " + s +",";
                }

                out = out.substring(0 ,out.length() - 1);
            }
            return out;
        }
    }

    static ArrayList getArrayList(String[] values){
        return new ArrayList(Arrays.asList(values));
    }

    public static boolean isOldGear360() {
        return modelName.equals("SM-C200");
    }

    public static boolean isNewGear360() {
        return modelName.equals("SM-R210");
    }

    public static boolean isMode(String mode){
        return confMode.getDefaultValue().equalsIgnoreCase(mode);
    }

    public static boolean isPhotoMode(){
        return isMode(MODE_PHOTO);
    }

    public static boolean isVideoMode(){
        return isMode(MODE_VIDEO);
    }

    public static boolean isTimeLapseMode(){
        return isMode(MODE_TIME_LAPSE);
    }

    public static boolean isLoopVideoMode(){
        return  isMode(MODE_LOOPING_VIDEO);
    }
}