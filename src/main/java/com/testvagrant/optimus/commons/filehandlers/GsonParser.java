package com.testvagrant.optimus.commons.filehandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

public class GsonParser {

  private final Gson gson;

  private GsonParser() {
    this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
  }

  public static GsonParser toInstance() {
    return new GsonParser();
  }

  public <T> String serialize(T object) {
    return gson.toJson(object);
  }

  public <T> T deserialize(FileReader reader, Class<T> tClass) {
    return gson.fromJson(reader, tClass);
  }

  public <T> T deserialize(String json, Class<T> tClass) {
    return gson.fromJson(json, tClass);
  }

  public <T> T deserialize(InputStreamReader streamReader, Class<T> tClass) {
    return gson.fromJson(streamReader, tClass);
  }

  public <T> T deserialize(JsonElement jsonElement, Class<T> tClass) {
    return gson.fromJson(jsonElement, tClass);
  }

  public Map<String, Object> deserialize(String json, Type tType) {
    return gson.fromJson(json, tType);
  }
}
