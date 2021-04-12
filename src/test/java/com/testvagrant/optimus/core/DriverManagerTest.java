package com.testvagrant.optimus.core;

import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.core.appium.DriverManager;
import com.testvagrant.optimus.core.model.MobileDriverDetails;
import com.testvagrant.optimus.devicemanager.AndroidDeviceManager;
import com.testvagrant.optimus.devicemanager.DeviceManager;
import org.testng.annotations.BeforeSuite;
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
    new CreateAndDisposeDriver().run();
  }

  @Test
  public void parallelAndroidDriverTest() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(2);
    executor.execute(new CreateAndDisposeDriver());
    executor.execute(new CreateAndDisposeDriver());
    executor.awaitTermination(30, TimeUnit.SECONDS);
  }

  private class CreateAndDisposeDriver implements Runnable {
    @Override
    public void run() {
      try {
        DriverManager driverManager = new DriverManager(deviceManager);
        MobileDriverDetails driverDetails = driverManager.createDriver();
        System.out.println(driverDetails.getDeviceDetails());
        driverManager.dispose(driverDetails);
      } catch (Exception ex) {
        throw new RuntimeException(ex.getMessage());
      }
    }
  }
}
