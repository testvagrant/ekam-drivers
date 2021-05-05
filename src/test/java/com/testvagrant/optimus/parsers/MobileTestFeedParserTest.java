package com.testvagrant.optimus.parsers;

import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.commons.SystemProperties;
import com.testvagrant.optimus.core.exceptions.AppNotFoundException;
import com.testvagrant.optimus.core.exceptions.TestFeedNotFoundException;
import com.testvagrant.optimus.core.exceptions.TestFeedTargetsNotFoundException;
import com.testvagrant.optimus.core.parser.MobileTestFeedParser;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class MobileTestFeedParserTest extends BaseTest {

  MobileTestFeedParser testFeed,
      testFeedParserWithInvalidServerArguments,
      testFeedParserWithoutServerArguments,
      testFeedParserWithValidAndInvalidServerArguments,
      nestedTestFeed,
      deeplyNestedTestFeed,
      cloudTestFeed,
      invalidAppTestFeed;

  public MobileTestFeedParserTest() {
    testFeed = new MobileTestFeedParser("localTestFeed");
    testFeedParserWithInvalidServerArguments =
        new MobileTestFeedParser("testFeedWithInvalidServerArguments");
    testFeedParserWithoutServerArguments =
        new MobileTestFeedParser("testFeedWithoutServerArguments");
    testFeedParserWithValidAndInvalidServerArguments =
        new MobileTestFeedParser("testFeedWithValidAndInvalidServerArguments");
    nestedTestFeed = new MobileTestFeedParser("mobile/nested/nestedTestFeed");
    deeplyNestedTestFeed = new MobileTestFeedParser("mobile/nested/nested1/nestedTestFeed");
    cloudTestFeed = new MobileTestFeedParser("browserStackTestFeed");
    invalidAppTestFeed = new MobileTestFeedParser("invalidAppTestFeed");
  }

  @Test
  public void runModeShouldBeLocalUnlessSpecified() {
    Assert.assertEquals(SystemProperties.RUN_MODE, "local");
  }

  @Test
  public void defaultPlatformShouldBeAndroidWhenTargetNotSpecified() {
    DesiredCapabilities desiredCapabilities = cloudTestFeed.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getPlatform(), Platform.ANDROID);
  }

  @Test(expectedExceptions = AppNotFoundException.class)
  public void shouldThrowExceptionWhenAppNotFound() {
    DesiredCapabilities desiredCapabilities = invalidAppTestFeed.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getPlatform(), Platform.ANDROID);
  }

  @Test
  public void cloudAppPathShouldNotBeTransformed() {
    DesiredCapabilities desiredCapabilities = cloudTestFeed.getDesiredCapabilities();
    Assert.assertEquals(
        desiredCapabilities.getCapability(MobileCapabilityType.APP),
        "bs://2a415535fc457368f4ac133f1b7b27551b90c98f");
  }

  @Test
  public void desiredCapsShouldIncludeCustomCapabilities() {
    DesiredCapabilities desiredCapabilities = cloudTestFeed.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getCapability("customCap"), "customValue");
  }

  @Test
  public void serverArgumentsLengthShouldBe0WhenServerArgumentBlockIsNotPassed() {
    Map<ServerArgument, String> serverArgumentsMap =
        testFeedParserWithoutServerArguments.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 0);
  }

  @Test
  public void serverArgumentsLengthShouldBe0WhenInvalidServerArgumentsArePassed() {
    Map<ServerArgument, String> serverArgumentsMap =
        testFeedParserWithInvalidServerArguments.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 0);
  }

  @Test
  public void onlyValidServerArgumentsShouldBeAdded() {
    Map<ServerArgument, String> serverArgumentsMap =
        testFeedParserWithValidAndInvalidServerArguments.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 2);
  }

  @Test
  public void nestedTestFeedShouldBeParsed() {
    Map<ServerArgument, String> serverArgumentsMap = nestedTestFeed.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 2);
  }

  @Test
  public void deeplyNestedTestFeedShouldBeParsed() {
    Map<ServerArgument, String> serverArgumentsMap = deeplyNestedTestFeed.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 2);
  }

  @Test(expectedExceptions = TestFeedTargetsNotFoundException.class)
  public void throwExceptionIfNoTargetsFound() {
    MobileTestFeedParser testFeed = new MobileTestFeedParser("noTargetsMobileTestFeed");
    testFeed.getDesiredCapabilities();
  }

  @Test(expectedExceptions = TestFeedNotFoundException.class)
  public void throwExceptionIfTestFeedIsNotFound() {
    MobileTestFeedParser testFeed = new MobileTestFeedParser("invalid");
    testFeed.getDesiredCapabilities();
  }
}
