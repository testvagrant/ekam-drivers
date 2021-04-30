package com.testvagrant.optimus.core.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudConfig {
  private String username;
  private String accessKey;
  private String hub;
}
