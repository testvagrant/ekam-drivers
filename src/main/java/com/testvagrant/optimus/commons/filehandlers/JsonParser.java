package com.testvagrant.optimus.commons.filehandlers;

import java.io.FileReader;
import java.util.Objects;

public class JsonParser {

  public <T> T deserialize(String name, Class<T> tClass) {
    try {
      String testFeed =
          Objects.requireNonNull(
                  this.getClass().getClassLoader().getResource(String.format("%s.json", name)))
              .getPath();

      return GsonParser.toInstance().deserialize(new FileReader(testFeed), tClass);
    } catch (Exception ex) {
      throw new RuntimeException("Unable to parse Json" + ex.getMessage());
    }
  }
}
