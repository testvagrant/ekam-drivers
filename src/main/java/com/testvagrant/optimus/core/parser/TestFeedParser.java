package com.testvagrant.optimus.core.parser;

import com.testvagrant.optimus.commons.AppFinder;
import com.testvagrant.optimus.commons.PortGenerator;
import com.testvagrant.optimus.commons.SystemProperties;
import com.testvagrant.optimus.commons.filehandlers.FileExtension;
import com.testvagrant.optimus.commons.filehandlers.FileFinder;
import com.testvagrant.optimus.commons.filehandlers.GsonParser;
import com.testvagrant.optimus.commons.filehandlers.JsonParser;
import com.testvagrant.optimus.core.appium.OptimusServerFlag;
import com.testvagrant.optimus.core.model.*;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.IOSServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestFeedParser {
  private final TestFeed testFeed;

  public TestFeedParser(String testFeedName) {
    testFeed = getTestFeed(testFeedName);
  }

  public Platform getPlatform() {
    try {
      return Platform.valueOf(testFeed.getPlatform().toUpperCase().trim());
    } catch (IllegalArgumentException e) {
      return Platform.ANDROID;
    }
  }

  public Map<ServerArgument, String> getServerArgumentsMap() {
    return addServerArguments(
        GeneralServerFlag.values(),
        IOSServerFlag.values(),
        AndroidServerFlag.values(),
        OptimusServerFlag.values());
  }

  private Map<ServerArgument, String> addServerArguments(ServerArgument[]... values) {
    Map<ServerArgument, String> serverArgumentsMap = new HashMap<>();

    Map<String, ServerArgument> serverFlags =
        Arrays.stream(values)
            .flatMap(Arrays::stream)
            .collect(Collectors.toMap(ServerArgument::getArgument, item -> item));

    testFeed.getServerArguments().parallelStream()
        .forEach(
            arg -> {
              String[] serverArg =
                  arg.contains("=") ? arg.trim().split("=") : arg.trim().split(" ");

              if (serverFlags.containsKey(serverArg[0])) {
                ServerArgument argument = serverFlags.get(serverArg[0]);
                String value = serverArg.length == 2 ? serverArg[1] : "";
                serverArgumentsMap.put(argument, value);
              }
            });

    return serverArgumentsMap;
  }

  public DesiredCapabilities getDesiredCapabilities() {
    Map<String, Object> generalCapabilities =
        getPlatform().equals(Platform.IOS)
            ? getIOSCapabilities().toDesiredCapabilities()
            : getAndroidCapabilities().toDesiredCapabilities();

    Map<String, Object> mergedCapabilities = mergeDesiredCapabilities(generalCapabilities);
    Map<String, Object> desiredCapabilitiesMap = updateMandatoryDesiredCaps(mergedCapabilities);
    return new DesiredCapabilities(desiredCapabilitiesMap);
  }


  public DeviceFilters getDeviceFilters() {
    return testFeed.getDeviceFilters();
  }
  private AndroidOnlyCapabilities getAndroidCapabilities() {
    GsonParser gsonParser = GsonParser.toInstance();
    String capabilities = gsonParser.serialize(testFeed.getDesiredCapabilities());
    return gsonParser.deserialize(capabilities, AndroidOnlyCapabilities.class);
  }

  private IOSOnlyCapabilities getIOSCapabilities() {
    GsonParser gsonParser = GsonParser.toInstance();
    String capabilities = gsonParser.serialize(testFeed.getDesiredCapabilities());
    return gsonParser.deserialize(capabilities, IOSOnlyCapabilities.class);
  }

  private Map<String, Object> mergeDesiredCapabilities(Map<String, Object> desiredCapabilitiesMap) {
    Map<String, Object> testFeedDesiredCapabilitiesMap = testFeed.getDesiredCapabilities();
    testFeedDesiredCapabilitiesMap.entrySet().parallelStream()
        .forEach(
            entry -> {
              if (!desiredCapabilitiesMap.containsKey(entry.getKey())) {
                desiredCapabilitiesMap.put(entry.getKey(), entry.getValue());
              }
            });
    return desiredCapabilitiesMap;
  }

  private Map<String, Object> updateMandatoryDesiredCaps(
      Map<String, Object> desiredCapabilitiesMap) {
    desiredCapabilitiesMap.put("platformName", getPlatform().name());
    desiredCapabilitiesMap.put(
        "app", AppFinder.getInstance().getDefaultPath(testFeed.getAppDir(), testFeed.getApp()));
    if (getPlatform().equals(Platform.ANDROID)) {
      desiredCapabilitiesMap.put("systemPort", PortGenerator.aRandomOpenPortOnAllLocalInterfaces());
    }
    return desiredCapabilitiesMap;
  }

  private TestFeed getTestFeed(String testFeedName) {
    JsonParser jsonParser = new JsonParser();
    return jsonParser.deserialize(testFeedName, TestFeed.class);
  }
}
