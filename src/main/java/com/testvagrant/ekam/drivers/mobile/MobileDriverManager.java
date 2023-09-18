package com.testvagrant.ekam.drivers.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
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
        return (desiredCapabilities.getPlatformName() == Platform.IOS)
                ? createIOSDriver()
                : createAndroidDriver();
    }

    private WebDriver createIOSDriver() {
        XCUITestOptions options = new XCUITestOptions();
        new CapabilityMapper<XCUITestOptions>().mapToOptions(desiredCapabilities, options);
        return new IOSDriver(url, options);
    }

    private WebDriver createAndroidDriver() {
        UiAutomator2Options options = new UiAutomator2Options();
        new CapabilityMapper<UiAutomator2Options>().mapToOptions(desiredCapabilities, options);
        return new AndroidDriver(url, options);
    }
}
