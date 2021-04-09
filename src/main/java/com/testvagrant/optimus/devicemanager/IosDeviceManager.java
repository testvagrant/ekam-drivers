package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.cache.DeviceCache;
import org.openqa.selenium.Platform;

public class IosDeviceManager extends DeviceManager {
  public IosDeviceManager() {
    super(new DeviceCache(Platform.IOS));
  }
}
