package com.testvagrant.optimus.core.model;

import com.google.gson.reflect.TypeToken;
import com.testvagrant.optimus.commons.filehandlers.GsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralCapabilities {
  private String automationName;
  private String platformName;
  private String platformVersion;
  private String deviceName;
  private String app;
  private String otherApps;
  private String browserName;
  private String newCommandTimeout;
  private String language;
  private String locale;
  private String udid;
  private String orientation;
  private Boolean autoWebview;
  private boolean noReset;
  private boolean fullReset;
  private boolean eventTimings;
  private boolean enablePerformanceLogging;
  private boolean printPageSourceOnFindFailure;
  private boolean clearSystemFiles;

  public Map<String, Object> toDesiredCapabilities() {
    GsonParser gsonParser = GsonParser.toInstance();
    String caps = gsonParser.serialize(this);
    return gsonParser.deserialize(caps, new TypeToken<Map<String, Object>>() {}.getType());
  }
}
