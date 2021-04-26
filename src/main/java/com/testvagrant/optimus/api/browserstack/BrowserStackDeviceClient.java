package com.testvagrant.optimus.api.browserstack;

import com.testvagrant.ekam.api.retrofit.RetrofitClient;
import com.testvagrant.optimus.api.BasicAuthRetrofitClient;
import com.testvagrant.optimus.commons.entities.DeviceDetails;
import com.testvagrant.optimus.commons.entities.DeviceType;
import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.mobile.BrowserStackDeviceDetails;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BrowserStackDeviceClient extends RetrofitClient {

  private final BrowserStackService browserStackService;

  public BrowserStackDeviceClient(CloudConfig cloudConfig) {
    super(
        new BasicAuthRetrofitClient(cloudConfig.getUsername(), cloudConfig.getAccessKey()).build());
    browserStackService =
        build("https://api-cloud.browserstack.com").create(BrowserStackService.class);
  }

  public List<DeviceDetails> getAndroidDevices() {
    List<BrowserStackDeviceDetails> browserStackDevices =
        getDevices().stream()
            .filter(deviceDetails -> deviceDetails.getOs().equals("android"))
            .collect(Collectors.toList());

    return populateDeviceDetails(browserStackDevices);
  }

  public List<DeviceDetails> getIosDevices() {
    List<BrowserStackDeviceDetails> browserStackDevices =
        getDevices().stream()
            .filter(deviceDetails -> deviceDetails.getOs().equals("android"))
            .collect(Collectors.toList());

    return populateDeviceDetails(browserStackDevices);
  }

  private List<BrowserStackDeviceDetails> getDevices() {
    Call<List<BrowserStackDeviceDetails>> responseCall = browserStackService.browserStackDevices();
    Response<List<BrowserStackDeviceDetails>> response = executeAsResponse(responseCall);
    if (response.body() == null) {
      throw new RuntimeException("Couldn't get device list from browserstack");
    }

    return response.body();
  }

  private List<DeviceDetails> populateDeviceDetails(
      List<BrowserStackDeviceDetails> browserStackDevices) {
    List<DeviceDetails> devices = new ArrayList<>();
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

          DeviceDetails deviceDetails =
              DeviceDetails.builder()
                  .deviceName(device.getDevice().trim())
                  .platformVersion(device.getOs_version().trim())
                  .platform(platform)
                  .runsOn(deviceType)
                  .udid("")
                  .build();

          devices.add(deviceDetails);
        });

    return devices;
  }
}
