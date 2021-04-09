package com.testvagrant.optimus.core.appium;

import io.appium.java_client.service.local.flags.ServerArgument;

public enum OptimusServerFlag implements ServerArgument {
  WDA_PORT("--webdriveragent-port"),
  /**
   * This will print the appium server logs to stdout
   */
  ENABLE_CONSOLE_LOGS("--enable-console-logs");

  private String arg;

  OptimusServerFlag(String arg) {
    this.arg = arg;
  }

  @Override
  public String getArgument() {
    return arg;
  }
}
