package com.testvagrant.optimus.core.parser;

import com.testvagrant.optimus.commons.SystemProperties;
import com.testvagrant.optimus.commons.filehandlers.TestFeedJsonParser;
import com.testvagrant.optimus.core.exceptions.NoTestFeedException;
import com.testvagrant.optimus.core.exceptions.TestFeedTargetsNotFoundException;
import com.testvagrant.optimus.core.models.web.SiteConfig;
import com.testvagrant.optimus.core.models.web.WebTestFeed;
import com.testvagrant.optimus.core.models.web.WebTestFeedDetails;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.Map;

public class WebTestFeedParser {
  private final WebTestFeed webTestFeed;
  private final WebTestFeedDetails webTestFeedDetails;
  private final String testFeedName;

  public WebTestFeedParser(String testFeedName) {
    this.testFeedName = testFeedName;
    webTestFeed = getTestFeed(testFeedName);
    webTestFeedDetails = getWebTestFeedDetails(webTestFeed);
  }

  public DesiredCapabilities getDesiredCapabilities() {
    Map<String, Object> capabilitiesMap = webTestFeedDetails.getDesiredCapabilities();
    capabilitiesMap.put(CapabilityType.BROWSER_NAME, getBrowserName());
    return new DesiredCapabilities(capabilitiesMap);
  }

  public String getBrowserName() {
    return SystemProperties.BROWSER;
  }

  public List<String> getArguments() {
    List<String> arguments = webTestFeedDetails.getArguments();
    if (SystemProperties.HEADLESS) {
      arguments.add("--headless");
    }
    return arguments;
  }

  public List<String> getExtensions() {
    return webTestFeedDetails.getExtensions();
  }

  public Map<String, Object> getExperimentalOptions() {
    return webTestFeedDetails.getExperimentalOptions();
  }

  public Map<String, Object> getPreferences() {
    return webTestFeedDetails.getPreferences();
  }

  public SiteConfig getSiteConfig() {
    return webTestFeed.getSiteConfig();
  }

  private WebTestFeed getTestFeed(String testFeedName) {
    if (testFeedName == null) {
      throw new NoTestFeedException("webFeed");
    }

    TestFeedJsonParser jsonParser = new TestFeedJsonParser();
    return jsonParser.deserialize(testFeedName, WebTestFeed.class);
  }

  private WebTestFeedDetails getWebTestFeedDetails(WebTestFeed testFeed) {
    if (testFeed.getTargets().isEmpty()) throw new TestFeedTargetsNotFoundException(testFeedName);

    return testFeed.getTargets().stream()
        .filter(target -> target.getTarget().equalsIgnoreCase(getBrowserName()))
        .findFirst()
        .orElse(testFeed.getTargets().get(0));
  }
}
