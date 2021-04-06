package com.testvagrant.optimusLite.cache;

import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.commons.entities.device.Status;
import com.testvagrant.optimusLite.mdb.android.Android;

import java.util.List;

public class AndroidDeviceProvider extends DeviceProvider {
  private List<DeviceDetails> devices;

  public AndroidDeviceProvider() {
    createDeviceCache();
  }

  @Override
  protected void createDeviceCache() {
    devices = new Android().getDevices();
    deviceCache = getDeviceCache(devices);
    deviceCache.put(Status.Available, devices);
  }
}
