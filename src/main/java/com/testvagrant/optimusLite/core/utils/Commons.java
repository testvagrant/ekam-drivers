package com.testvagrant.optimusLite.core.utils;


import com.google.gson.JsonObject;

public class Commons {

    public boolean isUDIDAvailable(JsonObject testFeed) {
        try {
            JsonObject appiumServerCapabilities = (JsonObject) ((JsonObject) testFeed.get("optimusDesiredCapabilities")).get("appiumServerCapabilities");
            String udid = appiumServerCapabilities.get("udid").getAsString();
            return udid == null;
        } catch (Exception e) {

        }
        return false;
    }

    public String getUDID(JsonObject testFeed) {
        JsonObject appiumServerCapabilities = (JsonObject) ((JsonObject) testFeed.get("optimusDesiredCapabilities")).get("appiumServerCapabilities");
        String udid = appiumServerCapabilities.get("udid").getAsString();
        return udid;
    }
}
