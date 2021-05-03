package com.testvagrant.optimus.core.remote;

import com.testvagrant.optimus.core.models.CloudConfig;

import java.net.URL;

public class RemoteUrlBuilder {

  public static URL build(CloudConfig cloudConfig) {
    try {
      String urlString =
          String.format(
              "https://%s:%s@%s/wd/hub",
              cloudConfig.getUsername(), cloudConfig.getAccessKey(), cloudConfig.getHub());
      return new URL(urlString);
    } catch (Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }
}
