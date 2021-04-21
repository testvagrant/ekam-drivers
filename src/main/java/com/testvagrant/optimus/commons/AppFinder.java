package com.testvagrant.optimus.commons;

import java.nio.file.Paths;

public class AppFinder {

  private AppFinder() {}

  public static AppFinder getInstance() {
    return new AppFinder();
  }

  public String getDefaultPath(String appDir, String app) {
    return Paths.get(SystemProperties.USER_DIR, appDir, app).toAbsolutePath().toString();
  }
}
