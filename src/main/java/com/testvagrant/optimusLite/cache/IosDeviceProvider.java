package com.testvagrant.optimusLite.cache;

import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.commons.entities.device.Status;
import com.testvagrant.optimusLite.mdb.ios.IOS;

import java.util.List;

public class IosDeviceProvider extends DeviceProvider {

  private List<DeviceDetails> devices;

  public IosDeviceProvider() {
    createDeviceCache();
  }

  @Override
  protected void createDeviceCache() {
    devices = new IOS().getDevices();
    deviceCache = getDeviceCache(devices);
    deviceCache.put(Status.Available, devices);
  }
}
