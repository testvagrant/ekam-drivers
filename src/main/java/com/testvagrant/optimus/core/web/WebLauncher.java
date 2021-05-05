package com.testvagrant.optimus.core.web;

import com.testvagrant.optimus.core.models.web.SiteConfig;
import com.testvagrant.optimus.core.models.web.WebDriverDetails;
import org.awaitility.Awaitility;

import java.time.Duration;

public class WebLauncher {

  public void launch(SiteConfig siteConfig, WebDriverDetails webDriverDetails) {
    webDriverDetails.getDriver().get(siteConfig.getUrl());
    for (int retry = 0; retry < 3; retry++) {
      try {
        Awaitility.await()
            .atMost(Duration.ofMinutes(1))
            .until(
                () ->
                    webDriverDetails
                        .getDriver()
                        .getTitle()
                        .toLowerCase()
                        .contains(siteConfig.getTitle().toLowerCase()));
        break;
      } catch (Exception e) {
        webDriverDetails.getDriver().navigate().refresh();
      }
    }
  }
}
