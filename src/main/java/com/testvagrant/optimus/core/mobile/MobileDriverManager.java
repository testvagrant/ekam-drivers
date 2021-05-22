package com.testvagrant.optimus.core.mobile;

import com.testvagrant.optimus.commons.SystemProperties;
import com.testvagrant.optimus.core.exceptions.UnsupportedPlatform;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.models.mobile.DeviceFilters;
import com.testvagrant.optimus.core.models.mobile.DeviceType;
import com.testvagrant.optimus.core.models.mobile.MobileDriverDetails;
import com.testvagrant.optimus.core.models.web.SiteConfig;
import com.testvagrant.optimus.core.parser.MobileTestFeedParser;
import com.testvagrant.optimus.core.remote.CloudConfigBuilder;
import com.testvagrant.optimus.core.remote.RemoteUrlBuilder;
import com.testvagrant.optimus.devicemanager.BrowserstackDeviceManager;
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
import java.util.Objects;
import java.util.function.Predicate;

public class MobileDriverManager extends ServerManager {

  private final DesiredCapabilities desiredCapabilities;
  private final DeviceFilters deviceFilters;
  private final Map<ServerArgument, String> serverArguments;
  private final SiteConfig siteConfig;

  private static final ThreadLocal<AppiumDriver<MobileElement>> driverThreadLocal =
      new ThreadLocal<>();
  private static final ThreadLocal<AppiumDriverLocalService> serviceThreadLocal =
      new ThreadLocal<>();
  private static final ThreadLocal<TargetDetails> targetDetailsThreadLocal = new ThreadLocal<>();

  public MobileDriverManager() {
    MobileTestFeedParser mobileTestFeedParser =
        new MobileTestFeedParser(SystemProperties.MOBILE_FEED);
    this.desiredCapabilities = mobileTestFeedParser.getDesiredCapabilities();
    this.serverArguments = mobileTestFeedParser.getServerArgumentsMap();
    this.deviceFilters = mobileTestFeedParser.getDeviceFilters();
    this.siteConfig = mobileTestFeedParser.getSiteConfig();
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
    this(desiredCapabilities, serverArguments, deviceFilters, null);
  }

  public MobileDriverManager(
          DesiredCapabilities desiredCapabilities,
          Map<ServerArgument, String> serverArguments,
          DeviceFilters deviceFilters,
          SiteConfig siteConfig) {
    this.desiredCapabilities = desiredCapabilities;
    this.serverArguments = serverArguments;
    this.deviceFilters = deviceFilters;
    this.siteConfig = siteConfig;
  }

  public MobileDriverDetails createDriverDetails() {
    String target = SystemProperties.RUN_MODE.toLowerCase();
    MobileDriverDetails mobileDriverDetails;
    if(target.equals("remote")) {
      mobileDriverDetails = createRemoteDriver();
    } else {
      mobileDriverDetails = createLocalDriver();
    }
    launchSiteForMobileWeb(mobileDriverDetails);
    return mobileDriverDetails;
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
    CloudConfig cloudConfig = new CloudConfigBuilder().build();
    URL url = RemoteUrlBuilder.build(cloudConfig);

    if (cloudConfig.getHub().toLowerCase().contains("browserstack")) {
      updateBrowserStackDesiredCapabilitiesWithTargetDetails();
    }

    driverThreadLocal.set(createAppiumDriver(url));
    return buildMobileDriverDetails(driverThreadLocal.get(), null, DeviceType.DEVICE);
  }

  private MobileDriverDetails createLocalDriver() {
    DeviceManager deviceManager = DeviceManagerProvider.getInstance();
    Predicate<TargetDetails> filters =
        new DeviceFiltersManager().createDeviceFilters(desiredCapabilities, deviceFilters);

    TargetDetails availableDevice = deviceManager.getAvailableDevice(filters);
    targetDetailsThreadLocal.set(availableDevice);
    System.out.println(availableDevice);

    desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, availableDevice.getName());
    desiredCapabilities.setCapability(MobileCapabilityType.UDID, availableDevice.getUdid());
    desiredCapabilities.setCapability(
        MobileCapabilityType.PLATFORM_VERSION, availableDevice.getPlatformVersion());

    AppiumDriverLocalService service = startService(serverArguments,
            availableDevice.getUdid(),
            getBrowserName());
    serviceThreadLocal.set(service);
    driverThreadLocal.set(createAppiumDriver(service.getUrl()));

    return buildMobileDriverDetails(driverThreadLocal.get(), service, availableDevice.getRunsOn());
  }

  private AppiumDriver<MobileElement> createAppiumDriver(URL url) {
    return desiredCapabilities.getPlatform() == Platform.IOS
        ? new IOSDriver<>(url, desiredCapabilities)
        : new AndroidDriver<>(url, desiredCapabilities);
  }

  private MobileDriverDetails buildMobileDriverDetails(
      AppiumDriver<MobileElement> appiumDriver,
      AppiumDriverLocalService service,
      DeviceType deviceType) {
    Capabilities capabilities = appiumDriver.getCapabilities();

    TargetDetails targetDetails =
        TargetDetails.builder()
            .platform(getPlatform())
            .platformVersion(
                (String) capabilities.getCapability(MobileCapabilityType.PLATFORM_VERSION))
            .name((String) capabilities.getCapability(MobileCapabilityType.DEVICE_NAME))
            .udid((String) capabilities.getCapability(MobileCapabilityType.UDID))
            .runsOn(deviceType)
            .build();

    return MobileDriverDetails.builder()
        .capabilities(capabilities)
        .targetDetails(targetDetails)
        .driver(appiumDriver)
        .service(service)
        .build();
  }

  private void updateBrowserStackDesiredCapabilitiesWithTargetDetails() {
    DeviceFilters deviceFilters =
        new MobileTestFeedParser(SystemProperties.MOBILE_FEED).getDeviceFilters();

    Predicate<TargetDetails> filters =
        new DeviceFiltersManager().createDeviceFilters(desiredCapabilities, deviceFilters);

    TargetDetails deviceDetails =
        BrowserstackDeviceManager.getInstance(getPlatform()).getDevice(filters);

    System.out.println("Device details: " + deviceDetails.toString());

    desiredCapabilities.setCapability("device", deviceDetails.getName());
    desiredCapabilities.setCapability("os_version", deviceDetails.getPlatformVersion());
  }

  private OptimusSupportedPlatforms getPlatform() {
    String platform =
        (String) desiredCapabilities.getCapability(MobileCapabilityType.PLATFORM_NAME);

    return Arrays.stream(OptimusSupportedPlatforms.values())
        .filter(
            optimusSupportedPlatform ->
                optimusSupportedPlatform.name().equals(platform.toUpperCase()))
        .findFirst()
        .orElseThrow(UnsupportedPlatform::new);
  }

  private void launchSiteForMobileWeb(MobileDriverDetails mobileDriverDetails) {
    if(isMobileWeb()) {
      mobileDriverDetails.getDriver().get(siteConfig.getUrl());
    }
  }

  private boolean isMobileWeb() {
    return Objects.nonNull(siteConfig);
  }

  private String getBrowserName() {
    return isMobileWeb()? desiredCapabilities.getCapability(MobileCapabilityType.BROWSER_NAME).toString(): "";
  }
}
