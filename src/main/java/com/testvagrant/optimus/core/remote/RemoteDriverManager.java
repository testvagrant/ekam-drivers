package com.testvagrant.optimus.core.remote;

import com.testvagrant.optimus.commons.entities.TargetDetails;
import com.testvagrant.optimus.commons.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.mobile.MobileDriverDetails;
import com.testvagrant.optimus.core.models.web.WebDriverDetails;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDriverManager {

  private static final ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();

  public MobileDriverDetails createMobileDriver(CloudConfig cloudConfig, Capabilities desiredCapabilities) {
    URL url = buildRemoteUrl(cloudConfig);
    OptimusSupportedPlatforms platform = getPlatform(desiredCapabilities);

    switch (platform) {
      case IOS:
        driverThreadLocal.set(new IOSDriver<>(url, desiredCapabilities));
        break;
      case ANDROID:
        driverThreadLocal.set(new AndroidDriver<>(url, desiredCapabilities));
        break;
      default:
        throw new UnsupportedPlatform();
    }

    return getMobileDriverDetails((AppiumDriver<MobileElement>) driverThreadLocal.get());
  }

  public WebDriverDetails createWebDriver(CloudConfig cloudConfig, Capabilities desiredCapabilities) {
    URL url = buildRemoteUrl(cloudConfig);
    OptimusSupportedPlatforms platform = getPlatform(desiredCapabilities);

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

  public static void dispose() {
    if (driverThreadLocal.get() != null) {
      driverThreadLocal.get().quit();
    }
  }

  private URL buildRemoteUrl(CloudConfig cloudConfig) {
    try {
      String urlString =
          String.format(
              "https://%s:%s@%s/wd/hub",
              cloudConfig.getUsername(), cloudConfig.getAccessKey(), cloudConfig.getHub());
      return new URL(urlString);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private OptimusSupportedPlatforms getPlatform(Capabilities desiredCapabilities) {
    try {
      String platform =
          desiredCapabilities.getCapability(CapabilityType.PLATFORM_NAME) != null
              ? (String) desiredCapabilities.getCapability(CapabilityType.PLATFORM_NAME)
              : (String) desiredCapabilities.getCapability(CapabilityType.BROWSER_NAME);

      return OptimusSupportedPlatforms.valueOf(platform.toUpperCase().trim());
    } catch (Exception ex) {
      return OptimusSupportedPlatforms.UNSUPPORTED;
    }
  }

  private MobileDriverDetails getMobileDriverDetails(AppiumDriver<MobileElement> appiumDriver) {
    DesiredCapabilities capabilities = (DesiredCapabilities) appiumDriver.getCapabilities();
    TargetDetails targetDetails = TargetDetails.builder().platform(OptimusSupportedPlatforms.valueOf(capabilities.getPlatform().name().toUpperCase()))
            .platformVersion(capabilities.getCapability(MobileCapabilityType.PLATFORM_VERSION).toString())
            .name(capabilities.getCapability("deviceModel").toString())
            .udid(capabilities.getCapability(MobileCapabilityType.UDID).toString()).build();
    return MobileDriverDetails.builder()
            .capabilities(capabilities)
            .targetDetails(targetDetails)
            .driver(appiumDriver)
            .build();
  }

  private WebDriverDetails getWebDriverDetails(RemoteWebDriver webDriver) {
    Capabilities capabilities = webDriver.getCapabilities();
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
