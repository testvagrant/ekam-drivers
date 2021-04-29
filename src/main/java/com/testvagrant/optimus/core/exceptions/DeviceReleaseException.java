package com.testvagrant.optimus.core.exceptions;

public class DeviceReleaseException extends RuntimeException {
  public DeviceReleaseException(String udid, String message) {
    super(String.format("Cannot release device with udid %s. \nError: %s", udid, message));
  }
}
