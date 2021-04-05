package com.testvagrant.optimusLite.core.utils;

public class RunProperties {

    public static boolean isDevMode() {
        String devMode = System.getProperty("devMode");
        return devMode != null && Boolean.parseBoolean(devMode);
    }
}
