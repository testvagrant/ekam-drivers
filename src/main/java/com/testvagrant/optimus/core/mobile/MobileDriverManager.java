package com.testvagrant.optimus.core.mobile;

import com.testvagrant.optimus.core.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.models.mobile.DeviceFilters;
import com.testvagrant.optimus.core.models.mobile.MobileDriverDetails;
import com.testvagrant.optimus.core.parser.TestFeedParser;
import com.testvagrant.optimus.core.remote.CloudConfigBuilder;
import com.testvagrant.optimus.core.remote.RemoteUrlBuilder;
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
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MobileDriverManager extends ServerManager {

  private final DesiredCapabilities desiredCapabilities;
  private final DeviceFilters deviceFilters;
  private DeviceManager deviceManager;
  private final Map<ServerArgument, String> serverArguments;

  private static final ThreadLocal<AppiumDriver<MobileElement>> driverThreadLocal =
      new ThreadLocal<>();
  private static final ThreadLocal<AppiumDriverLocalService> serviceThreadLocal =
      new ThreadLocal<>();
  private static final ThreadLocal<TargetDetails> targetDetailsThreadLocal = new ThreadLocal<>();

  public MobileDriverManager() {
    TestFeedParser testFeedParser = new TestFeedParser(System.getProperty("testFeed"));
    this.deviceManager = DeviceManagerProvider.getInstance();
    this.desiredCapabilities = testFeedParser.getDesiredCapabilities();
    this.serverArguments = testFeedParser.getServerArgumentsMap();
    this.deviceFilters = testFeedParser.getDeviceFilters();
  }

  public MobileDriverManager(DesiredCapabilities desiredCapabilities) {
    this(desiredCapabilities, new HashMap<>());
  }

  public MobileDriverManager(
      DesiredCapabilities desiredCapabilities, Map<ServerArgument, String> serverArguments) {
    this(desiredCapabilities, serverArguments, new DeviceFilters());
  }

  public MobileDriverManager(DesiredCapabilities desiredCapabilities, DeviceFilters deviceFilters) {
    this(desiredCapabilities, new HashMap<>(), deviceFilters);
  }

  public MobileDriverManager(
      DesiredCapabilities desiredCapabilities,
      Map<ServerArgument, String> serverArguments,
      DeviceFilters deviceFilters) {
    this.desiredCapabilities = desiredCapabilities;
    this.serverArguments = serverArguments;
    this.deviceFilters = deviceFilters;
  }

  public MobileDriverDetails createDriverDetails() {
    String target = System.getProperty("target", "local").toLowerCase();
    return target.equals("remote") ? createRemoteDriver() : createLocalDriver();
  }

  public static void dispose() {
    if (driverThreadLocal.get() != null) {
      driverThreadLocal.get().quit();
    }

    if (serviceThreadLocal.get() != null) {
      serviceThreadLocal.get().stop();
    }

    if (targetDetailsThreadLocal.get() != null) {
      DeviceManagerProvider.getInstance().releaseDevice(targetDetailsThreadLocal.get());
    }
  }

  private MobileDriverDetails createRemoteDriver() {
    URL url = RemoteUrlBuilder.build(new CloudConfigBuilder().build());
    driverThreadLocal.set(createAppiumDriver(url));
    return buildMobileDriverDetails(driverThreadLocal.get(), null);
  }

  private MobileDriverDetails createLocalDriver() {
    Predicate<TargetDetails> filters =
        new DeviceFiltersManager().createDeviceFilters(desiredCapabilities, deviceFilters);

    TargetDetails availableDevice = deviceManager.getAvailableDevice(filters);
    targetDetailsThreadLocal.set(availableDevice);
    System.out.println(availableDevice);

    desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, availableDevice.getName());
    desiredCapabilities.setCapability(MobileCapabilityType.UDID, availableDevice.getUdid());
    desiredCapabilities.setCapability(
        MobileCapabilityType.PLATFORM_VERSION, availableDevice.getPlatformVersion());

    AppiumDriverLocalService service = startService(serverArguments, availableDevice.getUdid());
    serviceThreadLocal.set(service);
    driverThreadLocal.set(createAppiumDriver(service.getUrl()));

    return buildMobileDriverDetails(driverThreadLocal.get(), service);
  }

  private AppiumDriver<MobileElement> createAppiumDriver(URL url) {
    return desiredCapabilities.getPlatform() == Platform.IOS
        ? new IOSDriver<>(url, desiredCapabilities)
        : new AndroidDriver<>(url, desiredCapabilities);
  }

  private MobileDriverDetails buildMobileDriverDetails(
      AppiumDriver<MobileElement> appiumDriver, AppiumDriverLocalService service) {
    Capabilities capabilities = appiumDriver.getCapabilities();

    String platform = (String) capabilities.getCapability(MobileCapabilityType.PLATFORM_NAME);

    OptimusSupportedPlatforms optimusPlatform =
        Arrays.stream(OptimusSupportedPlatforms.values())
            .filter(
                optimusSupportedPlatform ->
                    optimusSupportedPlatform.name().equals(platform.toUpperCase()))
            .findFirst()
            .orElseThrow(UnsupportedPlatform::new);

    TargetDetails targetDetails =
        TargetDetails.builder()
            .platform(optimusPlatform)
            .platformVersion(
                capabilities.getCapability(MobileCapabilityType.PLATFORM_VERSION).toString())
            .name(capabilities.getCapability("deviceModel").toString())
            .udid(capabilities.getCapability(MobileCapabilityType.UDID).toString())
            .build();

    return MobileDriverDetails.builder()
        .capabilities(capabilities)
        .targetDetails(targetDetails)
        .driver(appiumDriver)
        .service(service)
        .build();
  }
}
