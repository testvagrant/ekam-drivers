package com.testvagrant.ekam.drivers.web;

import com.testvagrant.ekam.drivers.models.RemoteBrowserConfig;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class RemoteDriverManager extends DriverManager {

  private final URL url;
  private final DesiredCapabilities desiredCapabilities;

  public RemoteDriverManager(RemoteBrowserConfig remoteBrowserConfig) {
    this.url = remoteBrowserConfig.getUrl();
    this.desiredCapabilities = remoteBrowserConfig.getDesiredCapabilities();
    this.desiredCapabilities.setCapability(
        MobileCapabilityType.BROWSER_NAME, remoteBrowserConfig.getBrowser());
  }

  protected WebDriver createDriver() {
    return new RemoteWebDriver(this.url, this.desiredCapabilities);
  }
}
