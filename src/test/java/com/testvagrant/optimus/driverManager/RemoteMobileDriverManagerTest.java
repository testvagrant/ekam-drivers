package com.testvagrant.optimus.driverManager;

import com.testvagrant.optimus.core.mobile.MobileDriverManager;
import com.testvagrant.optimus.core.parser.MobileTestFeedParser;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class RemoteMobileDriverManagerTest {

  private RemoteWebDriver driver;

  public RemoteMobileDriverManagerTest() {
    System.setProperty("runMode", "remote");
    System.setProperty("mobileFeed", "browserStackTestFeed");
  }

  @AfterMethod
  public void tearDown() {
    System.out.println("Disposing driver");
    MobileDriverManager.dispose();
  }

  @Test(enabled = true)
  public void createMobileDriverSuccessfully() {
    DesiredCapabilities browserStackCapabilities = getMobileBrowserStackCapabilities();
    driver = new MobileDriverManager(browserStackCapabilities).createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test(enabled = true)
  public void createMobileDriverSuccessfullyWhenTestFeedIsPresent() {
    MobileTestFeedParser mobileTestFeedParser = new MobileTestFeedParser("browserStackTestFeed");
    DesiredCapabilities desiredCapabilities = mobileTestFeedParser.getDesiredCapabilities();
    driver = new MobileDriverManager(desiredCapabilities).createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  private DesiredCapabilities getMobileBrowserStackCapabilities() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("app", "bs://2a415535fc457368f4ac133f1b7b27551b90c98f");
    caps.setCapability("autoGrantPermissions", "true");
    caps.setCapability("newCommandTimeout", "300");
    caps.setCapability("platform", "android");
    caps.setCapability(CapabilityType.PLATFORM_NAME, "android");
    return caps;
  }
}
