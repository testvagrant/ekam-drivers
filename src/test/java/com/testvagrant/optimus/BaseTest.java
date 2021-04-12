package com.testvagrant.optimus;

import org.testng.annotations.Guice;

@Guice()
public class BaseTest {
  public BaseTest() {
    System.setProperty("testFeed", "sampleTestFeed");
  }
}
