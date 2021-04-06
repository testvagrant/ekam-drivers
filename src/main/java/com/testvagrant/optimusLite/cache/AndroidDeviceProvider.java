package com.testvagrant.optimusLite.cache;

import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.commons.entities.device.Status;
import com.testvagrant.optimusLite.mdb.android.Android;

import java.util.List;

public class AndroidDeviceProvider extends DeviceProvider {

  public AndroidDeviceProvider() {
    List<DeviceDetails> devices = new Android().getDevices();
    addDeviceDetailsToCache(devices);
  }

}
