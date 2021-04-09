package com.testvagrant.optimus.core.model;

import lombok.*;
import org.openqa.selenium.Platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TestFeed {
  private String appDir = "app";
  private String app = "";
  private String platform = Platform.ANDROID.name();
  private Map<String, Object> desiredCapabilities = new HashMap<>();
  private List<String> serverArguments =  new ArrayList<>();
}
