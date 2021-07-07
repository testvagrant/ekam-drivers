package com.testvagrant.ekam.drivers.mobile;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.SESSION_OVERRIDE;
import static org.awaitility.Awaitility.await;

public class ServerManager {

  protected final ThreadLocal<AppiumDriverLocalService> appiumDriverLocalServiceThreadLocal;

  public ServerManager() {
    this.appiumDriverLocalServiceThreadLocal = new ThreadLocal<>();
  }

  public AppiumDriverLocalService startService(Map<ServerArgument, String> serverArguments) {
    try {
      boolean enableLogs = serverArguments.containsKey(AppiumServerFlags.ENABLE_CONSOLE_LOGS);

      AppiumDriverLocalService appiumService = buildAppiumService(serverArguments);
      if (!enableLogs) appiumService.clearOutPutStreams();

      appiumService.start();
      await().atMost(5, TimeUnit.SECONDS).until(appiumService::isRunning);
      appiumDriverLocalServiceThreadLocal.set(appiumService);
      return appiumDriverLocalServiceThreadLocal.get();
    } catch (Exception ex) {
      throw new RuntimeException("Unable to start Appium service.\nError:" + ex.getMessage());
    }
  }

  private AppiumDriverLocalService buildAppiumService(Map<ServerArgument, String> serverArguments) {
    AppiumServiceBuilder appiumServiceBuilder =
        new AppiumServiceBuilder()
            .withArgument(SESSION_OVERRIDE)
            .usingAnyFreePort()
            .withArgument(
                AndroidServerFlag.BOOTSTRAP_PORT_NUMBER,
                String.valueOf(randomOpenPortOnAllLocalInterfaces()))
            .withArgument(
                AppiumServerFlags.WDA_PORT, String.valueOf(randomOpenPortOnAllLocalInterfaces()));

    List<String> serverArgArray =
        Arrays.asList(
            SESSION_OVERRIDE.name(),
            AndroidServerFlag.BOOTSTRAP_PORT_NUMBER.name(),
            AppiumServerFlags.WDA_PORT.name(),
            AppiumServerFlags.ENABLE_CONSOLE_LOGS.name());

    Map<ServerArgument, String> serverArgs =
        serverArguments.entrySet().stream()
            .filter(
                serverArgument ->
                    !serverArgArray.contains(serverArgument.getKey().toString().trim()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    serverArgs.forEach(
        (argument, value) -> {
          if (value.isEmpty()) {
            appiumServiceBuilder.withArgument(argument);
          } else {
            appiumServiceBuilder.withArgument(argument, value);
          }
        });

    return AppiumDriverLocalService.buildService(appiumServiceBuilder);
  }

  private Integer randomOpenPortOnAllLocalInterfaces() {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException("No open ports available to start Appium service");
    }
  }
}
