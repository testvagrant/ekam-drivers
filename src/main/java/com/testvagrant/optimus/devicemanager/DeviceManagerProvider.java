package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.commons.SystemProperties;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import org.openqa.selenium.Platform;

public class DeviceManagerProvider {

  private static DeviceManager deviceManagerProvider;

  private DeviceManagerProvider() {}

  public static DeviceManager getInstance() {
    if (deviceManagerProvider == null) {
      synchronized (DeviceManagerProvider.class) {
        if (deviceManagerProvider == null) {
          deviceManagerProvider = new DeviceManagerProvider().get();
        }
      }
    }
    return deviceManagerProvider;
  }

  public DeviceManager get() {
    if (new TestFeedParser(SystemProperties.TEST_FEED).getPlatform() == Platform.IOS) {
      return new IosDeviceManager();
    }
    return new AndroidDeviceManager();
  }
}
