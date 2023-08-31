package com.testvagrant.ekam.drivers.web;

import com.testvagrant.ekam.drivers.models.BrowserConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ChromeDriverManager extends DriverManager {

  private final BrowserConfig browserConfig;

  public ChromeDriverManager(BrowserConfig browserConfig) {
    this.browserConfig = browserConfig;
  }

  protected WebDriver createDriver() {
    return new ChromeDriver(buildChromeOptions());
  }

  private ChromeOptions buildChromeOptions() {
    List<String> arguments = browserConfig.getArguments();
    List<String> extensions = browserConfig.getExtensions();
    Map<String, Object> preferences = browserConfig.getPreferences();
    Map<String, Object> experimentalOptions = browserConfig.getExperimentalOptions();
    DesiredCapabilities desiredCapabilities = browserConfig.getDesiredCapabilities();

    ChromeOptions options = new ChromeOptions();

    if (arguments.size() > 0) options.addArguments(arguments);

    if (extensions.size() > 0) {
      extensions.forEach(extensionPath -> options.addExtensions(new File(extensionPath)));
    }

    if (preferences.keySet().size() > 0) {
      options.setExperimentalOption("prefs", preferences);
    }

    if (experimentalOptions.keySet().size() > 0) {
      experimentalOptions.forEach(options::setExperimentalOption);
    }

    options.merge(desiredCapabilities);
    return options;
  }
}
