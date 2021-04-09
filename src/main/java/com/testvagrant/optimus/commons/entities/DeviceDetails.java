package com.testvagrant.optimus.commons.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Platform;

import java.io.Serializable;

@Getter
@Setter
@Builder(toBuilder = true)
public class DeviceDetails implements Serializable {
  private String deviceName;
  private Platform platform;
  private String platformVersion;
  private DeviceType runsOn;
  private String udid;

  @Override
  public String toString() {
    return "{"
        + "\"deviceName\":\""
        + deviceName
        + "\""
        + ", \"platform\":\""
        + platform
        + "\""
        + ", \"platformVersion\":\""
        + platformVersion
        + "\""
        + ", \"runsOn\":\""
        + runsOn
        + "\""
        + ", \"udid\":\""
        + udid
        + "\""
        + "}}";
  }
}
