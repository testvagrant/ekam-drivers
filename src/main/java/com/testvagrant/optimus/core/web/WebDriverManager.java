package com.testvagrant.optimus.core.web;

import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.web.WebDriverDetails;
import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import com.testvagrant.optimus.core.remote.CloudConfigBuilder;
import com.testvagrant.optimus.core.remote.RemoteUrlBuilder;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class WebDriverManager {

  private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
  private WebTestFeedParser testFeedParser;
  private final DesiredCapabilities desiredCapabilities;

  public WebDriverManager() {
    testFeedParser = new WebTestFeedParser(System.getProperty("testFeed"));
    desiredCapabilities = testFeedParser.getDesiredCapabilities();
  }

  public WebDriverManager(DesiredCapabilities capabilities) {
    this.desiredCapabilities = capabilities;
  }

  public WebDriverDetails createDriverDetails() {
    String target = System.getProperty("target", "local").toLowerCase();
    return target.equals("remote") ? createRemoteDriver() : createLocalDriver();
  }

  public static void dispose() {
    if (driverThreadLocal.get() != null) {
      driverThreadLocal.get().quit();
    }
  }

  private WebDriverDetails createRemoteDriver() {
    URL url = RemoteUrlBuilder.build(new CloudConfigBuilder().build());
    OptimusSupportedPlatforms platform =
        OptimusSupportedPlatforms.valueOf(
            desiredCapabilities
                .getCapability(CapabilityType.BROWSER_NAME)
                .toString()
                .toUpperCase());

    switch (platform) {
      case CHROME:
      case FIREFOX:
      case SAFARI:
      case EDGE:
        driverThreadLocal.set(new RemoteWebDriver(url, desiredCapabilities));
        break;
      case UNSUPPORTED:
      default:
        throw new UnsupportedPlatform();
    }

    return getWebDriverDetails(driverThreadLocal.get());
  }

  private WebDriverDetails createLocalDriver() {
    String browser = testFeedParser.getBrowserName().trim().toLowerCase();
    WebDriver webDriver =
        browser.equals("firefox")
            ? new FirefoxDriverManager(testFeedParser).createDriver()
            : new ChromeDriverManager(testFeedParser).createDriver();

    driverThreadLocal.set(webDriver);
    return getWebDriverDetails(driverThreadLocal.get());
  }

  private WebDriverDetails getWebDriverDetails(WebDriver webDriver) {
    Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();

    TargetDetails target =
        TargetDetails.builder()
            .name(capabilities.getBrowserName())
            .platformVersion(capabilities.getVersion())
            .platform(
                OptimusSupportedPlatforms.valueOf(capabilities.getBrowserName().toUpperCase()))
            .build();

    return WebDriverDetails.builder()
        .driver((RemoteWebDriver) webDriver)
        .targetDetails(target)
        .capabilities(capabilities)
        .build();
  }
}
