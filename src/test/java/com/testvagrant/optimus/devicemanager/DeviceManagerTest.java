package com.testvagrant.optimus.devicemanager;

import com.google.inject.Inject;
import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class DeviceManagerTest extends BaseTest {

  @Inject private AndroidDeviceManager androidDeviceProvider;

  @Inject private IosDeviceManager iosDeviceProvider;

  @Inject private DeviceFiltersManager deviceFiltersManager;

  @Test
  public void getAndroidDevices() {
    TargetDetails availableDevice = androidDeviceProvider.getAvailableDevice();
    Assert.assertTrue(Objects.nonNull(availableDevice));
    androidDeviceProvider.releaseDevice(availableDevice);
    availableDevice = androidDeviceProvider.getAvailableDevice();
    System.out.println(availableDevice);
  }

  @Test
  public void getIosDevices() {
    TargetDetails availableDevice = iosDeviceProvider.getAvailableDevice();
    System.out.println(availableDevice);
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void getAndroidDevicesWithoutReleasing() {
    TargetDetails availableDevice = androidDeviceProvider.getAvailableDevice();
    Assert.assertTrue(Objects.nonNull(availableDevice));
    androidDeviceProvider.getAvailableDevice();
  }

  @Test
  public void parallelDeviceCheck() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(41);
    long deviceCacheSizeBefore = iosDeviceProvider.getTotalAvailableDevices();
    for (int i = 0; i < 40; i++) {
      executor.submit(new AllocateDeviceAndRelease());
    }
    executor.awaitTermination(10, TimeUnit.SECONDS);
    long deviceCacheSizeAfter = iosDeviceProvider.getTotalAvailableDevices();
    Assert.assertEquals(deviceCacheSizeAfter, deviceCacheSizeBefore);
  }

  @Test
  public void deviceFilterByUdidTest() {
    TestFeedParser testFeedParser = new TestFeedParser(System.getProperty("testFeed"));
    Predicate<TargetDetails> deviceFilters =
        deviceFiltersManager.createDeviceFilters(
            testFeedParser.getDesiredCapabilities(), testFeedParser.getDeviceFilters());
    IntStream.range(0, 5)
        .forEach(
            r -> {
              TargetDetails availableDevice =
                  androidDeviceProvider.getAvailableDevice(deviceFilters);
              androidDeviceProvider.releaseDevice(availableDevice);
              System.out.println(availableDevice);
            });
  }

  private class AllocateDeviceAndRelease implements Runnable {
    @Override
    public void run() {
      try {
        TargetDetails availableDevice = iosDeviceProvider.getAvailableDevice();
        iosDeviceProvider.releaseDevice(availableDevice);
      } catch (Exception ex) {
        throw new RuntimeException(ex.getMessage());
      }
    }
  }
}
