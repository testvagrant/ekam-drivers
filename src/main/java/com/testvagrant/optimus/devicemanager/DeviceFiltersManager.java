package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.core.predicates.DeviceFilterPredicates;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.function.Predicate;

public class DeviceFiltersManager {

  public Predicate<DeviceDetails> createDeviceFilters(DesiredCapabilities desiredCapabilities) {
    DeviceFilterPredicates deviceFilterPredicates = new DeviceFilterPredicates();
    Predicate<DeviceDetails> basePredicate =
        deviceFilterPredicates.filterByModel(
            getCapability(desiredCapabilities, MobileCapabilityType.DEVICE_NAME));
    return basePredicate
        .and(
            deviceFilterPredicates.filterByUdid(
                getCapability(desiredCapabilities, MobileCapabilityType.UDID)))
        .and(
            deviceFilterPredicates.filterByPlatformVersion(
                getCapability(desiredCapabilities, MobileCapabilityType.PLATFORM_VERSION)));
  }

  private String getCapability(DesiredCapabilities desiredCapabilities, String capabilityType) {
    return (String) desiredCapabilities.getCapability(capabilityType);
  }
}
