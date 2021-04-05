package com.testvagrant.optimusLite.core.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.testvagrant.optimusLite.commons.entities.Device;
import com.testvagrant.optimusLite.commons.exceptions.DeviceEngagedException;
import com.testvagrant.optimusLite.commons.utils.JsonParser;
import com.testvagrant.optimusLite.core.builder.CapabilitiesBuilder;
import com.testvagrant.optimusLite.core.device.DeviceAllocation;
import com.testvagrant.optimusLite.core.device.DeviceFinder;
import com.testvagrant.optimusLite.core.entity.ExecutionDetails;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.testvagrant.optimusLite.core.parser.TestFeedConstants.*;


/**
 * Created by abhishek on 24/05/17.
 */
public class OptimusConfigParser {
    JsonObject jsonObject;

    public OptimusConfigParser(String appJson) {
        jsonObject = new JsonParser().getAppJson(appJson);
    }


    public ExecutionDetails getExecutionDetails() {
        ExecutionDetails execDetails = getObjectFromJson(jsonObject.get(EXEC_DETAILS), ExecutionDetails.class);
        return execDetails;
    }

    public boolean isMonitoring() {
        return true;
    }

    private void updateTestFeed(JsonObject testFeed, String appName) {
        testFeed.getAsJsonObject("optimusDesiredCapabilities").getAsJsonObject("appiumServerCapabilities")
                .addProperty("app", appName);
    }

    public boolean isForAndroid() {
        return findPlatform("android");
    }

    private boolean findPlatform(String platform) {
        JsonArray testFeedArray = (JsonArray) jsonObject.get(TEST_FEED);
        for (int testFeedIterator = 0; testFeedIterator < testFeedArray.size(); testFeedIterator++) {
            JsonObject testFeedJSON = (JsonObject) testFeedArray.get(testFeedIterator);
            if (testFeedJSON.getAsJsonObject(OPTIMUS_DESIRED_CAPABILITIES).getAsJsonObject(APPIUM_SERVER_CAPABILITIES).get(PLATFORM_NAME).getAsString()
                    .equalsIgnoreCase(platform)) {
                return true;
            }
        }
        return false;
    }

    public boolean isForIos() {
        return findPlatform("ios");
    }

    public boolean isForInterApp() {
        JsonArray testFeedArray = (JsonArray) jsonObject.get(TEST_FEED);
        if (testFeedArray.size() > 1)
            return true;
        return false;
    }

    public String getAppBelongingTo(String appConsumer) {
        JsonArray testFeedArray = (JsonArray) jsonObject.get(TEST_FEED);
        for (int testFeedIterator = 0; testFeedIterator < testFeedArray.size(); testFeedIterator++) {
            JsonObject testFeedJSON = (JsonObject) testFeedArray.get(testFeedIterator);
            if (testFeedJSON.get(BELONGS_TO).getAsString().equalsIgnoreCase(appConsumer)) {
                String appName = testFeedJSON.getAsJsonObject(OPTIMUS_DESIRED_CAPABILITIES).getAsJsonObject(APPIUM_SERVER_CAPABILITIES).get(APP).getAsString();
                if (appName.contains(".apk") || appName.contains(".ipa") || appName.contains(".app")) {
                    return appName;
                } else {
                    return appName + getAppExtension(testFeedJSON);
                }
            }

        }
        throw new RuntimeException("No app found for -- " + appConsumer);
    }

    private String getAppExtension(JsonObject testFeed) {
        String platform = testFeed.getAsJsonObject(OPTIMUS_DESIRED_CAPABILITIES).getAsJsonObject(APPIUM_SERVER_CAPABILITIES).get(PLATFORM_NAME).getAsString();
        switch (platform.toLowerCase()) {
            case "android":
                return ".apk";
            case "ios":
                return getIOSExtension(testFeed.get(RUNS_ON).getAsString());
        }
        return "";
    }

    private String getIOSExtension(String runsOn) {
        switch (runsOn.toLowerCase()) {
            case "simulator":
                return ".app";
            case "device":
                return ".ipa";
        }
        return "";
    }

    private <T> T getObjectFromJson(String appJson, Class<T> classOfT) {
        return new Gson().fromJson(appJson, classOfT);
    }

    private <T> T getObjectFromJson(JsonElement jsonElement, Class<T> classOfT) {
        return new Gson().fromJson(jsonElement, classOfT);
    }

    public List<DeviceAllocation> allocateDevicesForCurrentScenario() throws DeviceEngagedException {

        List<DeviceAllocation> deviceAllocations = new ArrayList<>();


        JsonArray testFeedArray = (JsonArray) jsonObject.get(TEST_FEED);
        for (int testFeedIterator = 0; testFeedIterator < testFeedArray.size(); testFeedIterator++) {
            DeviceAllocation allocatedDevice = new DeviceAllocation();
            JsonObject testFeedJSON = (JsonObject) testFeedArray.get(testFeedIterator);

            System.out.println("updated testFeed -- " + testFeedJSON.toString());

            Device deviceDetails = new DeviceFinder().getAvailableDeviceAndUpdateToEngaged(testFeedJSON);
            updateTestFeed(testFeedJSON, getAppBelongingTo(testFeedJSON.get(BELONGS_TO).getAsString()));
            DesiredCapabilities desiredCapabilities = new CapabilitiesBuilder(testFeedJSON, deviceDetails).buildCapabilities();

            allocatedDevice.setOwner((String) testFeedJSON.get(BELONGS_TO).getAsString());
            allocatedDevice.setDevice(deviceDetails);
            allocatedDevice.setCapabilities(desiredCapabilities);

            deviceAllocations.add(allocatedDevice);
        }
        return deviceAllocations;


    }
}
