package com.testvagrant.optimus.core.remote;

import com.testvagrant.optimus.commons.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDriverManager {

  private static final ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();

  public RemoteWebDriver createDriver(CloudConfig cloudConfig, Capabilities desiredCapabilities) {
    URL url = buildRemoteUrl(cloudConfig);
    OptimusSupportedPlatforms platform = getPlatform(desiredCapabilities);

    switch (platform) {
      case IOS:
        driverThreadLocal.set(new IOSDriver<>(url, desiredCapabilities));
        break;
      case ANDROID:
        driverThreadLocal.set(new AndroidDriver<>(url, desiredCapabilities));
        break;
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

    return driverThreadLocal.get();
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
}
