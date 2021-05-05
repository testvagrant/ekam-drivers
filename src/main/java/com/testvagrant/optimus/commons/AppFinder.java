package com.testvagrant.optimus.commons;

import com.testvagrant.optimus.core.exceptions.AppNotFoundException;

import java.io.File;
import java.nio.file.Paths;

public class AppFinder {

  private AppFinder() {}

  public static AppFinder getInstance() {
    return new AppFinder();
  }

  public String getDefaultPath(String appDir, String app) {
    String appPath = String.format("%s/%s/%s", SystemProperties.USER_DIR, appDir, app);
    if (!new File(appPath).exists()) {
      throw new AppNotFoundException(appPath);
    }

    return Paths.get(appPath).toAbsolutePath().toString();
  }
}
