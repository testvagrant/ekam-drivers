package com.testvagrant.optimus.core;

import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.core.appium.LocalMobileDriverManager;
import com.testvagrant.optimus.core.models.mobile.MobileDriverDetails;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocalMobileDriverManagerTest extends BaseTest {

  @BeforeSuite
  public void init() {}

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

  private static class CreateAndDisposeDriver implements Runnable {
    @Override
    public void run() {
      try {
        LocalMobileDriverManager localMobileDriverManager = new LocalMobileDriverManager();
        MobileDriverDetails driverDetails = localMobileDriverManager.createDriver();
        System.out.println(driverDetails.getTargetDetails());
        LocalMobileDriverManager.dispose(driverDetails);
      } catch (Exception ex) {
        throw new RuntimeException(ex.getMessage());
      }
    }
  }
}
