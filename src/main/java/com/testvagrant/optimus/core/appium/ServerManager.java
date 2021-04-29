package com.testvagrant.optimus.core.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.ServerArgument;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.SESSION_OVERRIDE;
import static org.awaitility.Awaitility.await;

public abstract class ServerManager {

  protected final ThreadLocal<AppiumDriverLocalService> appiumDriverLocalServiceThreadLocal;

  public ServerManager() {
    this.appiumDriverLocalServiceThreadLocal = new ThreadLocal<>();
  }

  public AppiumDriverLocalService startService(
      Map<ServerArgument, String> serverArguments, String udid) {
    boolean enableLogs = serverArguments.containsKey(OptimusServerFlag.ENABLE_CONSOLE_LOGS);
    AppiumDriverLocalService appiumService = buildAppiumService(udid, serverArguments);
    if (!enableLogs) appiumService.clearOutPutStreams();
    appiumService.start();
    await().atMost(5, TimeUnit.SECONDS).until(appiumService::isRunning);
    appiumDriverLocalServiceThreadLocal.set(appiumService);
    return appiumDriverLocalServiceThreadLocal.get();
  }

  private AppiumDriverLocalService buildAppiumService(
      String udid, Map<ServerArgument, String> serverArguments) {
    File logFile =
        new File(
            String.format(
                "logs/appium" + File.separator + "appium_%s.log",
                udid.replaceAll("-", "_").trim()));

    AppiumServiceBuilder appiumServiceBuilder =
        new AppiumServiceBuilder()
            .withArgument(SESSION_OVERRIDE)
            .usingAnyFreePort()
            .withLogFile(logFile)
            .withArgument(
                AndroidServerFlag.BOOTSTRAP_PORT_NUMBER,
                String.valueOf(aRandomOpenPortOnAllLocalInterfaces()))
            .withArgument(
                OptimusServerFlag.WDA_PORT, String.valueOf(aRandomOpenPortOnAllLocalInterfaces()));

    Map<ServerArgument, String> updatedServerArguments =
        ignoreOptimusServerArguments(serverArguments);

    updatedServerArguments.forEach(
        (argument, value) -> {
          if (value.isEmpty()) {
            appiumServiceBuilder.withArgument(argument);
          } else {
            appiumServiceBuilder.withArgument(argument, value);
          }
        });

    return AppiumDriverLocalService.buildService(appiumServiceBuilder);
  }

  private Integer aRandomOpenPortOnAllLocalInterfaces() {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    } catch (IOException e) {
      return 0;
    }
  }

  private Map<ServerArgument, String> ignoreOptimusServerArguments(
      Map<ServerArgument, String> serverArguments) {
    ServerArgument[] serverArgArray = {
      SESSION_OVERRIDE,
      AndroidServerFlag.BOOTSTRAP_PORT_NUMBER,
      OptimusServerFlag.WDA_PORT,
      OptimusServerFlag.ENABLE_CONSOLE_LOGS
    };
    Arrays.stream(serverArgArray).forEach(serverArguments::remove);
    return serverArguments;
  }
}
