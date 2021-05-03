package com.testvagrant.optimus.core.models.web;

import com.testvagrant.optimus.core.models.TargetDetails;
import lombok.*;
import org.openqa.selenium.Capabilities;
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
