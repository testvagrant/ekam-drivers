package com.testvagrant.optimus.core;

import com.testvagrant.optimus.core.web.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class WebDriverTest {

  @Test
  public void shouldCreateDriverSuccessfully() {
    System.setProperty("testFeed", "chromeTestFeed");
    WebDriver driver = new WebDriverManager().createDriver();
    Assert.assertNotNull(driver);
  }

  @Test
  public void shouldCreateFirefoxDriverSuccessfully() {
    System.setProperty("testFeed", "firefoxTestFeed");
    WebDriver driver = new WebDriverManager().createDriver();
    Assert.assertNotNull(driver);
  }

  @AfterMethod
  public void dispose() {
    WebDriverManager.dispose();
  }
}
