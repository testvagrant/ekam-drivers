package com.testvagrant.optimus.core.appium;

import com.testvagrant.optimus.commons.entities.TargetDetails;
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

public class LocalMobileDriverManager extends ServerManager {

  private final DesiredCapabilities desiredCapabilities;
  private final DeviceFilters deviceFilters;
  private DeviceManager deviceManager;
  private final Map<ServerArgument, String> serverArguments;

  public LocalMobileDriverManager() {
    TestFeedParser testFeedParser = new TestFeedParser(System.getProperty("testFeed"));
    this.deviceManager = DeviceManagerProvider.getInstance();
    this.desiredCapabilities = testFeedParser.getDesiredCapabilities();
    this.serverArguments = testFeedParser.getServerArgumentsMap();
    this.deviceFilters = testFeedParser.getDeviceFilters();
  }

  public LocalMobileDriverManager(DesiredCapabilities desiredCapabilities) {
    this(desiredCapabilities, new HashMap<>());
  }

  public LocalMobileDriverManager(
      DesiredCapabilities desiredCapabilities, Map<ServerArgument, String> serverArguments) {
    this(desiredCapabilities, serverArguments, new DeviceFilters());
  }

  public LocalMobileDriverManager(DesiredCapabilities desiredCapabilities, DeviceFilters deviceFilters) {
    this(desiredCapabilities, new HashMap<>(), deviceFilters);
  }

  public LocalMobileDriverManager(
      DesiredCapabilities desiredCapabilities,
      Map<ServerArgument, String> serverArguments,
      DeviceFilters deviceFilters) {
    this.desiredCapabilities = desiredCapabilities;
    this.serverArguments = serverArguments;
    this.deviceFilters = deviceFilters;
  }

  public MobileDriverDetails createDriver() {
    MobileDriverDetails mobileDriverDetails = new MobileDriverDetails();

    Predicate<TargetDetails> filters =
        new DeviceFiltersManager().createDeviceFilters(desiredCapabilities, deviceFilters);

    TargetDetails availableDevice = deviceManager.getAvailableDevice(filters);
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
        .service(service)
        .targetDetails(availableDevice)
        .build();
  }

  public static void dispose(MobileDriverDetails mobileDriverDetails) {
    DeviceManagerProvider.getInstance().releaseDevice(mobileDriverDetails.getTargetDetails());
    mobileDriverDetails.getDriver().quit();
    mobileDriverDetails.getService().stop();
  }

  private void updateDesiredCapabilitiesWithDeviceDetails(TargetDetails availableDevice) {
    desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, availableDevice.getName());
    desiredCapabilities.setCapability(MobileCapabilityType.UDID, availableDevice.getUdid());
    desiredCapabilities.setCapability(
        MobileCapabilityType.PLATFORM_VERSION, availableDevice.getPlatformVersion());
  }
}
