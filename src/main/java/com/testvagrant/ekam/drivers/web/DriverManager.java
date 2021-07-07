package com.testvagrant.ekam.drivers.web;

import org.openqa.selenium.WebDriver;

abstract class DriverManager {

  protected static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

  public static void dispose() {
    if (driverThreadLocal.get() != null) {
      driverThreadLocal.get().quit();
    }
  }

  public WebDriver launchDriver() {
    driverThreadLocal.set(createDriver());
    return driverThreadLocal.get();
  }

  protected abstract WebDriver createDriver();
}
