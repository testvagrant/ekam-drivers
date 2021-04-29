package com.testvagrant.optimus.commons.entities;

import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(toBuilder = true)
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
