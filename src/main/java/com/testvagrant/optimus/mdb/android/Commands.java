package com.testvagrant.optimus.mdb.android;

public interface Commands {
  String ADB_HEADER = "List of devices attached";
  String LIST_ALL_DEVICES = "adb devices -l";
  String GET_DEVICE_MODEL = "adb -s %s shell getprop ro.product.model";
  String GET_DEVICE_OS = "adb -s %s shell getprop ro.build.version.release";
}
