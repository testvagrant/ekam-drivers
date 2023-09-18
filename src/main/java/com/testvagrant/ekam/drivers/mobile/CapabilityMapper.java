package com.testvagrant.ekam.drivers.mobile;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
public class CapabilityMapper<T extends MutableCapabilities> {

    public T mapToOptions(DesiredCapabilities desiredCapabilities, T options) {
        desiredCapabilities.asMap().forEach(options::setCapability);
        return options;
    }
}

