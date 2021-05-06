package com.testvagrant.optimus.parsers;

import com.testvagrant.optimus.core.exceptions.NoTestFeedException;
import com.testvagrant.optimus.core.mobile.MobileDriverManager;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.parser.MobileTestFeedParser;
import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import com.testvagrant.optimus.core.remote.CloudConfigBuilder;
import com.testvagrant.optimus.core.web.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class SystemPropertiesTest {

  public SystemPropertiesTest() {
    System.setProperty("target", "ios");
    System.setProperty("browser", "firefox");
    System.setProperty("headless", "true");
    System.setProperty("hub", "kobiton");
  }

  @Test
  public void platformShouldConsiderTargetSystemProperty() {
    MobileTestFeedParser testFeed = new MobileTestFeedParser("browserStackTestFeed");
    DesiredCapabilities desiredCapabilities = testFeed.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getPlatform(), Platform.IOS);
  }

  @Test
  public void cloudConfigShouldConsiderHubSystemProperty() {
    CloudConfig config = new CloudConfigBuilder().build();
    Assert.assertEquals(config.getHub(), "kobiton");
  }

  @Test
  public void browserShouldObeyBrowserSystemProperty() {
//    System.setProperty("browser", "chrome");
    WebTestFeedParser testFeed = new WebTestFeedParser("webTestFeed");
    DesiredCapabilities desiredCapabilities = testFeed.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getBrowserName(), "firefox");
  }

  @Test
  public void serverArgumentsShouldHaveHeadlessFlagWhenSet() {
    WebTestFeedParser testFeed = new WebTestFeedParser("webTestFeed");
    List<String> arguments = testFeed.getArguments();
    Assert.assertTrue(arguments.contains("--headless"));
  }

  @Test(expectedExceptions = NoTestFeedException.class)
  public void shouldThrowExceptionWhenMobileTestFeedNotSpecified() {
    new MobileDriverManager().createDriverDetails();
  }

  @Test(expectedExceptions = NoTestFeedException.class)
  public void shouldThrowExceptionWhenWebTestFeedNotSpecified() {
    new WebDriverManager().createDriverDetails();
  }
}
