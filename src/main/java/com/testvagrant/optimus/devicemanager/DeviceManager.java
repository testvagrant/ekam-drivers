package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.cache.DeviceCache;
import com.testvagrant.optimus.commons.entities.DeviceDetails;

import java.util.function.Predicate;

public abstract class DeviceManager {
  DeviceCache deviceCache;

  public DeviceManager(DeviceCache deviceCache) {
    this.deviceCache = deviceCache;
  }

  public synchronized void releaseDevice(DeviceDetails deviceDetails) {
    deviceCache.release(deviceDetails.getUdid());
  }

  public long getTotalAvailableDevices() {
    return deviceCache.size();
  }

  public synchronized DeviceDetails getAvailableDevice(
      Predicate<DeviceDetails> deviceFilterPredicate) {
    return deviceCache.get(deviceFilterPredicate);
  }

  public DeviceDetails getAvailableDevice() {
    return getAvailableDevice(platformQueryPredicate());
  }

  protected Predicate<DeviceDetails> platformQueryPredicate() {
    return deviceDetails -> !deviceDetails.getPlatform().name().isEmpty();
  }
}
