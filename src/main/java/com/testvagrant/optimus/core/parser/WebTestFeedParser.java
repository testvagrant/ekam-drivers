package com.testvagrant.optimus.core.parser;

import com.testvagrant.optimus.commons.entities.TargetDetails;
import com.testvagrant.optimus.commons.exceptions.NoTestFeedException;
import com.testvagrant.optimus.commons.filehandlers.JsonParser;
import com.testvagrant.optimus.core.models.web.WebTestFeed;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.Map;

public class WebTestFeedParser {
  private final WebTestFeed webTestFeed;
  private final String browser;

  public WebTestFeedParser(String testFeedName) {
    webTestFeed = getTestFeed(testFeedName);
    this.browser = System.getProperty("browser", "chrome");
  }

  public DesiredCapabilities getDesiredCapabilities() {
    Map<String, Object> capabilitiesMap = webTestFeed.getDesiredCapabilities();
    capabilitiesMap.put(CapabilityType.BROWSER_NAME, browser);
    return new DesiredCapabilities(capabilitiesMap);
  }

  public String getBrowserName() {
    return browser;
  }

  public List<String> getArguments() {
    return webTestFeed.getArguments();
  }

  public List<String> getExtensions() {
    return webTestFeed.getExtensions();
  }

  public Map<String, Object> getExperimentalOptions() {
    return webTestFeed.getExperimentalOptions();
  }

  public Map<String, Object> getPreferences() {
    return webTestFeed.getPreferences();
  }


  private WebTestFeed getTestFeed(String testFeedName) {
    if (testFeedName == null) {
      throw new NoTestFeedException();
    }

    JsonParser jsonParser = new JsonParser();
    return jsonParser.deserialize(testFeedName, WebTestFeed.class);
  }
}
