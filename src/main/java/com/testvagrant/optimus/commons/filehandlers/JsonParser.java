package com.testvagrant.optimus.commons.filehandlers;

import com.testvagrant.optimus.commons.SystemProperties;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.testvagrant.optimus.commons.filehandlers.ResourcePaths.*;

public class JsonParser {

  public static final String[] validPaths =  new String[]{
          TEST_RESOURCES,
          TEST_TESTFEED_RESOURCES,
          MAIN_RESOURCES,
          MAIN_TESTFEED_RESOURCES};

  public <T> T deserialize(String name, Class<T> tClass) {
    try {
      AtomicReference<String> testFeed = new AtomicReference<>("");
      for(String path: validPaths) {
        File file = FileFinder.fileFinder(path).find(name, FileExtension.JSON);
        if(file != null && file.exists()) {
          testFeed.set(file.getPath());
          break;
        }
      }
      if(testFeed.get().isEmpty()) {
        throw new RuntimeException("Cannot find file "+name);
      }
      return GsonParser.toInstance().deserialize(new FileReader(testFeed.get()), tClass);
    } catch (Exception ex) {
      throw new RuntimeException("Unable to parse Json. Error: " + ex.getMessage());
    }
  }

}
