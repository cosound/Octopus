/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.core.message.ConnectMessage;
import com.chaos.octopus.commons.core.message.TaskMessage;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.NetworkingUtil;
import com.chaos.octopus.commons.util.StreamUtilities;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

public class OrchestratorProxy implements Orchestrator {
  private int _localListenPort;
  private String _localHostAddress;
  private NetworkingUtil _network;

  public OrchestratorProxy(String hostname, int port, int listenPort) {
    _localHostAddress = getHostAddress();
    _localListenPort = listenPort;
    _network = new NetworkingUtil(hostname, port);
  }

  private String getHostAddress() {
    try {
      String hostAddress = Inet4Address.getLocalHost().getHostAddress();

      System.out.println("Local IP: " + hostAddress);

      return hostAddress;
    } catch (UnknownHostException e) {
      throw new com.chaos.octopus.commons.exception.ConnectException("Could not determine local host address", e);
    }
  }

  public void open() throws ConnectException {
    ConnectMessage msg = new ConnectMessage(_localHostAddress, _localListenPort);

    _network.send(msg.toJson());
  }

  public void taskCompleted(Task task) {
    try(Socket socket = new Socket("localhost", 8080)) {
      String taskString = new Gson().toJson(task);

      socket.getOutputStream().write(String.format("GET /Task/Complete/?task=%1s HTTP/1.1", taskString).getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void taskUpdate(Task task) {
    try(Socket socket = new Socket("localhost", 8080)) {
      String taskString = new Gson().toJson(task);

      socket.getOutputStream().write(String.format("GET /Task/Update/?task=%1s HTTP/1.1", taskString).getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int get_localListenPort() {
    return _localListenPort;
  }

  @Override
  public void enqueue(Job job) {

  }

  @Override
  public void close() throws Exception {

  }
}
