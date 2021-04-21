package com.testvagrant.optimus.core.web;

import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.Map;

class FirefoxDriverManager {

  private final FirefoxOptions firefoxOptions;

  public FirefoxDriverManager(WebTestFeedParser testFeedParser) {
    this.firefoxOptions = buildFirefoxOptions(testFeedParser);
  }

  public WebDriver createDriver() {
    WebDriverManager.firefoxdriver().setup();
    return new FirefoxDriver(firefoxOptions);
  }

  private FirefoxOptions buildFirefoxOptions(WebTestFeedParser testFeedParser) {
    Map<String, Object> capabilities = testFeedParser.getDesiredCapabilities().asMap();
    Map<String, Object> preferences = testFeedParser.getPreferences();

    FirefoxOptions options = new FirefoxOptions();
    FirefoxProfile profile = new FirefoxProfile();

    preferences.forEach((preference, value) -> profile.setPreference(preference, (String) value));
    capabilities.forEach(options::setCapability);
    options.setProfile(profile);

    return options;
  }
}
