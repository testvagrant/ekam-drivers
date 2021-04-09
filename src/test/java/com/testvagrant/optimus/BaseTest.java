package com.testvagrant.optimus;

import com.testvagrant.optimus.devicemanager.AndroidDeviceManager;
import com.testvagrant.optimus.devicemanager.DeviceManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Guice;

@Guice()
public class BaseTest {
  public BaseTest() {
    System.setProperty("testFeed", "sampleTestFeed");
  }
}
