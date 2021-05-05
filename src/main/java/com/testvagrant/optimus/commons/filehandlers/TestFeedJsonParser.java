package com.testvagrant.optimus.commons.filehandlers;

import com.testvagrant.optimus.core.exceptions.TestFeedNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicReference;

import static com.testvagrant.optimus.commons.filehandlers.ResourcePaths.*;

public class TestFeedJsonParser {

  public static final String[] validPaths =
      new String[] {
        TEST_RESOURCES, TEST_TEST_FEED_RESOURCES, MAIN_RESOURCES, MAIN_TEST_FEED_RESOURCES
      };

  public <T> T deserialize(String name, Class<T> tClass) {
    try {
      AtomicReference<String> filePath = new AtomicReference<>("");

      for (String path : validPaths) {
        File file = FileFinder.fileFinder(path).find(name, ".json");
        if (file != null && file.exists()) {
          filePath.set(file.getPath());
          break;
        }
      }
      return GsonParser.toInstance().deserialize(new FileReader(filePath.get()), tClass);
    } catch (FileNotFoundException e) {
      throw new TestFeedNotFoundException(name, validPaths);
    }
  }
}
