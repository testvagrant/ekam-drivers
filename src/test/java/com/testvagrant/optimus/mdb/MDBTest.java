package com.testvagrant.optimus.mdb;

import com.google.inject.Inject;
import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.mdb.android.Android;
import com.testvagrant.optimus.mdb.ios.IOS;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class MDBTest extends BaseTest {

  @Inject private Android android;

  @Inject private IOS ios;

  @Test
  public void androidTest() {
    List<DeviceDetails> devices = android.getDevices();
    devices.forEach(System.out::println);
    Assert.assertTrue(devices.size() > 0);
  }

  @Test
  public void iosTest() {
    List<DeviceDetails> devices = ios.getDevices();
    devices.forEach(System.out::println);
    Assert.assertTrue(devices.size() > 0);
  }
}
