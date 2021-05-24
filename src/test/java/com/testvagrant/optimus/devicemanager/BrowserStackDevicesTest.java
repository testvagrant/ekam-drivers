package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.TargetDetails;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.function.Predicate;

public class BrowserStackDevicesTest {

  @Test
  public void getIosDevice() {
    Predicate<TargetDetails> filter =
        (targetDetails -> targetDetails.getName().equals("iPhone 12"));
    TargetDetails device =
        BrowserstackDeviceManager.getInstance(OptimusSupportedPlatforms.IOS).getDevice(filter);

    Assert.assertEquals(device.getName(), "iPhone 12");
  }

  @Test
  public void getAndroidDevice() {
    Predicate<TargetDetails> filter =
            (targetDetails -> targetDetails.getName().equals("OnePlus 7"));
    TargetDetails device =
            BrowserstackDeviceManager.getInstance(OptimusSupportedPlatforms.ANDROID).getDevice(filter);

    Assert.assertEquals(device.getName(), "OnePlus 7");
  }
}
