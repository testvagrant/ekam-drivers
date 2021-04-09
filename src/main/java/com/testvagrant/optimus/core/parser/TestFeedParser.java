package com.testvagrant.optimus.core.parser;

import com.testvagrant.optimus.commons.AppFinder;
import com.testvagrant.optimus.commons.PortGenerator;
import com.testvagrant.optimus.commons.filehandlers.GsonParser;
import com.testvagrant.optimus.commons.filehandlers.JsonParser;
import com.testvagrant.optimus.core.appium.OptimusServerFlag;
import com.testvagrant.optimus.core.model.AndroidOnlyCapabilities;
import com.testvagrant.optimus.core.model.GeneralCapabilities;
import com.testvagrant.optimus.core.model.IOSOnlyCapabilities;
import com.testvagrant.optimus.core.model.TestFeed;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.IOSServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.*;

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
    return addServerArguments(GeneralServerFlag.values(),
            IOSServerFlag.values(),
            AndroidServerFlag.values(),
            OptimusServerFlag.values());
  }

  //TODO: Improve the logic
  private Map<ServerArgument, String> addServerArguments(ServerArgument[]... values) {
    Map<ServerArgument, String> serverArgumentsMap = new HashMap<>();
    testFeed.getServerArguments().parallelStream().forEach( arg -> {
      Arrays.stream(values).parallel().forEach(serverFlags -> {
        String[] serverArg = arg.contains("=")? arg.trim().split("="): arg.trim().split(" ");
        Optional<ServerArgument> first = Arrays.stream(serverFlags).filter(value -> value.getArgument().equals(serverArg[0])).findFirst();
        first.ifPresent(iosServerFlag -> serverArgumentsMap.put(iosServerFlag, serverArg.length == 2 ? serverArg[1] : ""));
      });
    });
    return serverArgumentsMap;
  }

  public DesiredCapabilities getDesiredCapabilities() {
    Map<String, Object> generalCapabilities =
        getGeneralDesiredCapabilities().toDesiredCapabilities();
    Map<String, Object> mergedCapabilities = mergeDesiredCapabilities(generalCapabilities);
    Map<String, Object> desiredCapabilitiesMap = updateMandatoryDesiredCaps(mergedCapabilities);
    return new DesiredCapabilities(desiredCapabilitiesMap);
  }

  private <T extends GeneralCapabilities> T getGeneralDesiredCapabilities() {
    GsonParser gsonParser = GsonParser.toInstance();
    String capabilities = gsonParser.serialize(testFeed.getDesiredCapabilities());
    switch (getPlatform()) {
      case ANDROID:
        return (T) gsonParser.deserialize(capabilities, AndroidOnlyCapabilities.class);
      case IOS:
        return (T) gsonParser.deserialize(capabilities, IOSOnlyCapabilities.class);
      default:
        throw new RuntimeException("Cannot create general capabilities");
    }
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
    return new JsonParser().deserialize(testFeedName, TestFeed.class);
  }
}
