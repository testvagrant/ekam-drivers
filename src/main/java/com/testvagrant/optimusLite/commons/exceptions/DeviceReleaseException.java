package com.testvagrant.optimusLite.commons.exceptions;

public class DeviceReleaseException extends Exception {

    public DeviceReleaseException(String udid) {
        super(String.format("Cannot release device with udid %s",udid));
    }
}
