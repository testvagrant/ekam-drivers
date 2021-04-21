package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.parser.TestFeedParser;

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
    TestFeedParser parser = new TestFeedParser(System.getProperty("testFeed"));
    return parser.getPlatform() == OptimusSupportedPlatforms.IOS
        ? new IosDeviceManager()
        : new AndroidDeviceManager();
  }
}
