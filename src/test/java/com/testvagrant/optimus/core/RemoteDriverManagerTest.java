package com.testvagrant.optimus.core;

import com.testvagrant.optimus.commons.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import com.testvagrant.optimus.core.remote.CloudConfigBuilder;
import com.testvagrant.optimus.core.remote.RemoteDriverManager;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class RemoteDriverManagerTest {

  private RemoteWebDriver driver;

  @AfterMethod
  public void tearDown() {
    System.out.println("Disposing driver");
    RemoteDriverManager.dispose();
  }

  @Test(expectedExceptions = UnsupportedPlatform.class)
  public void driverCreationShouldThrowNoPlatformExceptionForPlatformBrowserNotSet() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("newCommandTimeout", "300");
    CloudConfig build = new CloudConfigBuilder().build();
    driver = new RemoteDriverManager().createDriver(build, caps);
  }

  @Test()
  public void createMobileDriverSuccessfully() {
    CloudConfig build = new CloudConfigBuilder().build();
    DesiredCapabilities browserStackCapabilities = getMobileBrowserStackCapabilities();
    driver = new RemoteDriverManager().createDriver(build, browserStackCapabilities);
    Assert.assertNotNull(driver);
  }

  @Test()
  public void createMobileDriverSuccessfullyWhenTestFeedIsPresent() {
    TestFeedParser testFeedParser = new TestFeedParser("sampleBrowserStack");
    CloudConfig build = new CloudConfigBuilder().build();
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
    driver = new RemoteDriverManager().createDriver(build, desiredCapabilities);
    Assert.assertNotNull(driver);
  }

  @Test()
  public void createWebDriverSuccessfullyWhenTestFeedIsPresent() {
    WebTestFeedParser testFeedParser = new WebTestFeedParser("chromeTestFeed");
    CloudConfig build = new CloudConfigBuilder().build();
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
    driver = new RemoteDriverManager().createDriver(build, desiredCapabilities);
    Assert.assertNotNull(driver);
  }

  @Test()
  public void createWebDriverSuccessfully() {
    CloudConfig build = new CloudConfigBuilder().build();
    DesiredCapabilities browserStackCapabilities = getWebBrowserStackCapabilities();
    driver = new RemoteDriverManager().createDriver(build, browserStackCapabilities);
    Assert.assertNotNull(driver);
  }

  @Test
  public void cloudConfigEnvTest() {
    CloudConfig cloudConfig = new CloudConfigBuilder().build();
    System.out.println(cloudConfig.getAccessKey());
  }

  private DesiredCapabilities getMobileBrowserStackCapabilities() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("device", "Google Pixel 3");
    caps.setCapability("os_version", "9.0");
    caps.setCapability("app", "bs://cbedade4e22e185541ec3035cac875fb79c6ad6d");
    caps.setCapability("autoGrantPermissions", "true");
    caps.setCapability("newCommandTimeout", "300");
    caps.setCapability("platform", "android");
    caps.setCapability(CapabilityType.PLATFORM_NAME, "android");
    return caps;
  }

  private DesiredCapabilities getWebBrowserStackCapabilities() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("newCommandTimeout", "300");
    caps.setCapability(CapabilityType.BROWSER_NAME, "chrome");
    caps.setCapability(CapabilityType.BROWSER_VERSION, "latest");
    return caps;
  }
}
