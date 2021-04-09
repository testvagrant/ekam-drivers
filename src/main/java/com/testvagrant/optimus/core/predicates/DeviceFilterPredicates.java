package com.testvagrant.optimus.core.predicates;

import com.testvagrant.optimus.commons.entities.DeviceDetails;

import java.util.function.Predicate;

public class DeviceFilterPredicates {

  public Predicate<DeviceDetails> filterByPlatformVersion(String platformVersion) {
    if (isNullOrEmpty(platformVersion)) return ignorePredicate();
    return deviceDetails -> deviceDetails.getPlatformVersion().equals(platformVersion);
  }

  public Predicate<DeviceDetails> filterByModel(String model) {
    if (isNullOrEmpty(model)) return ignorePredicate();
    return deviceDetails -> deviceDetails.getDeviceName().equals(model);
  }

  public Predicate<DeviceDetails> filterByUdid(String udid) {
    if (isNullOrEmpty(udid)) return ignorePredicate();
    return deviceDetails -> deviceDetails.getUdid().equals(udid);
  }

  private Predicate<DeviceDetails> ignorePredicate() {
    return deviceDetails -> true;
  }

  private boolean isNullOrEmpty(String capability) {
    return capability == null || capability.isEmpty();
  }
}
