package com.testvagrant.optimus.core;

import com.testvagrant.optimus.core.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.mobile.MobileDriverManager;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import com.testvagrant.optimus.core.web.WebDriverManager;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class RemoteDriverManagerTest {

  private RemoteWebDriver driver;

  public RemoteDriverManagerTest() {
    System.setProperty("target", "remote");
  }

  @AfterMethod
  public void tearDown() {
    System.out.println("Disposing driver");
    WebDriverManager.dispose();
    MobileDriverManager.dispose();
  }

  @Test()
  public void createMobileDriverSuccessfully() {
    DesiredCapabilities browserStackCapabilities = getMobileBrowserStackCapabilities();
    driver = new MobileDriverManager(browserStackCapabilities).createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test()
  public void createMobileDriverSuccessfullyWhenTestFeedIsPresent() {
    TestFeedParser testFeedParser = new TestFeedParser("sampleBrowserStack");
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
    driver = new MobileDriverManager(desiredCapabilities).createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test(expectedExceptions = UnsupportedPlatform.class)
  public void driverCreationShouldThrowNoPlatformExceptionForPlatformBrowserNotSet() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("newCommandTimeout", "300");
    driver = new WebDriverManager(caps).createDriverDetails().getDriver();
  }

  @Test()
  public void createWebDriverSuccessfullyWhenTestFeedIsPresent() {
    WebTestFeedParser testFeedParser = new WebTestFeedParser("chromeTestFeed");
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
    driver = new WebDriverManager(desiredCapabilities).createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test()
  public void createWebDriverSuccessfully() {
    DesiredCapabilities browserStackCapabilities = getWebBrowserStackCapabilities();
    driver = new WebDriverManager(browserStackCapabilities).createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
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
