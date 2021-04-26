package com.testvagrant.optimus.devicemanager;

import com.google.gson.GsonBuilder;
import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.api.browserstack.BrowserStackDeviceClient;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.remote.CloudConfigBuilder;
import org.testng.annotations.Test;

public class DeviceManagerTest extends BaseTest {

  //  @Inject private AndroidDeviceManager androidDeviceProvider;
  //
  //  @Inject private IosDeviceManager iosDeviceProvider;
  //
  //  @Inject private DeviceFiltersManager deviceFiltersManager;

  //  @Test
  //  public void getAndroidDevices() {
  //    DeviceDetails availableDevice = androidDeviceProvider.getAvailableDevice();
  //    Assert.assertTrue(Objects.nonNull(availableDevice));
  //    androidDeviceProvider.releaseDevice(availableDevice);
  //    availableDevice = androidDeviceProvider.getAvailableDevice();
  //    System.out.println(availableDevice);
  //  }
  //
  //  @Test
  //  public void getIosDevices() {
  //    DeviceDetails availableDevice = iosDeviceProvider.getAvailableDevice();
  //    System.out.println(availableDevice);
  //  }
  //
  //  @Test(expectedExceptions = RuntimeException.class)
  //  public void getAndroidDevicesWithoutReleasing() {
  //    DeviceDetails availableDevice = androidDeviceProvider.getAvailableDevice();
  //    Assert.assertTrue(Objects.nonNull(availableDevice));
  //    androidDeviceProvider.getAvailableDevice();
  //  }
  //
  //  @Test
  //  public void parallelDeviceCheck() throws InterruptedException {
  //    ExecutorService executor = Executors.newFixedThreadPool(41);
  //    long deviceCacheSizeBefore = iosDeviceProvider.getTotalAvailableDevices();
  //    for (int i = 0; i < 40; i++) {
  //      executor.submit(new AllocateDeviceAndRelease());
  //    }
  //    executor.awaitTermination(10, TimeUnit.SECONDS);
  //    long deviceCacheSizeAfter = iosDeviceProvider.getTotalAvailableDevices();
  //    Assert.assertEquals(deviceCacheSizeAfter, deviceCacheSizeBefore);
  //  }
  //
  //  @Test
  //  public void deviceFilterByUdidTest() {
  //    TestFeedParser testFeedParser = new TestFeedParser(System.getProperty("testFeed"));
  //    Predicate<DeviceDetails> deviceFilters =
  //        deviceFiltersManager.createDeviceFilters(
  //            testFeedParser.getDesiredCapabilities(), testFeedParser.getDeviceFilters());
  //    IntStream.range(0, 5)
  //        .forEach(
  //            r -> {
  //              DeviceDetails availableDevice =
  //                  androidDeviceProvider.getAvailableDevice(deviceFilters);
  //              androidDeviceProvider.releaseDevice(availableDevice);
  //              System.out.println(availableDevice);
  //            });
  //  }

  @Test
  public void getBrowserStackDevices() {
    CloudConfig cloudConfig = new CloudConfigBuilder().build();
    BrowserStackDeviceClient client = new BrowserStackDeviceClient(cloudConfig);

    System.out.println(
        new GsonBuilder().setPrettyPrinting().create().toJson(client.getAndroidDevices()));
    System.out.println("--------------------------");
    System.out.println(
        new GsonBuilder().setPrettyPrinting().create().toJson(client.getIosDevices()));
  }

  //  private class AllocateDeviceAndRelease implements Runnable {
  //    @Override
  //    public void run() {
  //      try {
  //        DeviceDetails availableDevice = iosDeviceProvider.getAvailableDevice();
  //        iosDeviceProvider.releaseDevice(availableDevice);
  //      } catch (Exception ex) {
  //        throw new RuntimeException(ex.getMessage());
  //      }
  //    }
  //  }
}
