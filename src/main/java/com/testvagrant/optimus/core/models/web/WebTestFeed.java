package com.testvagrant.optimus.core.models.web;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WebTestFeed {
  private SiteConfig siteConfig = SiteConfig.builder().build();
  private List<WebTestFeedDetails> targets = new ArrayList<>();
}
