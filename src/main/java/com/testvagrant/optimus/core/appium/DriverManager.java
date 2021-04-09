package com.testvagrant.optimus.core.appium;

import com.testvagrant.optimus.commons.SystemProperties;
import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.core.model.MobileDriverDetails;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import com.testvagrant.optimus.devicemanager.DeviceFiltersManager;
import com.testvagrant.optimus.devicemanager.DeviceManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class DriverManager extends ServerManager {

  private final DesiredCapabilities desiredCapabilities;
  private DeviceManager deviceManager;
  private Map<ServerArgument, String> serverArguments;

  public DriverManager(DeviceManager deviceManager) {
    TestFeedParser testFeedParser = new TestFeedParser(SystemProperties.TESTFEED);
    this.deviceManager = deviceManager;
    this.desiredCapabilities = testFeedParser.getDesiredCapabilities();
    this.serverArguments = testFeedParser.getServerArgumentsMap();
  }

  public DriverManager(DesiredCapabilities desiredCapabilities) {
    this.desiredCapabilities = desiredCapabilities;
    this.serverArguments = new HashMap<>();
  }

  public DriverManager(DesiredCapabilities desiredCapabilities, Map<ServerArgument, String> serverArguments) {
    this.desiredCapabilities = desiredCapabilities;
    this.serverArguments = serverArguments;
  }

  public MobileDriverDetails createDriver() {
    MobileDriverDetails mobileDriverDetails = new MobileDriverDetails();

    Predicate<DeviceDetails> deviceFilters =
        new DeviceFiltersManager().createDeviceFilters(desiredCapabilities);

    DeviceDetails availableDevice = deviceManager.getAvailableDevice(deviceFilters);
    updateDesiredCapabilitiesWithDeviceDetails(availableDevice);

    AppiumDriverLocalService service = startService(serverArguments);

    AppiumDriver<MobileElement> driver =
        desiredCapabilities.getPlatform() == Platform.IOS
            ? new IOSDriver<>(service.getUrl(), desiredCapabilities)
            : new AndroidDriver<>(service.getUrl(), desiredCapabilities);

    return mobileDriverDetails.toBuilder()
        .capabilities(desiredCapabilities)
        .driver(driver)
        .deviceDetails(availableDevice)
        .service(service)
        .build();
  }

  @Override
  public void dispose(MobileDriverDetails mobileDriverDetails) {
    deviceManager.releaseDevice(mobileDriverDetails.getDeviceDetails());
    mobileDriverDetails.getDriver().quit();
    mobileDriverDetails.getService().stop();
  }

  private void updateDesiredCapabilitiesWithDeviceDetails(DeviceDetails availableDevice) {
    desiredCapabilities.setCapability(
        MobileCapabilityType.DEVICE_NAME, availableDevice.getDeviceName());
    desiredCapabilities.setCapability(MobileCapabilityType.UDID, availableDevice.getUdid());
    desiredCapabilities.setCapability(
        MobileCapabilityType.PLATFORM_VERSION, availableDevice.getPlatformVersion());
  }
}
