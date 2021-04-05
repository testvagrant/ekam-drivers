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

package com.testvagrant.optimusLite.core.device;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.testvagrant.optimusLite.commons.entities.DeviceDetails;
import com.testvagrant.optimusLite.commons.entities.Scenario;
import com.testvagrant.optimusLite.commons.entities.SmartBOT;
import com.testvagrant.optimusLite.commons.exceptions.DeviceEngagedException;
import com.testvagrant.optimusLite.commons.exceptions.DeviceMatchingException;
import com.testvagrant.optimusLite.commons.exceptions.DeviceReleaseException;
import com.testvagrant.optimusLite.commons.utils.JsonParser;
import com.testvagrant.optimusLite.core.builder.SmartBOTBuilder;
import com.testvagrant.optimusLite.core.parser.OptimusConfigParser;
import com.testvagrant.optimusLite.core.utils.AppiumServerManager;
import com.testvagrant.optimusLite.core.utils.RunProperties;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

//TODO: Refactor whole class
public class OptimusController {

    private String appJson;
    private Scenario scenario;
    private OptimusConfigParser optimusConfigParser;


    public OptimusController(String appJson, Scenario scenario) {
        this.scenario = scenario;
        validateAPPJson(appJson);
        this.appJson = prepareAppJson(appJson);
        this.optimusConfigParser = new OptimusConfigParser(this.appJson);
    }

    private void validateAPPJson(String appJson) {
        if (!isJsonValid(appJson)) {
            throw new RuntimeException("incorrect testFeed json");
        }
    }

    private boolean isJsonValid(String appJson) {
        return true;
    }

    private String prepareAppJson(String appJson) {
        return runModeFragmentation() ? updatedAppJsonWithUdid(appJson, System.getProperty("udid")) : appJson;
    }

    private boolean runModeFragmentation() {
        String runMode = System.getProperty("runMode");
        if (runMode != null)
            return runMode.equalsIgnoreCase("Fragmentation");
        else
            return false;
    }


    public String updatedAppJsonWithUdid(String appJson, String udid) {
        JsonObject jsonObject = new JsonParser().getAppJson(appJson);
        JsonArray testFeedArray = (JsonArray) jsonObject.get("testFeed");
        JsonObject testFeed = (JsonObject) testFeedArray.get(0);
        JsonObject appiumServerCapabilities = (JsonObject) ((JsonObject) testFeed.get("optimusDesiredCapabilities")).get("appiumServerCapabilities");
        appiumServerCapabilities.addProperty("udid", udid);

        System.out.println("appJson updated with - " + udid);
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }


    public void deRegisterSmartBOTs(List<SmartBOT> smartBOTs) throws DeviceReleaseException {
        for (SmartBOT engagedBOT : smartBOTs) {
            try {
                engagedBOT.getDriver().quit();
                AppiumDriverLocalService appiumService = null;

                appiumService = engagedBOT.getAppiumService();
                appiumService.stop();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            } finally {
//                new DevicesServiceImpl().updateStatusToAvailableForDevice(engagedBOT.getDeviceUdid()); //TODO: Make device available in inmemory DS
            }
        }
    }


    public List<SmartBOT> registerSmartBOTs() throws IOException, InterruptedException, DeviceMatchingException, DeviceEngagedException {
        List<SmartBOT> smartBOTs = new ArrayList<>();
        List<DeviceAllocation> deviceAllocations = optimusConfigParser.allocateDevicesForCurrentScenario();
        for (DeviceAllocation allocatedDevice : deviceAllocations) {
            String udid = allocatedDevice.getDevice().getUdid();
            AppiumDriverLocalService appiumService = null;
            AppiumDriver driver = null;
            AppiumServerManager appiumServerManager = new AppiumServerManager(optimusConfigParser);
            appiumService = new AppiumServerManager(optimusConfigParser)
                    .startAppiumService(UUID.randomUUID().toString(), udid);
            driver = addDriver(appiumService.getUrl(), allocatedDevice.getCapabilities());
            String appPackage = (String) allocatedDevice.getCapabilities().getCapability("appPackage");

            SmartBOT bot = new SmartBOTBuilder()
                    .withBelongsTo(allocatedDevice.getOwner())
                    .withRunsOn(allocatedDevice.getDevice().getRunsOn())
                    .withCapabilities(allocatedDevice.getCapabilities())
                    .withDeviceUdid(udid)
                    .withDeviceId(allocatedDevice.getDevice().getId())
                    .withDriver(driver)
                    .withAppiumService(appiumService)
                    .withScenario(scenario)
                    .withAppPackageName(appPackage)
                    .build();
            smartBOTs.add(bot);
        }
        return smartBOTs;
    }


    public AppiumDriver addDriver(URL url, DesiredCapabilities capabilities) {

        try {
            return setUpDevice(url, capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Driver could not be created for -- " + capabilities.getCapability("udid"));
    }

    private static AppiumDriver setUpDevice(URL url, DesiredCapabilities capabilities) throws MalformedURLException {
        if (capabilities.getCapability("platformName").toString().equalsIgnoreCase("Android")) {
            return new AndroidDriver(url, capabilities);
        }
        return new IOSDriver(url, capabilities);
    }


}
