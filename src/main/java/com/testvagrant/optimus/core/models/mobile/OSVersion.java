package com.testvagrant.optimus.core.models.mobile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class OSVersion {
  private String version;
  private String name;
  private double baseVersion;

  @Override
  public String toString() {
    return "OSVersion{"
        + "version='"
        + version
        + '\''
        + ", name='"
        + name
        + '\''
        + ", baseVersion="
        + baseVersion
        + '}';
  }
}
