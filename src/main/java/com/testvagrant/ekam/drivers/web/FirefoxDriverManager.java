package com.testvagrant.ekam.drivers.web;

import com.testvagrant.ekam.drivers.models.BrowserConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class FirefoxDriverManager extends DriverManager {

  private final BrowserConfig browserConfig;

  public FirefoxDriverManager(BrowserConfig browserConfig) {
    this.browserConfig = browserConfig;
  }

  protected WebDriver createDriver() {
    WebDriverManager.firefoxdriver().setup();
    return new FirefoxDriver(buildFirefoxOptions());
  }

  private FirefoxOptions buildFirefoxOptions() {
    DesiredCapabilities capabilities = browserConfig.getDesiredCapabilities();
    Map<String, Object> preferences = browserConfig.getPreferences();

    FirefoxOptions options = new FirefoxOptions();
    FirefoxProfile profile = new FirefoxProfile();

    preferences.forEach(
        (preference, value) -> profile.setPreference(preference, String.valueOf(value)));
    options.setProfile(profile);
    options.merge(capabilities);
    return options;
  }
}
