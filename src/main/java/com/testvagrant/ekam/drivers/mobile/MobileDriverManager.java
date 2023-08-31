package com.testvagrant.ekam.drivers.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class MobileDriverManager {
    private final DesiredCapabilities desiredCapabilities;
    private final URL url;

    public MobileDriverManager(URL url, DesiredCapabilities desiredCapabilities) {
        this.url = url;
        this.desiredCapabilities = desiredCapabilities;
    }

    public WebDriver createDriver() {
        return desiredCapabilities.getPlatformName() == Platform.IOS
                ? new IOSDriver(url, desiredCapabilities)
                : new AndroidDriver(url, desiredCapabilities);
    }
}
