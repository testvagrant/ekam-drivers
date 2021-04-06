package com.testvagrant.optimusLite.cache;

import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.mdb.ios.IOS;

import java.util.List;

public class IosDeviceProvider extends DeviceProvider {
    public IosDeviceProvider() {
        List<DeviceDetails> devices = new IOS().getDevices();
        addDeviceDetailsToCache(devices);
    }
}
