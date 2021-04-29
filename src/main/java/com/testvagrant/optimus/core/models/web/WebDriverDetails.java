package com.testvagrant.optimus.core.models.web;

import com.testvagrant.optimus.commons.entities.TargetDetails;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import lombok.*;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WebDriverDetails {
  private RemoteWebDriver driver;
  private Capabilities capabilities;
  private TargetDetails targetDetails;

  @Override
  public String toString() {
    return "{"
        + "\"driver\":"
        + driver
        + ", \"capabilities\":"
        + capabilities
        + ", \"targetDetails\":"
        + targetDetails
        + "}}";
  }
}
