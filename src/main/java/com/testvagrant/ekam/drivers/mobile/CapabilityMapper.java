package com.testvagrant.ekam.drivers.mobile;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class CapabilityMapper<T extends MutableCapabilities> {

    public T mapToOptions(DesiredCapabilities desiredCapabilities, T options) {
        for (Map.Entry<String, Object> entry : desiredCapabilities.asMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            options.setCapability(key, value);
        }
        return options;
    }
}

