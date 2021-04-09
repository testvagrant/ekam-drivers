package com.testvagrant.optimus.core.model;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AndroidOnlyCapabilities extends GeneralCapabilities {
  private String appActivity;
  private String appPackage;
  private String appWaitActivity;
  private String appWaitPackage;
  private int appWaitDuration;
  private int deviceReadyTimeout;
  private boolean allowTestPackages;
  private String androidCoverage;
  private String androidCoverageEndIntent;
  private int androidDeviceReadyTimeout;
  private int androidInstallTimeout;
  private String androidInstallPath;
  private int adbPort;
  private int systemPort;
  private String remoteAdbHost;
  private String androidDeviceSocket;
  private String avd;
  private int avdLaunchTimeout;
  private String avdReadyTimeout;
  private String avdArgs;
  private boolean useKeystore;
  private String keystorePath;
  private String keystorePassword;
  private String keyAlias;
  private String keyPassword;
  private String chromedriverExecutable;
  private boolean nativeWebScreenshot;
  private boolean noSign;
  private boolean disableAndroidWatchers;
  private boolean dontStopAppOnReset;
  private boolean resetKeyboard;
  private boolean recreateChromeDriverSessions;
  private String intentFlags;
  private String chromeOptions;
  private String optionalIntentArguments;
  private String intentCategory;
  private int autoWebviewTimeout;
  private String androidScreenshotPath;
  private String intentAction;
  private boolean ignoreUnimportantViews;
  private boolean unicodeKeyboard;
  private boolean autoGrantPermissions;
  private boolean autoAcceptAlerts;
}
