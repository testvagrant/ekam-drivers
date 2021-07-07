package com.testvagrant.ekam.drivers.models;

import lombok.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RemoteBrowserConfig {
  private URL url;
  private String browser;
  private DesiredCapabilities desiredCapabilities;
}
