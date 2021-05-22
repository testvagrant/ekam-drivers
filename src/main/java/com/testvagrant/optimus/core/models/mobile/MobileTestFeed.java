package com.testvagrant.optimus.core.models.mobile;

import com.testvagrant.optimus.core.models.web.SiteConfig;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobileTestFeed {
  @Builder.Default private String appDir = "app";

  @Builder.Default private SiteConfig siteConfig = SiteConfig.builder().build();

  @Builder.Default private List<MobileTestFeedDetails> targets = new ArrayList<>();

  @Builder.Default private List<String> serverArguments = new ArrayList<>();

  @Builder.Default private DeviceFilters deviceFilters = new DeviceFilters();
}
