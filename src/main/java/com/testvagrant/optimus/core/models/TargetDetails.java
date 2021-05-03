package com.testvagrant.optimus.core.models;

import com.testvagrant.optimus.core.models.mobile.DeviceType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class TargetDetails implements Serializable {
  private String name;
  private OptimusSupportedPlatforms platform;
  private String platformVersion;
  private DeviceType runsOn;
  private String udid;

  @Override
  public String toString() {
    return "{"
        + "\"deviceName\":\""
        + name
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
