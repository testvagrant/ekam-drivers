package com.testvagrant.optimus.core.exceptions;

public class TestFeedTargetsNotFoundException extends RuntimeException {

  public TestFeedTargetsNotFoundException(String testFeedName) {
    super("No Targets found in testFeed: " + testFeedName);
  }
}
