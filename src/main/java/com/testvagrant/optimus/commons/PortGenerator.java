package com.testvagrant.optimus.commons;

import java.io.IOException;
import java.net.ServerSocket;

public class PortGenerator {

  public static Integer aRandomOpenPortOnAllLocalInterfaces() {
    try (ServerSocket socket = new ServerSocket(0); ) {
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException("no open ports found for bootstrap");
    }
  }
}
