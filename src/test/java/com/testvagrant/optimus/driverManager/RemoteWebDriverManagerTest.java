package com.testvagrant.optimus.driverManager;

import com.testvagrant.optimus.core.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.web.WebDriverManager;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class RemoteWebDriverManagerTest {

  private RemoteWebDriver driver;

  public RemoteWebDriverManagerTest() {
    System.setProperty("runMode", "remote");
    System.setProperty("webFeed", "webTestFeed");
  }

  @AfterMethod
  public void tearDown() {
    System.out.println("Disposing driver");
    WebDriverManager.dispose();
  }

  @Test(expectedExceptions = UnsupportedPlatform.class, enabled = false)
  public void driverCreationShouldThrowNoPlatformExceptionForPlatformBrowserNotSet() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("newCommandTimeout", "300");
    driver = new WebDriverManager(caps).createDriverDetails().getDriver();
  }

  @Test(enabled = false)
  public void createWebDriverSuccessfullyWhenTestFeedIsPresent() {
    driver = new WebDriverManager().createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test(enabled = false)
  public void createWebDriverSuccessfully() {
    DesiredCapabilities browserStackCapabilities = getWebBrowserStackCapabilities();
    driver = new WebDriverManager(browserStackCapabilities).createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  private DesiredCapabilities getWebBrowserStackCapabilities() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("newCommandTimeout", "300");
    caps.setCapability(CapabilityType.BROWSER_NAME, "chrome");
    caps.setCapability(CapabilityType.BROWSER_VERSION, "latest");
    return caps;
  }
}
