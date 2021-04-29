package com.testvagrant.optimus.core.models.mobile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@Builder(toBuilder = true)
public class Device {
  private String id;
  private String platform;
  private String status;
  private String deviceName;
  private String runsOn;
  private String platformVersion;
  private String udid;
  private String buildId;
  private byte[] screenshot;

  @Override
  public String toString() {
    return "Device{"
        + "id='"
        + id
        + '\''
        + ", platform='"
        + platform
        + '\''
        + ", status='"
        + status
        + '\''
        + ", deviceName='"
        + deviceName
        + '\''
        + ", runsOn='"
        + runsOn
        + '\''
        + ", platformVersion='"
        + platformVersion
        + '\''
        + ", udid='"
        + udid
        + '\''
        + ", buildId='"
        + buildId
        + '\''
        + ", screenshot="
        + Arrays.toString(screenshot)
        + '}';
  }
}
