package com.testvagrant.optimus.core.models.web;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WebTestFeed {
  private Map<String, Object> desiredCapabilities = new HashMap<>();
  private List<String> arguments = new ArrayList<>();
  private Map<String, Object> preferences = new HashMap<>();
  private List<String> extensions = new ArrayList<>();
  private Map<String, Object> experimentalOptions = new HashMap<>();
}
