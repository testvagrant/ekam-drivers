package com.testvagrant.optimus.commons.exceptions;

public class DeviceEngagedException extends RuntimeException {
  public DeviceEngagedException() {
    super("Cannot find any available device for your request, please try after sometime");
  }
}
