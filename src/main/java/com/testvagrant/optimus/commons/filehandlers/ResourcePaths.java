package com.testvagrant.optimus.commons.filehandlers;

import com.testvagrant.optimus.commons.SystemProperties;

import java.nio.file.Paths;

public class ResourcePaths {

  public static final String SRC_ROOT = Paths.get(SystemProperties.USER_DIR, "src").toString();
  public static final String TEST_RESOURCES = Paths.get(SRC_ROOT, "test", "resources").toString();
  public static final String TEST_TEST_FEED_RESOURCES =
      Paths.get(TEST_RESOURCES, "testFeed").toString();
  public static final String MAIN_RESOURCES = Paths.get(SRC_ROOT, "main", "resources").toString();
  public static final String MAIN_TEST_FEED_RESOURCES =
      Paths.get(MAIN_RESOURCES, "testFeed").toString();
}
