package com.testvagrant.optimus.cache;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.testvagrant.optimus.commons.entities.TargetDetails;
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

public class DeviceCache extends DataCache<String, TargetDetails> {
  List<TargetDetails> targetDetailsList;
  LoadingCache<String, TargetDetails> masterDeviceCache, availableDevicesCache, engagedDevicesCache;

  public DeviceCache(OptimusSupportedPlatforms platform) {
    targetDetailsList = getDeviceList(platform);
    masterDeviceCache = build(new DeviceCacheLoader());
    availableDevicesCache = build(new DeviceCacheLoader());
    engagedDevicesCache = build(new DeviceCacheLoader());
    loadDeviceDetails();
  }

  private void loadDeviceDetails() {
    targetDetailsList.parallelStream()
        .forEach(deviceDetails -> put(deviceDetails.getUdid(), deviceDetails));
  }

  @Override
  protected synchronized void lock(String udid) {
    try {
      TargetDetails targetDetails = availableDevicesCache.get(udid);
      engagedDevicesCache.put(targetDetails.getUdid(), targetDetails);
      availableDevicesCache.invalidate(udid);
    } catch (Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  @Override
  public void release(String udid) {
    try {
      TargetDetails targetDetails = engagedDevicesCache.get(udid);
      availableDevicesCache.put(targetDetails.getUdid(), targetDetails);
      engagedDevicesCache.invalidate(udid);
    } catch (Exception ex) {
      throw new DeviceReleaseException(udid, ex.getMessage());
    }
  }

  public boolean isAvailable(Predicate<TargetDetails> predicate) {
    return anyMatch(availableDevicesCache, predicate);
  }

  public boolean isEngaged(Predicate<TargetDetails> predicate) {
    return anyMatch(engagedDevicesCache, predicate);
  }

  @Override
  public void put(String key, TargetDetails targetDetails) {
    masterDeviceCache.put(key, targetDetails);
    availableDevicesCache.put(key, targetDetails);
  }

  @Override
  public boolean isPresent(Predicate<TargetDetails> predicate) {
    return anyMatch(masterDeviceCache, predicate);
  }

  @Override
  public synchronized TargetDetails get(Predicate<TargetDetails> predicate) {
    if (!isPresent(predicate)) {
      throw new NoSuchDeviceException();
    }

    if (isAvailable(predicate)) {
      Optional<TargetDetails> availableDevice =
          availableDevicesCache.asMap().values().stream().filter(predicate).findAny();

      if (availableDevice.isPresent()) {
        TargetDetails targetDetails = availableDevice.get();
        lock(targetDetails.getUdid());
        return targetDetails;
      }
    }

    throw new DeviceEngagedException();
  }

  @Override
  public long size() {
    return availableDevicesCache.size();
  }

  private List<TargetDetails> getDeviceList(OptimusSupportedPlatforms platform) {
    List<TargetDetails> targetDetails = new ArrayList<>();
    switch (platform) {
      case ANDROID:
        targetDetails.addAll(new Android().getDevices());
        break;
      case IOS:
        targetDetails.addAll(new IOS().getDevices());
        break;
    }
    return targetDetails;
  }

  private class DeviceCacheLoader extends CacheLoader<String, TargetDetails> {

    @Override
    public TargetDetails load(String udid) {
      return getDeviceDetails(targetDetailsList, udid);
    }

    private synchronized TargetDetails getDeviceDetails(
        List<TargetDetails> targetDetailsList, String udid) {
      return targetDetailsList.stream()
          .filter(deviceDetails -> deviceDetails.getUdid().equals(udid))
          .findAny()
          .orElseThrow(() -> new RuntimeException("No Devices Available"));
    }
  }
}
