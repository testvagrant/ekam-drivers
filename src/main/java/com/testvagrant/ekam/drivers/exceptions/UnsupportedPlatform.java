package com.testvagrant.ekam.drivers.exceptions;

public class UnsupportedPlatform extends RuntimeException {
  public UnsupportedPlatform() {
    super(String.format("%s/%s is mandatory", "platformName", "browserName"));
  }
}
