package com.testvagrant.optimus.core.exceptions;

public class NoTestFeedException extends RuntimeException {
  public NoTestFeedException(String testFeedProperty) {
    super(String.format("'%s' system property not set", testFeedProperty));
  }
}
