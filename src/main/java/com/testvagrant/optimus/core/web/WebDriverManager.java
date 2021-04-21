package com.testvagrant.optimus.core.web;

import com.testvagrant.optimus.core.parser.WebTestFeedParser;
import org.openqa.selenium.WebDriver;

public class WebDriverManager {

  private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
  private final WebTestFeedParser testFeedParser;

  public WebDriverManager() {
    testFeedParser = new WebTestFeedParser(System.getProperty("testFeed"));
  }

  public WebDriver createDriver() {
    String browser = testFeedParser.getBrowserName().trim().toLowerCase();
    WebDriver webDriver =
        browser.equals("firefox")
            ? new FirefoxDriverManager(testFeedParser).createDriver()
            : new ChromeDriverManager(testFeedParser).createDriver();

    driverThreadLocal.set(webDriver);
    return driverThreadLocal.get();
  }

  public static void dispose() {
    if (driverThreadLocal.get() != null) {
      driverThreadLocal.get().quit();
    }
  }
}
