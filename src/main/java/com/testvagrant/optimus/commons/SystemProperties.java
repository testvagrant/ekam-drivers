package com.testvagrant.optimus.commons;

public class SystemProperties {
  public static final String USER_DIR = System.getProperty("user.dir");
  public static final String TARGET = System.getProperty("target", "android").toUpperCase();
  public static final String BROWSER = System.getProperty("browser", "chrome");
  public static final String RUN_MODE = System.getProperty("runMode");
  public static final String MOBILE_FEED = System.getProperty("mobileFeed");
  public static final String WEB_FEED = System.getProperty("webFeed");
  public static final boolean HEADLESS =
      Boolean.parseBoolean(System.getProperty("headless", "false"));
  public static final String HUB = System.getProperty("hub", "browserstack");
}
