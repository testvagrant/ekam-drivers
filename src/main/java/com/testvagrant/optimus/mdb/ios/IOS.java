package com.testvagrant.optimus.mdb.ios;

import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.commons.entities.DeviceType;
import com.testvagrant.optimus.mdb.CommandExecutor;
import com.testvagrant.optimus.mdb.Mobile;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.testvagrant.optimus.mdb.ios.Commands.*;

public class IOS extends Mobile {

  @Override
  public List<DeviceDetails> getDevices() {
    return collectDeviceDetails();
  }

  private List<DeviceDetails> collectDeviceDetails() {
    if (!SystemUtils.IS_OS_MAC) return new ArrayList<>();
    List<String> allDevices = new CommandExecutor().exec(LIST_ALL_DEVICES).asList();
    List<String> simulators = findSimulators(allDevices);
    List<String> realDevices = findRealDevices(allDevices, simulators);
    simulators = filterMobileSimulators(simulators);
    List<DeviceDetails> deviceDetails = initDevices(realDevices, DeviceType.DEVICE);
    deviceDetails.addAll(initDevices(simulators, DeviceType.SIMULATOR));
    return deviceDetails;
  }

  private List<String> findRealDevices(List<String> devices, List<String> simulators) {
    List<String> realDevices = devices.stream().skip(2).collect(Collectors.toList());
    realDevices.removeAll(simulators);
    return realDevices;
  }

  private List<String> findSimulators(List<String> devices) {
    int simulatorHeaderIndex =
        IntStream.range(0, devices.size())
            .filter(counter -> devices.get(counter).contains(SIMULATORS_INSTRUMENTS_HEADER))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No Simulators found"));

    return devices.stream().skip(simulatorHeaderIndex).collect(Collectors.toList());
  }

  private List<String> filterMobileSimulators(List<String> simulators) {
    simulators =
        simulators.stream()
            .skip(1)
            .filter(device -> !device.isEmpty())
            .filter(device -> !device.toLowerCase().contains("apple tv"))
            .filter(device -> !device.toLowerCase().contains("apple watch series"))
            .filter(device -> !device.toLowerCase().contains("ipad"))
            .filter(device -> !device.toLowerCase().contains("ipod"))
            .collect(Collectors.toList());
    return simulators;
  }

  public List<DeviceDetails> initDevices(List<String> processLog, DeviceType deviceType) {
    List<DeviceDetails> devices = new ArrayList<>();
    for (String process : processLog) {
      DeviceDetails device = buildDevice(process, deviceType);
      devices.add(device);
    }

    return devices;
  }

  private DeviceDetails buildDevice(String line, DeviceType deviceType) {
    String udid = getUDID(line, deviceType); // TODO: Throw error when udid is not found
    String iosVersion = getIOSVersion(line);
    String deviceName = getDeviceName(line);
    return DeviceDetails.builder()
        .deviceName(deviceName)
        .platformVersion(iosVersion)
        .udid(udid)
        .platform(Platform.IOS)
        .runsOn(deviceType)
        .build();
  }

  private String getUDID(String line, DeviceType deviceType) {
    return extractPattern(
        line, deviceType.equals(DeviceType.DEVICE) ? DEVICE_UDID_PATTERN : SIMULATOR_UDID_PATTERN);
  }

  private String extractPattern(String line, String patternIdentifier) {
    Pattern pattern = Pattern.compile(patternIdentifier);
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
      return matcher.group(0);
    }
    throw new RuntimeException(
        String.format("No matching pattern found for regex: %s", patternIdentifier));
  }

  private String getIOSVersion(String line) {
    return extractPattern(line, OS_VERSION_PATTERN);
  }

  private String getDeviceName(String line) {
    int deviceNameLastValue = line.indexOf("(");
    return line.substring(0, deviceNameLastValue).trim();
  }
}