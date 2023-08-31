package com.testvagrant.ekam.drivers.web;

import com.testvagrant.ekam.drivers.models.BrowserConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FirefoxDriverManager extends DriverManager {

    private final BrowserConfig browserConfig;

    public FirefoxDriverManager(BrowserConfig browserConfig) {
        this.browserConfig = browserConfig;
    }

    protected WebDriver createDriver() {
        return new FirefoxDriver(buildFirefoxOptions());
    }

    private FirefoxOptions buildFirefoxOptions() {
        List<String> arguments = browserConfig.getArguments();
        List<String> extensions = browserConfig.getExtensions();
        Map<String, Object> preferences = browserConfig.getPreferences();
        DesiredCapabilities desiredCapabilities = browserConfig.getDesiredCapabilities();

        FirefoxOptions options = new FirefoxOptions();
        FirefoxProfile profile = new FirefoxProfile();

        preferences.forEach(
                (preference, value) -> profile.setPreference(preference, String.valueOf(value)));

        if (extensions.size() > 0) {
            extensions.forEach(extensionPath -> profile.addExtension(new File(extensionPath)));
        }

        if (arguments.size() > 0) options.addArguments(arguments);

        options.setProfile(profile);
        options.merge(desiredCapabilities);
        return options;
    }
}
