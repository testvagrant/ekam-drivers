package com.testvagrant.optimus.core.model;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class IOSOnlyCapabilities extends GeneralCapabilities {
  private String waitForAppScript;
  private int interKeyDelay;
  private String safariInitialUrl;
  private String localizableStringsDir;
  private boolean keepKeyChains;
  private String calendarFormat;
  private int screenshotWaitTimeout;
  private boolean nativeWebTap;
  private int launchTimeout;
  private boolean autoDismissAlerts;
  private boolean safariOpenLinksInBackground;
  private String sendKeyStrategy;
  private boolean safariAllowPopups;
  private String udid;
  private boolean locationServicesAuthorized;
  private String appName;
  private boolean safariIgnoreFraudWarning;
  private boolean autoAcceptAlerts;
  private boolean showIOSLog;
  private String processArguments;
  private boolean nativeInstrumentsLib;
  private String bundleId;
  private int webviewConnectRetries;
  private boolean locationServicesEnabled;
}
