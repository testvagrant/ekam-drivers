package com.testvagrant.optimusLite.commons.entities;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Device {
    private String id;
    private String platform;
    private String status;
    private String deviceName;
    private String runsOn;
    private String platformVersion;
    private String udid;
    private String buildId;
    private byte[] screenshot = new byte[]{};

    @Override
    public String toString() {
        return "Device{" +
                "platform:\"" + platform + '\"' +
                ", deviceName:\"" + deviceName + '\"' +
                ", runsOn:\"" + runsOn + '\"' +
                ", platformVersion:\"" + platformVersion + '\"' +
                ", udid:\"" + udid + '\"' +
                '}';
    }
}
