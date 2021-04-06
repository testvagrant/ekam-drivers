package com.testvagrant.optimusLite.cache;

import com.google.inject.Inject;
import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
@Guice(modules = {})
public class DeviceProviderTest {

    @Inject
    AndroidDeviceProvider androidDeviceProvider;

    @Inject
    IosDeviceProvider iosDeviceProvider;

    @Test
    public void getAndroidDevices() throws ExecutionException {
        DeviceDetails availableDevice = androidDeviceProvider.getAvailableDevice();
        Assert.assertTrue(Objects.nonNull(availableDevice));
    }

    @Test
    public void getIosDevices() throws ExecutionException {
        DeviceDetails availableDevice = iosDeviceProvider.getAvailableDevice();
        Assert.assertTrue(Objects.nonNull(availableDevice));
    }
}
