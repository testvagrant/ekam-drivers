package com.testvagrant.optimus.api.browserstack;

import com.testvagrant.optimus.core.models.mobile.BrowserStackDeviceDetails;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface BrowserStackService {

  @GET("app-automate/devices.json")
  Call<List<BrowserStackDeviceDetails>> browserStackDevices();
}
