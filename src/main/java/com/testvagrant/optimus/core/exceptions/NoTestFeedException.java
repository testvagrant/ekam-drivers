package com.testvagrant.optimus.core.exceptions;

public class NoTestFeedException extends RuntimeException {
  public NoTestFeedException() {
    super("TestFeed is mandatory. Please Set testFeed system property");
  }
}
