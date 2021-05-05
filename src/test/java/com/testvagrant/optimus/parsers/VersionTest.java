package com.testvagrant.optimus.parsers;

import com.testvagrant.optimus.BaseTest;
import com.testvagrant.optimus.core.comparables.Version;
import com.testvagrant.optimus.core.models.mobile.TestFeedDeviceFilterOperators;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class VersionTest extends BaseTest {

  @Test
  public void shouldValidateEQ() {
    List<Pair<String, String>> inputs =
        new ArrayList<Pair<String, String>>() {
          {
            add(Pair.of("7.1.4", "7.1.4"));
            add(Pair.of("7.1", "7.1.0"));
            add(Pair.of("8", "8"));
            add(Pair.of("7", "7.0.0"));
          }
        };

    inputs.forEach(
        pair -> {
          Version expectedVersion = new Version(pair.getRight());
          Version actualVersion = new Version(pair.getLeft());

          Assert.assertEquals(
              actualVersion.compareTo(expectedVersion),
              TestFeedDeviceFilterOperators.EQ.getValue());
        });
  }

  @Test
  public void shouldValidateLT() {
    List<Pair<String, String>> inputs =
        new ArrayList<Pair<String, String>>() {
          {
            add(Pair.of("7.1", "7.1.4"));
            add(Pair.of("7.1", "8"));
            add(Pair.of("7.1.1", "7.1.4"));
            add(Pair.of("7", "8"));
            add(Pair.of("8", "10.1.1"));
          }
        };

    inputs.forEach(
        pair -> {
          Version expectedVersion = new Version(pair.getRight());
          Version actualVersion = new Version(pair.getLeft());

          Assert.assertEquals(
              actualVersion.compareTo(expectedVersion),
              TestFeedDeviceFilterOperators.LT.getValue());
        });
  }

  @Test
  public void shouldValidateGT() {
    List<Pair<String, String>> inputs =
        new ArrayList<Pair<String, String>>() {
          {
            add(Pair.of("7.1.4", "7.1"));
            add(Pair.of("8", "7.1"));
            add(Pair.of("7.1.4", "7.1.1"));
            add(Pair.of("8", "7"));
            add(Pair.of("10.1.1", "8"));
          }
        };

    inputs.forEach(
        pair -> {
          Version expectedVersion = new Version(pair.getRight());
          Version actualVersion = new Version(pair.getLeft());

          Assert.assertEquals(
              actualVersion.compareTo(expectedVersion),
              TestFeedDeviceFilterOperators.GT.getValue());
        });
  }
}
