package com.testvagrant.optimus.parsers;

import com.testvagrant.optimus.core.exceptions.TestFeedNotFoundException;
import com.testvagrant.optimus.core.exceptions.TestFeedTargetsNotFoundException;
import com.testvagrant.optimus.core.models.web.SiteConfig;
import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class WebTestFeedParserTest {

  private final WebTestFeedParser webTestFeed;

  public WebTestFeedParserTest() {
    webTestFeed = new WebTestFeedParser("webTestFeed");
  }

  @Test
  public void defaultBrowserShouldBeChromeWhenNotSpecified() {
    DesiredCapabilities desiredCapabilities = webTestFeed.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getBrowserName(), "chrome");
  }

  @Test
  public void headlessRunModeShouldBeFalseByDefault() {
    List<String> arguments = webTestFeed.getArguments();
    Assert.assertFalse(arguments.contains("--headless"));
  }

  @Test
  public void siteConfigDetailsShouldBeParsed() {
    SiteConfig siteConfig = webTestFeed.getSiteConfig();
    Assert.assertFalse(siteConfig.getTitle().isEmpty());
    Assert.assertFalse(siteConfig.getUrl().isEmpty());
  }

  @Test(expectedExceptions = TestFeedTargetsNotFoundException.class)
  public void throwExceptionIfNoTargetsFound() {
    WebTestFeedParser testFeed = new WebTestFeedParser("noTargetsWebTestFeed");
    testFeed.getDesiredCapabilities();
  }

  @Test(expectedExceptions = TestFeedNotFoundException.class)
  public void throwExceptionIfTestFeedIsNotFound() {
    WebTestFeedParser testFeed = new WebTestFeedParser("invalid");
    testFeed.getDesiredCapabilities();
  }
}
