package com.testvagrant.optimus.core;

import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.core.appium.DriverManager;
import com.testvagrant.optimus.core.model.MobileDriverDetails;
import com.testvagrant.optimus.devicemanager.AndroidDeviceManager;
import com.testvagrant.optimus.devicemanager.DeviceManager;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DriverManagerTest extends BaseTest {

  private DeviceManager deviceManager;

  @BeforeSuite
  public void init() {
    deviceManager = new AndroidDeviceManager();
  }

  @Test
  public void androidDriverTest() {
    createAndDisposeDriver();
  }

  @Test //TODO: Check why ExecutorService doesn't throw exception?
  public void parallelAndroidDriverTest() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    executor.submit(this::createAndDisposeDriver);
    executor.submit(this::createAndDisposeDriver);
    executor.awaitTermination(30, TimeUnit.SECONDS);
  }

  private void createAndDisposeDriver() {
    DriverManager driverManager = new DriverManager(deviceManager);
    MobileDriverDetails driverDetails = driverManager.createDriver();
    System.out.println(driverDetails.getDeviceDetails());
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e.getMessage());
    }
    driverManager.dispose(driverDetails);
  }
}
