/*
 * Copyright (c) 2017.  TestVagrant Technologies
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.testvagrant.optimusLite.core.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.testvagrant.optimusLite.commons.entities.Device;
import com.testvagrant.optimusLite.core.utils.AppiumServerManager;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class CapabilitiesBuilder {
    private DesiredCapabilities capabilities = new DesiredCapabilities();
    private JsonObject testFeedJSON;

    public CapabilitiesBuilder(JsonObject testFeedJSON, Device device) {

        this.testFeedJSON = testFeedJSON;
        JsonObject appiumServerCapabilities = (JsonObject) ((JsonObject) testFeedJSON.get("optimusDesiredCapabilities")).get("appiumServerCapabilities");
        if (!isNativeApp()) {
            buildWebAppCapabilities(appiumServerCapabilities, device);
            return;
        }
        File app = getAppFile((String) testFeedJSON.get("appDir").getAsString(), (String) appiumServerCapabilities.get("app").getAsString());
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("udid", device.getUdid());
        capabilities.setCapability("deviceName", device.getDeviceName());
        capabilities.setCapability("systemPort", AppiumServerManager.aRandomOpenPortOnAllLocalInterfaces());
        initializeCapabilities();
    }

    private void buildWebAppCapabilities(JsonObject appiumServerCapabilities, Device deviceDetails) {
        if (isBrowserAppProvided(appiumServerCapabilities)) {
            File app = getAppFile((String) testFeedJSON.get("appDir").getAsString(), (String) appiumServerCapabilities.get("app").getAsString());
            capabilities.setCapability("app", app.getAbsolutePath());
        }
        capabilities.setCapability("udid", deviceDetails.getUdid());
        capabilities.setCapability("deviceName", deviceDetails.getDeviceName());
        initializeCapabilities();
    }

    private boolean isAndroid(JsonObject testFeedJSON) {
        String platformName = testFeedJSON.get("platformName").getAsString();
        return platformName.equalsIgnoreCase("Android");
    }

    private boolean isNativeApp() {
        Boolean nativeApp = testFeedJSON.get("nativeApp").getAsBoolean();
        return nativeApp;
    }

    private boolean isBrowserAppProvided(JsonObject testFeedJSON) {
        try {
            String appName = testFeedJSON.get("app").getAsString();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void initializeCapabilities() {
        JsonObject appiumServerCapabilities = (JsonObject) ((JsonObject) testFeedJSON.get("optimusDesiredCapabilities")).get("appiumServerCapabilities");
        JsonObject platformSpecificCapabilities = null;

        if (appiumServerCapabilities.get("platformName").toString().equalsIgnoreCase("Android")) {
            platformSpecificCapabilities = (JsonObject) ((JsonObject) testFeedJSON.get("optimusDesiredCapabilities")).get("androidOnlyCapabilities");
        } else if (appiumServerCapabilities.get("platformName").toString().equalsIgnoreCase("iOS")) {
            platformSpecificCapabilities = (JsonObject) ((JsonObject) testFeedJSON.get("optimusDesiredCapabilities")).get("iOSOnlyCapabilities");
        }
        setDesiredCapabilities(appiumServerCapabilities, capabilities);
        setDesiredCapabilities(platformSpecificCapabilities, capabilities);
    }

    private void setDesiredCapabilities(JsonObject platformSpecificCapabilities, DesiredCapabilities desiredCapabilities) {
        Iterator<Map.Entry<String, JsonElement>> keys = platformSpecificCapabilities.entrySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next().getKey();
            if (key.equalsIgnoreCase("app")) {
                continue;
            }
            Object value = platformSpecificCapabilities.get(key);
            if (value instanceof Boolean) {
                desiredCapabilities.setCapability(key, platformSpecificCapabilities.get(key).getAsString());
            } else if (value instanceof String) {
                desiredCapabilities.setCapability(key, platformSpecificCapabilities.get(key));
            } else if (value instanceof Integer) {
                desiredCapabilities.setCapability(key, platformSpecificCapabilities.get(key).getAsInt());
            } else if (value instanceof JsonArray) {
                desiredCapabilities.setCapability(key, platformSpecificCapabilities.get(key).getAsJsonArray());
            }
        }
    }


    public DesiredCapabilities buildCapabilities() {
        return capabilities;
    }


    private static File getAppFile(String appLocation, String appName) {
        File appDir = new File(appLocation);
        return new File(appDir, appName);
    }

}
