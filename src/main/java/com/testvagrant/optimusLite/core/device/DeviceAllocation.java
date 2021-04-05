package com.testvagrant.optimusLite.core.device;

import com.testvagrant.optimusLite.commons.entities.Device;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.remote.DesiredCapabilities;

@Getter @Setter
public class DeviceAllocation {
    private String Owner;
    private DesiredCapabilities capabilities;
    private Device device;
}
