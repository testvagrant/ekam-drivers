package com.testvagrant.optimus.mdb.ios;

public interface Commands {
  String DEVICES_INSTRUMENTS_HEADER = "Devices";
  String SIMULATORS_INSTRUMENTS_HEADER = "== Simulators ==";
  String LIST_ALL_DEVICES = "xcrun xctrace list devices";
  String SIMULATOR_UDID_PATTERN =
      "[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}";
  String OS_VERSION_PATTERN = "[0-9]{1,2}\\.[0-9]{1,2}";
  String DEVICE_UDID_PATTERN = "[a-zA-Z0-9-]{25,40}";
  String XCODE_INSTALLATION = "xcode-select -p";
  String XCODE_INSTALLATION_DETAILS = "gcc --version";
  String XCODE_VERSION = "([A-z])\\w+ ([A-Z])\\w+ ([a-z])\\w+ ([0-9])(.)([0-9])(.)([0-9]) (.*)";
  String XCODE_VERSION_REGEX = "([0-9])(.)([0-9])(.)([0-9])";
}
