package com.testvagrant.optimus.core.remote;

import com.testvagrant.optimus.core.models.CloudConfig;

public class CloudConfigBuilder {

  private final String userName;
  private final String accessKey;
  private final CloudConfig cloudConfig;

  public CloudConfigBuilder() {
    userName = System.getenv("username");
    accessKey = System.getenv("accessKey");
    cloudConfig = new ConfigLoader().loadConfig();
    overrideCloudConfig(cloudConfig);
    overrideHub(cloudConfig);
  }

  private void overrideHub(CloudConfig cloudConfig) {
    if (cloudConfig.getHub() == null) {
      cloudConfig.setHub(System.getProperty("hub", "browserstack"));
    }
  }

  public CloudConfig build() {
    return cloudConfig;
  }

  private void overrideCloudConfig(CloudConfig cloudConfig) {
    if (userName != null) {
      cloudConfig.setUsername(userName);
    }
    if (accessKey != null) {
      cloudConfig.setAccessKey(accessKey);
    }
  }
}
