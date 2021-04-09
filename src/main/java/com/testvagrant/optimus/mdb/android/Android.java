package com.testvagrant.optimus.mdb.android;

import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.commons.entities.DeviceType;
import com.testvagrant.optimus.mdb.CommandExecutor;
import com.testvagrant.optimus.mdb.Mobile;
import org.openqa.selenium.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.testvagrant.optimus.mdb.android.Commands.*;

public class Android extends Mobile {

  private final CommandExecutor commandExecutor;

  public Android() {
    commandExecutor = new CommandExecutor();
  }

  @Override
  public List<DeviceDetails> getDevices() {
    return collectDeviceDetails();
  }

  private List<DeviceDetails> collectDeviceDetails() {
    List<String> devices = commandExecutor.exec(LIST_ALL_DEVICES).asList();

    List<String> collectedDevices =
        devices.parallelStream()
            .filter(line -> !(line.equals(ADB_HEADER)))
            .collect(Collectors.toList());

    if (collectedDevices.size() == 0) {
      throw new RuntimeException("Could not find any devices, are any devices available?");
    }

    return initDevices(collectedDevices);
  }

  private boolean isRealDevice(String process) {
    return !process.contains("vbox")
        && !process.startsWith("emulator")
        && !process.startsWith("* daemon");
  }

  private List<DeviceDetails> initDevices(List<String> processLog) {
    List<DeviceDetails> devices = new ArrayList<>();
    for (String process : processLog) {
      DeviceType deviceType = isRealDevice(process) ? DeviceType.DEVICE : DeviceType.EMULATOR;
      DeviceDetails device = buildDeviceDetails(process, deviceType);
      devices.add(device);
    }
    return devices;
  }

  private DeviceDetails buildDeviceDetails(String process, DeviceType devicetype) {
    String udid = getUDID(process);
    String model = getModel(udid);
    String osVersion = getOSVersion(udid);
    return DeviceDetails.builder()
        .udid(udid)
        .deviceName(model)
        .platform(Platform.ANDROID)
        .platformVersion(osVersion)
        .runsOn(devicetype)
        .build();
  }

  private String getUDID(String process) {
    String uid;
    int uidLastChar = process.indexOf(" ");
    uid = process.substring(0, uidLastChar);
    return uid;
  }

  private String getModel(String UID) {
    String command = String.format(GET_DEVICE_MODEL, UID);
    return commandExecutor.exec(command).asLine().replace("\n", "");
  }

  private String getOSVersion(String UID) {
    String command = String.format(GET_DEVICE_OS, UID);
    return commandExecutor.exec(command).asLine().replace("\n", "");
  }
}
