/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.commons.core.*;
import com.chaos.octopus.commons.util.Commands;
import com.chaos.octopus.commons.util.NetworkingUtil;

import java.net.ConnectException;
import java.net.Inet4Address;
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
    TaskMessage msg = new TaskMessage(Commands.TASK_DONE, task);

    _network.send(msg.toJson());
  }

  @Override
  public void taskUpdate(Task task) {
    TaskMessage msg = new TaskMessage(Commands.TASK_UPDATE, task);

    _network.send(msg.toJson());
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
