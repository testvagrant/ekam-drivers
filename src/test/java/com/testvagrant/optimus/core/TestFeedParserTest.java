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
          testFeedParserWithoutServerArguements,
          testFeedParserWithValidAndInvalidServerArguments;

  public TestFeedParserTest() {
    testFeedParser = new TestFeedParser("sampleTestFeed");
    testFeedParserWithInvalidServerArguments = new TestFeedParser("sampleTestFeedWithInvalidServerArguments");
    testFeedParserWithoutServerArguements = new TestFeedParser("sampleTestFeedWithoutServerArguments");
    testFeedParserWithValidAndInvalidServerArguments = new TestFeedParser("sampleTestFeedWithValidAndInvalidServerArguments");
  }

  @Test
  public void desiredCapsTest() {
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
    System.out.println(desiredCapabilities);
    System.out.println(desiredCapabilities);
    Assert.assertEquals(desiredCapabilities.getPlatform(), Platform.ANDROID);
  }

  @Test
  public void desiredCapsShouldIncludeCustomCapsTest() {
    DesiredCapabilities desiredCapabilities = testFeedParser.getDesiredCapabilities();
    Assert.assertEquals(desiredCapabilities.getCapability("customCap"), "customValue");
  }

  @Test
  public void serverArgumentsLengthShouldBe0WhenServerArgumentBlockIsNotPassed() {
    Map<ServerArgument, String> serverArgumentsMap = testFeedParserWithoutServerArguements.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 0);
  }

  @Test
  public void serverArgumentsLengthShouldBe0WhenInvalidServerArgumentsArePassed() {
    Map<ServerArgument, String> serverArgumentsMap = testFeedParserWithInvalidServerArguments.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 0);
  }

  @Test
  public void onlyValidServerArgumentsShouldBeAdded() {
    Map<ServerArgument, String> serverArgumentsMap = testFeedParserWithValidAndInvalidServerArguments.getServerArgumentsMap();
    Assert.assertEquals(serverArgumentsMap.size(), 2);
  }
}
