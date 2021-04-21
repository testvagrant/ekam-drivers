package com.testvagrant.optimus.commons.filehandlers;

import java.io.File;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicReference;

import static com.testvagrant.optimus.commons.filehandlers.ResourcePaths.*;

public class JsonParser {

  public static final String[] validPaths =
      new String[] {
        TEST_RESOURCES, TEST_TEST_FEED_RESOURCES, MAIN_RESOURCES, MAIN_TEST_FEED_RESOURCES
      };

  public <T> T deserialize(String name, Class<T> tClass) {
    try {
      AtomicReference<String> testFeed = new AtomicReference<>("");
      for (String path : validPaths) {
        File file = FileFinder.fileFinder(path).find(name, ".json");
        if (file != null && file.exists()) {
          testFeed.set(file.getPath());
          break;
        }
      }
      if (testFeed.get().isEmpty()) {
        throw new RuntimeException("Cannot find file " + name);
      }
      return GsonParser.toInstance().deserialize(new FileReader(testFeed.get()), tClass);
    } catch (Exception ex) {
      throw new RuntimeException("Unable to parse Json. Error: " + ex.getMessage());
    }
  }
}
