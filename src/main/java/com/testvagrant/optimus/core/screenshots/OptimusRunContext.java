package com.testvagrant.optimus.core.screenshots;

import com.google.common.collect.ImmutableList;
import com.testvagrant.optimus.commons.entities.Device;
import lombok.*;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class OptimusRunContext {
    private WebDriver webDriver;
    private Path testFolder;

    @Builder.Default
    private List<Device> targets = new ArrayList<>();

    public OptimusRunContext testPath(String className, String testName) {
        testFolder = Paths.get(System.getProperty("user.dir"),
                "execution-timeline",
                className, testName);
        return this;
    }

    public OptimusRunContext addTarget(Device target) {
        targets.add(target);
        return this;
    }

    public OptimusRunContext addTarget(Device... target) {
        targets = Arrays.asList(target);
        return this;
    }
}
