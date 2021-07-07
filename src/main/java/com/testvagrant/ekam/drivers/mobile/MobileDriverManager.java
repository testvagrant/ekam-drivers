package com.testvagrant.ekam.drivers.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class MobileDriverManager {
  private final DesiredCapabilities desiredCapabilities;
  private final URL url;

  public MobileDriverManager(URL url, DesiredCapabilities desiredCapabilities) {
    this.url = url;
    this.desiredCapabilities = desiredCapabilities;
  }

  public AppiumDriver<MobileElement> createDriver() {
    return desiredCapabilities.getPlatform() == Platform.IOS
        ? new IOSDriver<>(url, desiredCapabilities)
        : new AndroidDriver<>(url, desiredCapabilities);
  }
}
