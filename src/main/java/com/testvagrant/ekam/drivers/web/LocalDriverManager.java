package com.testvagrant.ekam.drivers.web;

import com.testvagrant.ekam.drivers.exceptions.UnsupportedPlatform;
import com.testvagrant.ekam.drivers.models.BrowserConfig;
import org.openqa.selenium.WebDriver;

public class LocalDriverManager extends DriverManager {

  private final BrowserConfig browserConfig;
  private final String browser;

  public LocalDriverManager(String browser, BrowserConfig browserConfig) {
    this.browser = browser;
    this.browserConfig = browserConfig;
  }

  @Override
  protected WebDriver createDriver() {
    switch (browser.toLowerCase().trim()) {
      case "firefox":
        return new FirefoxDriverManager(browserConfig).createDriver();
      case "msedge":
        return new EdgeDriverManager(browserConfig).createDriver();
      case "safari":
        throw new UnsupportedPlatform();
      default:
        return new ChromeDriverManager(browserConfig).createDriver();
    }
  }
}
