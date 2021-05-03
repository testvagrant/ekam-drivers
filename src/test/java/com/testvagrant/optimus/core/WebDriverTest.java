package com.testvagrant.optimus.core;

import com.testvagrant.optimus.core.screenshots.OptimusRunContext;
import com.testvagrant.optimus.core.screenshots.OptimusRunTarget;
import com.testvagrant.optimus.core.web.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class WebDriverTest {

  @Test
  public void shouldCreateDriverSuccessfully() {
    System.setProperty("testFeed", "chromeTestFeed");
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test
  public void shouldCreateFirefoxDriverSuccessfully() {
    System.setProperty("testFeed", "firefoxTestFeed");
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();
    Assert.assertNotNull(driver);
  }

  @Test
  public void screenshotTest() throws InterruptedException {
    System.setProperty("testFeed", "chromeTestFeed");
    WebDriver driver = new WebDriverManager().createDriverDetails().getDriver();

    driver.get("http://www.google.com");
    OptimusRunContext optimusRunContext =
        OptimusRunContext.builder()
            .webDriver(driver)
            .build()
            .testPath("WebDriverTest", "screenshotTest");
    OptimusRunTarget optimusRunTarget = new OptimusRunTarget(optimusRunContext);
    optimusRunTarget.captureScreenshot();
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
