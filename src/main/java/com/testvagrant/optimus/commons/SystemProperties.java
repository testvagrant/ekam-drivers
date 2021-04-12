package com.testvagrant.optimus.commons;

public class SystemProperties {
  public static final String USER_DIR = System.getProperty("user.dir");
  public static final String TEST_FEED = System.getProperty("testFeed", "sampleTestFeed");
}
