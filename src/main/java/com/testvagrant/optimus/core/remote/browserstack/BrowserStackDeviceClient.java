package com.testvagrant.optimus.core.remote.browserstack;

import com.testvagrant.ekam.api.retrofit.RetrofitBaseClient;
import com.testvagrant.ekam.api.retrofit.interceptors.BasicAuthInterceptor;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.TargetDetails;
import com.testvagrant.optimus.core.models.mobile.BrowserStackDeviceDetails;
import com.testvagrant.optimus.core.models.mobile.DeviceType;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrowserStackDeviceClient extends RetrofitBaseClient {

  private final BrowserStackService browserStackService;

  public BrowserStackDeviceClient(CloudConfig cloudConfig) {
    super(
        "https://api-cloud.browserstack.com",
        new BasicAuthInterceptor(cloudConfig.getUsername(), cloudConfig.getAccessKey()));
    browserStackService = httpClient.getService(BrowserStackService.class);
  }

  public List<TargetDetails> getAndroidDevices() {
    List<BrowserStackDeviceDetails> browserStackDevices =
        getDevices().stream()
            .filter(TargetDetails -> TargetDetails.getOs().equals("android"))
            .collect(Collectors.toList());

    return populateTargetDetails(browserStackDevices);
  }

  public List<TargetDetails> getIosDevices() {
    List<BrowserStackDeviceDetails> browserStackDevices =
        getDevices().stream()
            .filter(TargetDetails -> TargetDetails.getOs().equals("ios"))
            .collect(Collectors.toList());

    return populateTargetDetails(browserStackDevices);
  }

  private List<BrowserStackDeviceDetails> getDevices() {
    Call<List<BrowserStackDeviceDetails>> responseCall = browserStackService.browserStackDevices();
    Response<List<BrowserStackDeviceDetails>> response = httpClient.executeAsResponse(responseCall);
    if (response.body() == null) {
      throw new RuntimeException("Couldn't get device list from browserstack");
    }

    return response.body();
  }

  private List<TargetDetails> populateTargetDetails(
      List<BrowserStackDeviceDetails> browserStackDevices) {
    List<TargetDetails> devices = new ArrayList<>();
    browserStackDevices.forEach(
        device -> {
          OptimusSupportedPlatforms platform =
              OptimusSupportedPlatforms.valueOf(device.getOs().toUpperCase().trim());

          DeviceType deviceType =
              device.isRealMobile()
                  ? DeviceType.DEVICE
                  : platform.equals(OptimusSupportedPlatforms.IOS)
                      ? DeviceType.SIMULATOR
                      : DeviceType.EMULATOR;

          TargetDetails targetDetails =
              TargetDetails.builder()
                  .name(device.getDevice().trim())
                  .platformVersion(device.getOs_version().trim())
                  .platform(platform)
                  .runsOn(deviceType)
                  .udid("")
                  .build();

          devices.add(targetDetails);
        });

    return devices;
  }
}
