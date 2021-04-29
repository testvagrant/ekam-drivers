package com.testvagrant.optimus.core.web;

import com.testvagrant.optimus.commons.entities.TargetDetails;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.web.WebDriverDetails;
import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LocalWebDriverManager {

  private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
  private final WebTestFeedParser testFeedParser;

  public LocalWebDriverManager() {
    testFeedParser = new WebTestFeedParser(System.getProperty("testFeed"));
  }

  public WebDriverDetails createDriver() {
    String browser = testFeedParser.getBrowserName().trim().toLowerCase();
    WebDriver webDriver =
        browser.equals("firefox")
            ? new FirefoxDriverManager(testFeedParser).createDriver()
            : new ChromeDriverManager(testFeedParser).createDriver();

    driverThreadLocal.set(webDriver);
    return getWebDriverDetails(driverThreadLocal.get());
  }

  public static void dispose() {
    if (driverThreadLocal.get() != null) {
      driverThreadLocal.get().quit();
    }
  }

  public WebDriverDetails getWebDriverDetails(WebDriver webDriver) {
    Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
    TargetDetails target = TargetDetails.builder()
            .name(capabilities.getBrowserName())
            .platformVersion(capabilities.getVersion())
            .platform(OptimusSupportedPlatforms.valueOf(capabilities.getBrowserName().toUpperCase()))
            .build();
    return WebDriverDetails.builder()
            .driver(webDriver)
            .targetDetails(target)
            .capabilities(capabilities)
            .build();
  }
}
