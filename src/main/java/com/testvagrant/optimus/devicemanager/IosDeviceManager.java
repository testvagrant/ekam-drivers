package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.cache.DeviceCache;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;

public class IosDeviceManager extends DeviceManager {
  public IosDeviceManager() {
    super(new DeviceCache(OptimusSupportedPlatforms.IOS));
  }
}
