package com.testvagrant.optimus.core.remote;

import com.testvagrant.optimus.core.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.models.mobile.DeviceFilters;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import com.testvagrant.optimus.devicemanager.BrowserstackDeviceManager;
import com.testvagrant.optimus.devicemanager.DeviceFiltersManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Predicate;

public class RemoteDriverManager {

  private static final ThreadLocal<RemoteWebDriver> driverThreadLocal = new ThreadLocal<>();

  public RemoteWebDriver createDriver(
      CloudConfig cloudConfig, DesiredCapabilities desiredCapabilities) {
    URL url = buildRemoteUrl(cloudConfig);
    OptimusSupportedPlatforms platform = getPlatform(desiredCapabilities);

    DesiredCapabilities updatedCapabilities =
        updateDesiredCapabilitiesWithDeviceFilters(cloudConfig, desiredCapabilities, platform);

    switch (platform) {
      case IOS:
        driverThreadLocal.set(new IOSDriver<>(url, updatedCapabilities));
        break;
      case ANDROID:
        driverThreadLocal.set(new AndroidDriver<>(url, updatedCapabilities));
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

  private DesiredCapabilities updateDesiredCapabilitiesWithDeviceFilters(
      CloudConfig cloudConfig,
      DesiredCapabilities desiredCapabilities,
      OptimusSupportedPlatforms platform) {
    String testFeed = System.getProperty("testFeed");

    if (cloudConfig.getHub().toLowerCase().contains("browserstack") && testFeed != null) {
      DeviceFilters deviceFilters =
          new TestFeedParser(System.getProperty("testFeed")).getDeviceFilters();

      Predicate<TargetDetails> filters =
          new DeviceFiltersManager().createDeviceFilters(desiredCapabilities, deviceFilters);

      TargetDetails TargetDetails =
          BrowserstackDeviceManager.getInstance(platform).getDevice(filters);

      System.out.println("Device details: " + TargetDetails.toString());

      return updateBrowserstackDesiredCapabilitiesWithTargetDetails(
          TargetDetails, desiredCapabilities);
    }

    return desiredCapabilities;
  }

  private DesiredCapabilities updateBrowserstackDesiredCapabilitiesWithTargetDetails(
      TargetDetails TargetDetails, DesiredCapabilities desiredCapabilities) {
    desiredCapabilities.setCapability("device", TargetDetails.getName());
    desiredCapabilities.setCapability("os_version", TargetDetails.getPlatformVersion());
    return desiredCapabilities;
  }
}
