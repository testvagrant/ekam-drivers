package com.testvagrant.optimus.core.models.mobile;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppiumServerCapabilities {
  private String automationName;
  private String app;
  private String orientation;
  private String browserName;
  private String locale;
  private String udid;
  private boolean noReset;
  private int newCommandTimeout;
  private String platformVersion;
  private boolean autoWebview;
  private boolean fullReset;
  private String platformName;
  private String deviceName;
  private String language;
}
