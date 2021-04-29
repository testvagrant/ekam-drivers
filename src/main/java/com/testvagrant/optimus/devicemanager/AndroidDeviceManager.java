package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.cache.DeviceCache;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;

public class AndroidDeviceManager extends DeviceManager {

  public AndroidDeviceManager() {
    super(new DeviceCache(OptimusSupportedPlatforms.ANDROID));
  }
}
