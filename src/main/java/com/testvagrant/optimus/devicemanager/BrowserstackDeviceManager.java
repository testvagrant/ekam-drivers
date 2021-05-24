package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.core.exceptions.NoSuchDeviceException;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.remote.CloudConfigBuilder;
import com.testvagrant.optimus.core.remote.browserstack.BrowserStackDeviceClient;

import java.util.List;
import java.util.function.Predicate;

public class BrowserstackDeviceManager {

  private final List<TargetDetails> devices;
  private static BrowserstackDeviceManager deviceManager;
  private OptimusSupportedPlatforms platform;

  private BrowserstackDeviceManager(OptimusSupportedPlatforms platform) {
    BrowserStackDeviceClient browserStackClient =
        new BrowserStackDeviceClient(new CloudConfigBuilder().build());

    devices =
        platform.equals(OptimusSupportedPlatforms.IOS)
            ? browserStackClient.getIosDevices()
            : browserStackClient.getAndroidDevices();
  }

  public static BrowserstackDeviceManager getInstance(OptimusSupportedPlatforms platform) {
    return new BrowserstackDeviceManager(platform);
  }

  public synchronized TargetDetails getDevice(Predicate<TargetDetails> deviceFilterPredicate) {
    return devices.stream()
        .filter(deviceFilterPredicate)
        .findFirst()
        .orElseThrow(NoSuchDeviceException::new);
  }

  public TargetDetails getDevice() {
    return getDevice(platformQueryPredicate());
  }

  protected Predicate<TargetDetails> platformQueryPredicate() {
    return TargetDetails -> !TargetDetails.getName().isEmpty();
  }
}
