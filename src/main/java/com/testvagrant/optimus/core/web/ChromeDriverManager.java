package com.testvagrant.optimus.core.web;

import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.List;
import java.util.Map;

class ChromeDriverManager {

  private final ChromeOptions chromeOptions;

  public ChromeDriverManager(WebTestFeedParser testFeedParser) {
    this.chromeOptions = buildChromeOptions(testFeedParser);
  }

  public WebDriver createDriver() {
    WebDriverManager.chromedriver().setup();
    return new ChromeDriver(chromeOptions);
  }

  private ChromeOptions buildChromeOptions(WebTestFeedParser testFeedParser) {
    List<String> arguments = testFeedParser.getArguments();
    List<String> extensions = testFeedParser.getExtensions();
    Map<String, Object> preferences = testFeedParser.getPreferences();
    Map<String, Object> experimentalOptions = testFeedParser.getExperimentalOptions();
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();

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
