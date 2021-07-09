package com.testvagrant.ekam.drivers.web;

import com.testvagrant.ekam.drivers.models.RemoteBrowserConfig;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Map;

public class RemoteDriverManager extends DriverManager {

  private final URL url;
  private final RemoteBrowserConfig browserConfig;

  public RemoteDriverManager(RemoteBrowserConfig remoteBrowserConfig) {
    this.url = remoteBrowserConfig.getUrl();
    this.browserConfig = remoteBrowserConfig;
  }

  protected WebDriver createDriver() {
    Map<String, Object> experimentalOptions = browserConfig.getExperimentalOptions();
    DesiredCapabilities desiredCapabilities = browserConfig.getDesiredCapabilities();

    String browser = browserConfig.getBrowser().trim();
    if (browser.equalsIgnoreCase("chrome")) {
      desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, experimentalOptions);
    }
    desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browser);
    return new RemoteWebDriver(url, desiredCapabilities);
  }
}
