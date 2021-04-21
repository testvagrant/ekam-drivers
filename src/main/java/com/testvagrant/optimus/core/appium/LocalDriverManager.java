package com.testvagrant.optimus.core.appium;

import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.core.models.mobile.DeviceFilters;
import com.testvagrant.optimus.core.models.mobile.MobileDriverDetails;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import com.testvagrant.optimus.devicemanager.DeviceFiltersManager;
import com.testvagrant.optimus.devicemanager.DeviceManager;
import com.testvagrant.optimus.devicemanager.DeviceManagerProvider;
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

public class LocalDriverManager extends ServerManager {

  private final DesiredCapabilities desiredCapabilities;
  private final DeviceFilters deviceFilters;
  private DeviceManager deviceManager;
  private final Map<ServerArgument, String> serverArguments;

  public LocalDriverManager() {
    TestFeedParser testFeedParser = new TestFeedParser(System.getProperty("testFeed"));
    this.deviceManager = DeviceManagerProvider.getInstance();
    this.desiredCapabilities = testFeedParser.getDesiredCapabilities();
    this.serverArguments = testFeedParser.getServerArgumentsMap();
    this.deviceFilters = testFeedParser.getDeviceFilters();
  }

  public LocalDriverManager(DesiredCapabilities desiredCapabilities) {
    this(desiredCapabilities, new HashMap<>());
  }

  public LocalDriverManager(
      DesiredCapabilities desiredCapabilities, Map<ServerArgument, String> serverArguments) {
    this(desiredCapabilities, serverArguments, new DeviceFilters());
  }

  public LocalDriverManager(DesiredCapabilities desiredCapabilities, DeviceFilters deviceFilters) {
    this(desiredCapabilities, new HashMap<>(), deviceFilters);
  }

  public LocalDriverManager(
      DesiredCapabilities desiredCapabilities,
      Map<ServerArgument, String> serverArguments,
      DeviceFilters deviceFilters) {
    this.desiredCapabilities = desiredCapabilities;
    this.serverArguments = serverArguments;
    this.deviceFilters = deviceFilters;
  }

  public MobileDriverDetails createDriver() {
    MobileDriverDetails mobileDriverDetails = new MobileDriverDetails();

    Predicate<DeviceDetails> filters =
        new DeviceFiltersManager().createDeviceFilters(desiredCapabilities, deviceFilters);

    DeviceDetails availableDevice = deviceManager.getAvailableDevice(filters);
    System.out.println(availableDevice);
    updateDesiredCapabilitiesWithDeviceDetails(availableDevice);

    AppiumDriverLocalService service = startService(serverArguments, availableDevice.getUdid());

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

  public static void dispose(MobileDriverDetails mobileDriverDetails) {
    DeviceManagerProvider.getInstance().releaseDevice(mobileDriverDetails.getDeviceDetails());
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
