package com.testvagrant.optimus.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceFilters {
  private TestFeedDeviceFilter platformVersion = new TestFeedDeviceFilter();
  private TestFeedDeviceFilter udid = new TestFeedDeviceFilter();
  private TestFeedDeviceFilter model = new TestFeedDeviceFilter();
}
