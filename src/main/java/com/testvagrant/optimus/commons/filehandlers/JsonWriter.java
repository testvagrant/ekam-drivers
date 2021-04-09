package com.testvagrant.optimus.commons.filehandlers;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {

  public <T> void writeJson(T json, String filePath) {
    try {
      new Gson().toJson(json, new FileWriter(filePath));
    } catch (IOException exception) {
      throw new RuntimeException(exception.getMessage());
    }
  }
}
