package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.cache.DeviceCache;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import org.openqa.selenium.Platform;

public class AndroidDeviceManager extends DeviceManager {

  public AndroidDeviceManager() {
    super(new DeviceCache(OptimusSupportedPlatforms.ANDROID));
  }
}
