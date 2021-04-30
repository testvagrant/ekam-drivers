package com.testvagrant.optimus.core.exceptions;

public class NoTestFeedException extends RuntimeException {
  public NoTestFeedException() {
    super("'testFeed' system property not set");
  }
}
