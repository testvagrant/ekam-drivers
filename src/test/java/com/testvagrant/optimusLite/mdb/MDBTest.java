package com.testvagrant.optimusLite.mdb;

import com.google.inject.Inject;
import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.mdb.android.Android;
import com.testvagrant.optimusLite.mdb.ios.IOS;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.List;

@Guice(modules = {})
public class MDBTest {

    @Inject
    Android android;

    @Inject
    IOS ios;

    @Test
    public void androidTest() {
        List<DeviceDetails> devices = android.getDevices();
        devices.forEach(System.out::println);
        Assert.assertEquals(devices.size() > 0, true);
    }

    @Test
    public void iosTest() {
        List<DeviceDetails> devices = ios.getDevices();
        devices.forEach(System.out::println);
        Assert.assertEquals(devices.size() > 0, true);
    }

    @Test
    public void jsonGeneratorTest() {
        List<DeviceDetails> deviceDetails = MDBClient.loadDevices();
        System.out.println(deviceDetails.size());
        deviceDetails.forEach(System.out::println);
    }



}
