package com.testvagrant.optimus.core.screenshots;

import com.testvagrant.optimus.core.models.TargetDetails;
import lombok.*;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimusRunContext {
  private WebDriver webDriver;
  private Path testFolder;

  @Builder.Default private List<TargetDetails> targets = new ArrayList<>();

  public OptimusRunContext testPath(String className, String testName) {
    testFolder =
        Paths.get(
            System.getProperty("user.dir"),
            "build",
            "optimus-execution-timeline",
            className,
            testName);
    return this;
  }

  public OptimusRunContext addTarget(TargetDetails target) {
    targets.add(target);
    return this;
  }

  public OptimusRunContext addTarget(TargetDetails... target) {
    targets.addAll(Arrays.asList(target));
    return this;
  }
}