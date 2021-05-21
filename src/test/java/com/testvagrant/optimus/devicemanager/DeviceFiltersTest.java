package com.testvagrant.optimus.devicemanager;

import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.models.mobile.DeviceType;
import com.testvagrant.optimus.core.parser.MobileTestFeedParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.function.Predicate;

public class DeviceFiltersTest {

  @Test
  public void deviceFilterTest() {
    MobileTestFeedParser testFeedParser = new MobileTestFeedParser("emptyDeviceFiltersTestFeed");

    Predicate<TargetDetails> filters =
        new DeviceFiltersManager()
            .createDeviceFilters(
                testFeedParser.getDesiredCapabilities(), testFeedParser.getDeviceFilters());

    TargetDetails details =
        TargetDetails.builder()
            .name("Test")
            .runsOn(DeviceType.EMULATOR)
            .udid("123456")
            .platform(OptimusSupportedPlatforms.ANDROID)
            .platformVersion("11.0")
            .build();

    boolean filterTest = filters.test(details);
    Assert.assertTrue(filterTest);
  }
}
