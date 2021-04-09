package com.testvagrant.optimus.devicemanager;

import com.google.inject.Inject;
import com.testvagrant.optimus.commons.entities.DeviceDetails;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Guice()
public class DeviceManagerTest {

  @Inject private AndroidDeviceManager androidDeviceProvider;

  @Inject private IosDeviceManager iosDeviceProvider;

  @Test
  public void getAndroidDevices() {
    DeviceDetails availableDevice = androidDeviceProvider.getAvailableDevice();
    Assert.assertTrue(Objects.nonNull(availableDevice));
    androidDeviceProvider.releaseDevice(availableDevice);
    availableDevice = androidDeviceProvider.getAvailableDevice();
    System.out.println(availableDevice);
  }

  @Test
  public void getIosDevices() {
    DeviceDetails availableDevice = iosDeviceProvider.getAvailableDevice();
    System.out.println(availableDevice);
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void getAndroidDevicesWithoutReleasing() {
    DeviceDetails availableDevice = androidDeviceProvider.getAvailableDevice();
    Assert.assertTrue(Objects.nonNull(availableDevice));
    androidDeviceProvider.getAvailableDevice();
  }

  @Test
  public void parallelDeviceCheck() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(41);
    long deviceCacheSizeBefore = iosDeviceProvider.getTotalAvailableDevices();
    for (int i = 0; i < 40; i++) {
      executor.submit(
          () -> {
            DeviceDetails availableDevice = iosDeviceProvider.getAvailableDevice();
            iosDeviceProvider.releaseDevice(availableDevice);
            return availableDevice;
          });
    }
    executor.awaitTermination(10, TimeUnit.SECONDS);
    long deviceCacheSizeAfter = iosDeviceProvider.getTotalAvailableDevices();
    Assert.assertEquals(deviceCacheSizeAfter, deviceCacheSizeBefore);
  }
}
