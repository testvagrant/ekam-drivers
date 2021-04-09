package com.testvagrant.optimus.commons;

import java.nio.file.Paths;

public class AppFinder {
  // TODO: Add functions to read system property => Url or custom path

  private AppFinder() {}

  public static AppFinder getInstance() {
    return new AppFinder();
  }

  public String getDefaultPath(String appDir, String app) {
    return Paths.get(System.getProperty("user.dir"), appDir, app).toAbsolutePath().toString();
  }
}
