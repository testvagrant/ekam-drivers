package com.testvagrant.optimusLite.cache;

import com.google.inject.Inject;
import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.*;

@Guice(modules = {})
public class DeviceProviderTest {

    @Inject
    AndroidDeviceProvider androidDeviceProvider;

    @Inject
    IosDeviceProvider iosDeviceProvider;

    @Test
    public void getAndroidDevices() throws ExecutionException, CloneNotSupportedException {
        DeviceDetails availableDevice = androidDeviceProvider.getAvailableDevice();
        Assert.assertTrue(Objects.nonNull(availableDevice));
        androidDeviceProvider.releaseDevice(availableDevice);
        availableDevice = androidDeviceProvider.getAvailableDevice();
        System.out.println(availableDevice);
    }

    @Test
    public void getIosDevices() throws ExecutionException, CloneNotSupportedException {
        DeviceDetails availableDevice = getIOSDevice();
        Assert.assertTrue(Objects.nonNull(availableDevice));
        availableDevice = iosDeviceProvider.getAvailableDevice();
        System.out.println(availableDevice);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void getAndroidDevicesWithoutReleasing() throws ExecutionException, CloneNotSupportedException {
        DeviceDetails availableDevice = androidDeviceProvider.getAvailableDevice();
        Assert.assertTrue(Objects.nonNull(availableDevice));
        androidDeviceProvider.getAvailableDevice();
    }

    private DeviceDetails getIOSDevice() throws ExecutionException, CloneNotSupportedException {
        DeviceDetails availableDevice = iosDeviceProvider.getAvailableDevice();
        System.out.println(availableDevice.getUdid());
        return availableDevice;
    }

    @Test
    public void parallelDeviceCheck() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(41);
        int deviceCacheSizeBefore = iosDeviceProvider.getTotalAvailableDevices();
        for(int i=0; i<40; i++)  {
            executor.submit(() -> {
                DeviceDetails availableDevice = iosDeviceProvider.getAvailableDevice();
                iosDeviceProvider.releaseDevice(availableDevice);
                return availableDevice;
            });
        };
        executor.awaitTermination(10, TimeUnit.SECONDS);
        int deviceCacheSizeAfter = iosDeviceProvider.getTotalAvailableDevices();
        Assert.assertEquals(deviceCacheSizeAfter, deviceCacheSizeBefore);
    }






}
