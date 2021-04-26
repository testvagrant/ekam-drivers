package com.testvagrant.optimus.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.commons.exceptions.DeviceEngagedException;
import com.testvagrant.optimus.commons.exceptions.DeviceReleaseException;
import com.testvagrant.optimus.commons.exceptions.NoSuchDeviceException;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.mdb.android.Android;
import com.testvagrant.optimus.mdb.ios.IOS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class DeviceCache extends DataCache<String, DeviceDetails> {
  List<DeviceDetails> deviceDetailsList;
  LoadingCache<String, DeviceDetails> masterDeviceCache, availableDevicesCache, engagedDevicesCache;

  public DeviceCache(OptimusSupportedPlatforms platform) {
    deviceDetailsList = getDeviceList(platform);
    masterDeviceCache = build(new DeviceCacheLoader());
    availableDevicesCache = build(new DeviceCacheLoader());
    engagedDevicesCache = build(new DeviceCacheLoader());
    loadDeviceDetails();
  }

  @Override
  protected synchronized void lock(String udid) {
    try {
      DeviceDetails deviceDetails = availableDevicesCache.get(udid);
      engagedDevicesCache.put(deviceDetails.getUdid(), deviceDetails);
      availableDevicesCache.invalidate(udid);
    } catch (Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  @Override
  public void release(String udid) {
    try {
      DeviceDetails deviceDetails = engagedDevicesCache.get(udid);
      availableDevicesCache.put(deviceDetails.getUdid(), deviceDetails);
      engagedDevicesCache.invalidate(udid);
    } catch (Exception ex) {
      throw new DeviceReleaseException(udid, ex.getMessage());
    }
  }

  public boolean isAvailable(Predicate<DeviceDetails> predicate) {
    return anyMatch(availableDevicesCache, predicate);
  }

  public boolean isEngaged(Predicate<DeviceDetails> predicate) {
    return anyMatch(engagedDevicesCache, predicate);
  }

  @Override
  public void put(String key, DeviceDetails deviceDetails) {
    masterDeviceCache.put(key, deviceDetails);
    availableDevicesCache.put(key, deviceDetails);
  }

  @Override
  public boolean isPresent(Predicate<DeviceDetails> predicate) {
    return anyMatch(masterDeviceCache, predicate);
  }

  @Override
  public synchronized DeviceDetails get(Predicate<DeviceDetails> predicate) {
    if (!isPresent(predicate)) {
      throw new NoSuchDeviceException();
    }

    if (isAvailable(predicate)) {
      Optional<DeviceDetails> availableDevice =
          availableDevicesCache.asMap().values().stream().filter(predicate).findAny();

      if (availableDevice.isPresent()) {
        DeviceDetails deviceDetails = availableDevice.get();
        lock(deviceDetails.getUdid());
        return deviceDetails;
      }
    }

    throw new DeviceEngagedException();
  }

  @Override
  public long size() {
    return availableDevicesCache.size();
  }

  private List<DeviceDetails> getDeviceList(OptimusSupportedPlatforms platform) {
    List<DeviceDetails> deviceDetails = new ArrayList<>();
    switch (platform) {
      case ANDROID:
        deviceDetails.addAll(new Android().getDevices());
        break;
      case IOS:
        deviceDetails.addAll(new IOS().getDevices());
        break;
    }
    return deviceDetails;
  }

  private void loadDeviceDetails() {
    deviceDetailsList.parallelStream()
        .forEach(deviceDetails -> put(deviceDetails.getUdid(), deviceDetails));
  }

  private class DeviceCacheLoader extends CacheLoader<String, DeviceDetails> {

    @Override
    public DeviceDetails load(String udid) {
      return getDeviceDetails(deviceDetailsList, udid);
    }

    private synchronized DeviceDetails getDeviceDetails(
        List<DeviceDetails> deviceDetailsList, String udid) {
      return deviceDetailsList.stream()
          .filter(deviceDetails -> deviceDetails.getUdid().equals(udid))
          .findAny()
          .orElseThrow(() -> new RuntimeException("No Devices Available"));
    }
  }
}
