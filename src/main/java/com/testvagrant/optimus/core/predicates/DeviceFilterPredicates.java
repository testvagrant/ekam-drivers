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
import java.util.stream.Collectors;

public class DeviceFilterPredicates {

  public Predicate<TargetDetails> filterByModel(String model, TestFeedDeviceFilter modelFilter) {
    if (modelFilter.isEmpty()) {
      if (isNullOrEmpty(model)) return ignorePredicate();
      return TargetDetails -> TargetDetails.getName().equals(model);
    }
    List<String> include = ignoreEmptyValues(modelFilter.getInclude());
    List<String> exclude = ignoreEmptyValues(modelFilter.getExclude());

    if (include.isEmpty() && exclude.isEmpty()) {
      return ignorePredicate();
    }

    return include.isEmpty() || exclude.isEmpty()
        ? (include.isEmpty()
            ? TargetDetails -> !exclude.contains(TargetDetails.getName())
            : TargetDetails -> include.contains(TargetDetails.getName()))
        : TargetDetails ->
            include.contains(TargetDetails.getName()) && !exclude.contains(TargetDetails.getName());
  }

  public Predicate<TargetDetails> filterByUdid(String udid, TestFeedDeviceFilter udidFilter) {
    if (udidFilter.isEmpty()) {
      if (isNullOrEmpty(udid)) return ignorePredicate();
      return TargetDetails -> TargetDetails.getUdid().equals(udid);
    }
    List<String> include = ignoreEmptyValues(udidFilter.getInclude());
    List<String> exclude = ignoreEmptyValues(udidFilter.getExclude());

    if (include.isEmpty() && exclude.isEmpty()) {
      return ignorePredicate();
    }

    return include.isEmpty() || exclude.isEmpty()
        ? (include.isEmpty()
            ? TargetDetails -> !exclude.contains(TargetDetails.getUdid())
            : TargetDetails -> include.contains(TargetDetails.getUdid()))
        : TargetDetails ->
            include.contains(TargetDetails.getUdid()) && !exclude.contains(TargetDetails.getUdid());
  }

  public Predicate<TargetDetails> filterByPlatformVersion(
      String platformVersion, TestFeedDeviceFilter platformVersionFilter) {
    if (platformVersionFilter.isEmpty()) {
      if (isNullOrEmpty(platformVersion)) return ignorePredicate();
      return TargetDetails -> TargetDetails.getPlatformVersion().equals(platformVersion);
    }

    List<String> include = ignoreEmptyValues(platformVersionFilter.getInclude());
    include.sort(Comparator.comparing(Version::new));

    List<String> exclude = ignoreEmptyValues(platformVersionFilter.getExclude());

    if (include.isEmpty() && exclude.isEmpty()) {
      return ignorePredicate();
    }

    TestFeedDeviceFilterOperators filterOperators =
        getPlatformVersionOperator(platformVersionFilter);

    Predicate<TargetDetails> targetDetails;

    switch (filterOperators) {
      case GT:
        targetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.GT, "0");
        break;
      case GTE:
        targetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.GTE, "0");
        break;
      case LT:
        targetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.LT, "100");
        break;
      case LTE:
        targetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.LTE, "100");
        break;
      default:
        targetDetails = platformVersionFilter(include, TestFeedDeviceFilterOperators.EQ, "0");
        break;
    }

    return exclude.isEmpty()
        ? targetDetails
        : targetDetails.and(device -> !exclude.contains(device.getPlatformVersion()));
  }

  private Predicate<TargetDetails> platformVersionFilter(
      List<String> platformVersions,
      TestFeedDeviceFilterOperators operator,
      String defaultVersion) {
    if (platformVersions.isEmpty()) {
      return ignorePredicate();
    }
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

  private List<String> ignoreEmptyValues(List<String> deviceFilterList) {
    return deviceFilterList.stream().filter(value -> !value.isEmpty()).collect(Collectors.toList());
  }
}
