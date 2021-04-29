package com.testvagrant.optimus.api;

import okhttp3.OkHttpClient;

public class BasicAuthRetrofitClient {
  private final String username, password;

  public BasicAuthRetrofitClient(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public OkHttpClient build() {
    return new OkHttpClient.Builder()
        .addInterceptor(new BasicAuthInterceptor(username, password))
        .build();
  }
}
