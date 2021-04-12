package com.testvagrant.optimus.core;

import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class TestFeedParserTest extends BaseTest {

  TestFeedParser testFeedParser,
      testFeedParserWithInvalidServerArguments,
      testFeedParserWithoutServerArguments,
      testFeedParserWithValidAndInvalidServerArguments,
      nestedTestFeed,
      deeplyNestedTestFeed;

  public TestFeedParserTest() {
    testFeedParser = new TestFeedParser("sampleTestFeed");
    testFeedParserWithInvalidServerArguments =
        new TestFeedParser("sampleTestFeedWithInvalidServerArguments");
    testFeedParserWithoutServerArguments =
        new TestFeedParser("sampleTestFeedWithoutServerArguments");
    testFeedParserWithValidAndInvalidServerArguments =
        new TestFeedParser("sampleTestFeedWithValidAndInvalidServerArguments");
    nestedTestFeed = new TestFeedParser("nested/sampleTestFeed");
    deeplyNestedTestFeed = new TestFeedParser("nested/nested1/sampleTestFeed");
  }

  @Test
  public void desiredCapsTest() {
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getPlatform(), Platform.ANDROID);
  }

  @Test
  public void desiredCapsShouldIncludeCustomCapsTest() {
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
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
}
