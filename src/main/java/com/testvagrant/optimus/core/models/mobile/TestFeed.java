package com.testvagrant.optimus.core.models.mobile;

import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestFeed {
  private String appDir = "app";
  private String app = "";
  private String platform = OptimusSupportedPlatforms.ANDROID.name();
  private Map<String, Object> desiredCapabilities = new HashMap<>();
  private List<String> serverArguments = new ArrayList<>();
  private DeviceFilters deviceFilters = new DeviceFilters();
}
