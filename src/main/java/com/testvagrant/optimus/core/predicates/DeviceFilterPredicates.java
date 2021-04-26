package com.testvagrant.optimus.core.predicates;

import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.core.comparables.Version;
import com.testvagrant.optimus.core.models.mobile.TestFeedDeviceFilter;
import com.testvagrant.optimus.core.models.mobile.TestFeedDeviceFilterOperators;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class DeviceFilterPredicates {

  public Predicate<DeviceDetails> filterByModel(String model, TestFeedDeviceFilter modelFilter) {
    if (modelFilter.isEmpty()) {
      if (isNullOrEmpty(model)) return ignorePredicate();
      return deviceDetails -> deviceDetails.getDeviceName().equals(model);
    }
    List<String> include = modelFilter.getInclude();
    List<String> exclude = modelFilter.getExclude();
    return deviceDetails ->
        include.contains(deviceDetails.getDeviceName())
            && !exclude.contains(deviceDetails.getDeviceName());
  }

  public Predicate<DeviceDetails> filterByUdid(String udid, TestFeedDeviceFilter udidFilter) {
    if (udidFilter.isEmpty()) {
      if (isNullOrEmpty(udid)) return ignorePredicate();
      return deviceDetails -> deviceDetails.getUdid().equals(udid);
    }
    List<String> include = udidFilter.getInclude();
    List<String> exclude = udidFilter.getExclude();
    return deviceDetails ->
        include.contains(deviceDetails.getUdid()) && !exclude.contains(deviceDetails.getUdid());
  }

  public Predicate<DeviceDetails> filterByPlatformVersion(
      String platformVersion, TestFeedDeviceFilter platformVersionFilter) {
    if (platformVersionFilter.isEmpty()) {
      if (isNullOrEmpty(platformVersion)) return ignorePredicate();
      return deviceDetails -> deviceDetails.getPlatformVersion().equals(platformVersion);
    }

    List<String> include = platformVersionFilter.getInclude();
    include.sort(Comparator.comparing(Version::new));

    List<String> exclude = platformVersionFilter.getExclude();

    TestFeedDeviceFilterOperators filterOperators =
        getPlatformVersionOperator(platformVersionFilter);

    Predicate<DeviceDetails> deviceDetails;

    switch (filterOperators) {
      case GT:
        deviceDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.GT, "0");
        break;
      case GTE:
        deviceDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.GTE, "0");
        break;
      case LT:
        deviceDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.LT, "100");
        break;
      case LTE:
        deviceDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.LTE, "100");
        break;
      default:
        deviceDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.EQ, "0");
        break;
    }

    return deviceDetails.and(device -> !exclude.contains(device.getPlatformVersion()));
  }

  private Predicate<DeviceDetails> platformVersionFilter(
      List<String> platformVersions,
      TestFeedDeviceFilterOperators operator,
      String defaultVersion) {
    return deviceDetails -> {
      Version actualVersion = new Version(deviceDetails.getPlatformVersion());
      Version expectedVersion =
          new Version(platformVersions.stream().findFirst().orElse(defaultVersion));
      return actualVersion.compareTo(expectedVersion) == operator.getValue();
    };
  }

  private Predicate<DeviceDetails> ignorePredicate() {
    return deviceDetails -> true;
  }

  private boolean isNullOrEmpty(String capability) {
    return capability == null || capability.isEmpty();
  }

  private TestFeedDeviceFilterOperators getPlatformVersionOperator(
      TestFeedDeviceFilter testFeedDeviceFilter) {
    String operator = testFeedDeviceFilter.getOperator();
    Optional<TestFeedDeviceFilterOperators> deviceFilterOperatorsOptional =
        Arrays.stream(TestFeedDeviceFilterOperators.values())
            .filter(opr -> opr.getOperator().equals(operator.trim()))
            .findFirst();
    return deviceFilterOperatorsOptional.orElse(TestFeedDeviceFilterOperators.EQ);
  }
}
