package com.testvagrant.optimus.core.comparables;

import com.testvagrant.optimus.core.model.TestFeedDeviceFilterOperators;
import org.apache.commons.lang3.tuple.Pair;

public class Version implements Comparable<Version> {

  private final String version;

  public final String get() {
    return this.version;
  }

  public Version(String version) {
    if (!version.matches("[0-9]+(\\.[0-9]+)*"))
      throw new RuntimeException("Invalid version format");
    this.version = version;
  }

  @Override
  public int compareTo(Version version) {
    if (version == null) return TestFeedDeviceFilterOperators.GT.getValue();

    Pair<Integer, Integer> versionsPair = formatVersions(this.get(), version.get());
    int thisVersion = versionsPair.getLeft();
    int thatVersion = versionsPair.getRight();

    if (thisVersion < thatVersion) return TestFeedDeviceFilterOperators.LT.getValue();
    if (thisVersion == thatVersion) return TestFeedDeviceFilterOperators.EQ.getValue();

    return TestFeedDeviceFilterOperators.GT.getValue();
  }

  private Pair<Integer, Integer> formatVersions(String thisPart, String thatPart) {
    int thisPartDotsLength = getDotsLength(thisPart);
    int thatPartDotsLength = getDotsLength(thatPart);

    int maxLength = Math.max(thisPartDotsLength, thatPartDotsLength);

    thisPart = padVersionWithZeroes(thisPart, (maxLength - thisPartDotsLength));
    thatPart = padVersionWithZeroes(thatPart, (maxLength - thatPartDotsLength));

    return Pair.of(Integer.parseInt(thisPart), Integer.parseInt(thatPart));
  }

  private int getDotsLength(String value) {
    int lengthWithDots = value.length();
    int lengthWithoutDots = value.replaceAll("\\.", "").length();
    return lengthWithDots - lengthWithoutDots;
  }

  private String padVersionWithZeroes(String value, int zeroesTobeAppended) {
    for (int i = 0; i < zeroesTobeAppended; i++) {
      value = value.concat(".0");
    }

    return value.replaceAll("\\.", "");
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) return true;
    if (that == null) return false;
    if (this.getClass() != that.getClass()) return false;
    return this.compareTo((Version) that) == 0;
  }
}
