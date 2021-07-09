package com.testvagrant.ekam.drivers.models;

import lombok.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RemoteBrowserConfig {
  private URL url;

  private String browser;

  @Builder.Default private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

  @Builder.Default private Map<String, Object> experimentalOptions = new HashMap<>();
}
