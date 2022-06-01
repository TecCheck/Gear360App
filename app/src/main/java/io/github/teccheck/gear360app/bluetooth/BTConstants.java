package io.github.teccheck.gear360app.bluetooth;

import java.util.UUID;

public class BTConstants {

    public static final String TAG_PREFIX = "G360_";

    public static final UUID GEAR360_UUID = UUID.fromString("a49eb41e-cb06-495c-9f4f-791130a90ccf");
    public static final String TYPE_GEAR_360 = "Gear 360";
    public static boolean hasLastConnected = true;

    public static String lastConnectedBTAddress() {
        return "EC:10:7B:72:E9:B3";
    }

}
