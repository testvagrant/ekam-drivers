package com.testvagrant.optimus.core.exceptions;

import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import org.openqa.selenium.remote.CapabilityType;

import java.util.Arrays;

public class UnsupportedPlatform extends RuntimeException {
  public UnsupportedPlatform() {
    super(
        String.format(
            "%s/%s is mandatory and should be one of: %s",
            CapabilityType.PLATFORM_NAME,
            CapabilityType.BROWSER_NAME,
            Arrays.toString(OptimusSupportedPlatforms.values())));
  }
}
