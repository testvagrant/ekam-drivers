package com.testvagrant.optimus.core.remote;

import com.testvagrant.optimus.commons.filehandlers.GsonParser;
import com.testvagrant.optimus.core.models.CloudConfig;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigLoader {

  public CloudConfig loadConfig() {
    String file = String.format("cloudConfig/%s.json", System.getProperty("hub", "browserstack"));
    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(file);
    if (resourceAsStream == null) return new CloudConfig();
    return GsonParser.toInstance()
        .deserialize(new InputStreamReader(resourceAsStream), CloudConfig.class);
  }
}
