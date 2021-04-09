package com.testvagrant.optimus.commons.exceptions;

public class NoSuchPlatformException extends RuntimeException {

  public NoSuchPlatformException(String platform) {
    super(
        String.format(
            "Platform %s does not exist. Platform can be either Android or IOS", platform));
  }
}
