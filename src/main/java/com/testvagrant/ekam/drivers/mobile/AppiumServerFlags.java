package com.testvagrant.ekam.drivers.mobile;

import io.appium.java_client.service.local.flags.ServerArgument;

public enum AppiumServerFlags implements ServerArgument {
  WDA_PORT("--driver-xcuitest-webdriveragent-port"),
  /** This will print the appium server logs to stdout */
  ENABLE_CONSOLE_LOGS("--enable-console-logs"),
  BASE_PATH("--base-path"),
  WD_HUB("/wd/hub");

  private final String arg;

  AppiumServerFlags(String arg) {
    this.arg = arg;
  }

  @Override
  public String getArgument() {
    return arg;
  }
}
