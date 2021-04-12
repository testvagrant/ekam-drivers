package com.testvagrant.optimus.core.commons;

import com.testvagrant.optimus.core.model.TestFeedDeviceFilterOperators;
import org.apache.commons.lang3.tuple.Pair;

public class VersionComparator implements Comparable<VersionComparator> {

  private final String version;

  public final String get() {
    return this.version;
  }

  public VersionComparator(String version) {
    if (!version.matches("[0-9]+(\\.[0-9]+)*"))
      throw new RuntimeException("Invalid version format");
    this.version = version;
  }

  @Override
  public int compareTo(VersionComparator that) {
    if (that == null) return TestFeedDeviceFilterOperators.GT.getValue();

    Pair<Integer, Integer> versionsPair = appendZeroes(this.get(), that.get());
    int thisVersion = versionsPair.getLeft();
    int thatVersion = versionsPair.getRight();

    if (thisVersion < thatVersion) return TestFeedDeviceFilterOperators.LT.getValue();
    if (thisVersion == thatVersion) return TestFeedDeviceFilterOperators.EQ.getValue();

    return TestFeedDeviceFilterOperators.GT.getValue();
  }

  // TODO: Refactor this method
  private Pair<Integer, Integer> appendZeroes(String thisPart, String thatPart) {
    int thisPartWithDots = thisPart.length();
    int thisPartLengthWithoutDots = thisPart.replaceAll("\\.", "").length();
    int thisPartDotsLength = thisPartWithDots - thisPartLengthWithoutDots;

    int thatPartWithDots = thatPart.length();
    int thatPartLengthWithoutDots = thatPart.replaceAll("\\.", "").length();
    int thatPartDotsLength = thatPartWithDots - thatPartLengthWithoutDots;

    int maxLength = Math.max(thisPartDotsLength, thatPartDotsLength);

    if (thisPartDotsLength < maxLength) {
      for (int i = 0; i < (maxLength - thisPartDotsLength); i++) {
        thisPart = thisPart.concat(".0");
      }
    }

    if (thatPartDotsLength < maxLength) {
      for (int i = 0; i < (maxLength - thatPartDotsLength); i++) {
        thatPart = thatPart.concat(".0");
      }
    }

    return Pair.of(
        Integer.parseInt(thisPart.replaceAll("\\.", "")),
        Integer.parseInt(thatPart.replaceAll("\\.", "")));
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) return true;
    if (that == null) return false;
    if (this.getClass() != that.getClass()) return false;
    return this.compareTo((VersionComparator) that) == 0;
  }
}
