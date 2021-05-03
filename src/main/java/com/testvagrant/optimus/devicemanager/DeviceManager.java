package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.cache.DeviceCache;
import com.testvagrant.optimus.core.models.TargetDetails;

import java.util.function.Predicate;

public abstract class DeviceManager {
  DeviceCache deviceCache;

  public DeviceManager(DeviceCache deviceCache) {
    this.deviceCache = deviceCache;
  }

  public synchronized void releaseDevice(TargetDetails targetDetails) {
    deviceCache.release(targetDetails.getUdid());
  }

  public long getTotalAvailableDevices() {
    return deviceCache.size();
  }

  public synchronized TargetDetails getAvailableDevice(
      Predicate<TargetDetails> deviceFilterPredicate) {
    return deviceCache.get(deviceFilterPredicate);
  }

  public TargetDetails getAvailableDevice() {
    return getAvailableDevice(platformQueryPredicate());
  }

  protected Predicate<TargetDetails> platformQueryPredicate() {
    return deviceDetails -> !deviceDetails.getPlatform().name().isEmpty();
  }
}
