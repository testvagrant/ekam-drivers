package com.testvagrant.optimus.core.models.mobile;

public enum TestFeedDeviceFilterOperators {
  LT("<", -1),
  GT(">", 1),
  LTE("<=", -2),
  GTE(">=", 2),
  EQ("=", 0);

  private final String operator;
  private final int value;

  TestFeedDeviceFilterOperators(String operator, int value) {
    this.operator = operator;
    this.value = value;
  }

  public String getOperator() {
    return operator;
  }

  public int getValue() {
    return value;
  }
}
