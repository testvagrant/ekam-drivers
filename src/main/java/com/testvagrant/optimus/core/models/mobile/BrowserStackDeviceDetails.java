package com.testvagrant.optimus.core.models.mobile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowserStackDeviceDetails {
  private String os;
  private String os_version;
  private String device;
  private boolean realMobile;

  @Override
  public String toString() {
    return "{"
        + "\"os\":"
        + os
        + ", \"os_version\":"
        + os_version
        + ", \"device\":"
        + device
        + ", \"realMobile\":"
        + realMobile
        + "}}";
  }
}
