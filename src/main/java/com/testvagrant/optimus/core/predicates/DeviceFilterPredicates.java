package com.testvagrant.optimus.core.predicates;

import com.testvagrant.optimus.core.comparables.Version;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.models.mobile.TestFeedDeviceFilter;
import com.testvagrant.optimus.core.models.mobile.TestFeedDeviceFilterOperators;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class DeviceFilterPredicates {

  public Predicate<TargetDetails> filterByModel(String model, TestFeedDeviceFilter modelFilter) {
    if (modelFilter.isEmpty()) {
      if (isNullOrEmpty(model)) return ignorePredicate();
      return TargetDetails -> TargetDetails.getName().equals(model);
    }
    List<String> include = modelFilter.getInclude();
    List<String> exclude = modelFilter.getExclude();
    return TargetDetails ->
        include.contains(TargetDetails.getName()) && !exclude.contains(TargetDetails.getName());
  }

  public Predicate<TargetDetails> filterByUdid(String udid, TestFeedDeviceFilter udidFilter) {
    if (udidFilter.isEmpty()) {
      if (isNullOrEmpty(udid)) return ignorePredicate();
      return TargetDetails -> TargetDetails.getUdid().equals(udid);
    }
    List<String> include = udidFilter.getInclude();
    List<String> exclude = udidFilter.getExclude();
    return TargetDetails ->
        include.contains(TargetDetails.getUdid()) && !exclude.contains(TargetDetails.getUdid());
  }

  public Predicate<TargetDetails> filterByPlatformVersion(
      String platformVersion, TestFeedDeviceFilter platformVersionFilter) {
    if (platformVersionFilter.isEmpty()) {
      if (isNullOrEmpty(platformVersion)) return ignorePredicate();
      return TargetDetails -> TargetDetails.getPlatformVersion().equals(platformVersion);
    }

    List<String> include = platformVersionFilter.getInclude();
    include.sort(Comparator.comparing(Version::new));

    List<String> exclude = platformVersionFilter.getExclude();

    TestFeedDeviceFilterOperators filterOperators =
        getPlatformVersionOperator(platformVersionFilter);

    Predicate<TargetDetails> TargetDetails;

    switch (filterOperators) {
      case GT:
        TargetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.GT, "0");
        break;
      case GTE:
        TargetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.GTE, "0");
        break;
      case LT:
        TargetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.LT, "100");
        break;
      case LTE:
        TargetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.LTE, "100");
        break;
      default:
        TargetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.EQ, "0");
        break;
    }

    return TargetDetails.and(device -> !exclude.contains(device.getPlatformVersion()));
  }

  private Predicate<TargetDetails> platformVersionFilter(
      List<String> platformVersions,
      TestFeedDeviceFilterOperators operator,
      String defaultVersion) {
    return TargetDetails -> {
      Version actualVersion = new Version(TargetDetails.getPlatformVersion());
      Version expectedVersion =
          new Version(platformVersions.stream().findFirst().orElse(defaultVersion));
      return actualVersion.compareTo(expectedVersion) == operator.getValue();
    };
  }

  private Predicate<TargetDetails> ignorePredicate() {
    return TargetDetails -> true;
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
