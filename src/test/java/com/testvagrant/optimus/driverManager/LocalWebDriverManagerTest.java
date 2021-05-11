package com.testvagrant.optimus.driverManager;

import com.testvagrant.optimus.core.screenshots.OptimusRunContext;
import com.testvagrant.optimus.core.screenshots.OptimusRunTarget;
import com.testvagrant.optimus.core.web.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class LocalWebDriverManagerTest {

  public LocalWebDriverManagerTest() {
    System.setProperty("webFeed", "webTestFeed");
  }

  @Test
  public void shouldCreateDriverSuccessfully() {
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test
  public void shouldCreateFirefoxDriverSuccessfully() {
    System.setProperty("browser", "firefox");
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test
  public void shouldCreateDriverSuccessfullyAndRunInHeadless() {
    System.setProperty("headless", "true");
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
    driver.get("http://www.google.com");
  }

  @Test
  public void shouldCreateFirefoxDriverSuccessfullyAndRunInHeadless() {
    System.setProperty("headless", "true");
    System.setProperty("browser", "firefox");
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test
  public void screenshotTest() throws InterruptedException {
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();

    driver.get("http://www.google.com");
    OptimusRunContext optimusRunContext =
        OptimusRunContext.builder()
            .webDriver(driver)
            .build()
            .testPath("WebDriverTest", "screenshotTest");

    OptimusRunTarget optimusRunTarget = new OptimusRunTarget(optimusRunContext);
    optimusRunTarget.captureWebScreenshot();
    Thread.sleep(2000);

    driver.findElement(By.name("q")).sendKeys("Hello taking a screenshot");
    optimusRunTarget.captureScreenshot();
    optimusRunTarget.captureLogs();
  }

  @AfterMethod
  public void dispose() {
    WebDriverManager.dispose();
  }
}
