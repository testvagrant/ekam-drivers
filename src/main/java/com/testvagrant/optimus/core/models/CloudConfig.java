package com.testvagrant.optimus.core.models;

import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true, builderMethodName = "configBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class CloudConfig {

  @Builder.Default
  private String username = "";

  @Builder.Default
  private String accessKey = "";

  @Builder.Default
  private String hub = "";

  @Builder.Default
  private String url = "";

  @Builder.Default
  private String protocol = "https";
}
