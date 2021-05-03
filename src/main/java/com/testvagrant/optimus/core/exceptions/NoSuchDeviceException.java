package com.testvagrant.optimus.core.exceptions;

public class NoSuchDeviceException extends RuntimeException {
  public NoSuchDeviceException() {
    super(
        "Cannot find any device connected matching your request!! Please check your testFeed or DesiredCapabilities");
  }
}
