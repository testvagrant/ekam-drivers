package com.testvagrant.optimusLite.mdb;

import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.mdb.android.Android;
import com.testvagrant.optimusLite.mdb.ios.IOS;

import java.util.List;

public class MDBClient {

    //OnStart => Only If Target Is Local
    private static List<DeviceDetails> testDevices;
    static {
        testDevices = new Android().getDevices();
        testDevices.addAll(new IOS().getDevices());
    }
    private MDBClient() {}

    public static List<DeviceDetails> loadDevices() {
        return testDevices;
    }
}
