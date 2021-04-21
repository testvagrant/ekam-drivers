package com.testvagrant.optimus.commons.exceptions;

public class NoTestFeedException extends RuntimeException {
  public NoTestFeedException() {
    super("TestFeed is mandatory. Please Set testFeed system property");
  }
}
