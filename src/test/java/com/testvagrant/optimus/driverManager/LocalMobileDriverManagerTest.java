package com.testvagrant.optimus.driverManager;

import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.core.mobile.MobileDriverManager;
import com.testvagrant.optimus.core.models.mobile.MobileDriverDetails;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocalMobileDriverManagerTest extends BaseTest {

  @BeforeSuite
  public void init() {}

  @Test(enabled = false)
  public void androidDriverTest() {
    MobileDriverManager mobileDriverManager = new MobileDriverManager();
    MobileDriverDetails driverDetails = mobileDriverManager.createDriverDetails();
    System.out.println(driverDetails.getTargetDetails());
    MobileDriverManager.dispose();
  }

  @Test(enabled = false)
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
        MobileDriverDetails driverDetails = new MobileDriverManager().createDriverDetails();
        System.out.println(driverDetails.getTargetDetails());
        MobileDriverManager.dispose();
      } catch (Exception ex) {
        throw new RuntimeException(ex.getMessage());
      }
    }
  }
}
