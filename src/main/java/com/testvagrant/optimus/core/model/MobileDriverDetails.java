package com.testvagrant.optimus.core.model;

import com.testvagrant.optimus.commons.entities.DeviceDetails;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.*;
import org.openqa.selenium.remote.DesiredCapabilities;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MobileDriverDetails {
  private AppiumDriver<MobileElement> driver;
  private DesiredCapabilities capabilities;
  private AppiumDriverLocalService service;
  private DeviceDetails deviceDetails;

  @Override
  public String toString() {
    return "{"
        + "\"driver\":"
        + driver
        + ", \"capabilities\":"
        + capabilities
        + ", \"service\":"
        + service
        + ", \"deviceDetails\":"
        + deviceDetails
        + "}}";
  }
}
