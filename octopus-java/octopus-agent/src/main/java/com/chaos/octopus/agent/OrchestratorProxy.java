/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.agent;

import com.chaos.octopus.commons.core.*;
import com.google.gson.Gson;

import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

public class OrchestratorProxy implements Orchestrator {
  private final int port;
  private final String hostname;
  private int _localListenPort;
  private String _localHostAddress;

  public OrchestratorProxy(String hostname, int port, int listenPort) {
    _localHostAddress = getHostAddress();
    _localListenPort = listenPort;
    this.port = port;
    this.hostname = hostname;
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
    sendResponse("Agent/Connect",
        new KeyValue("hostname", _localHostAddress),
        new KeyValue("port", _localListenPort + ""));
  }

  public void taskCompleted(Task task) {
    String taskString = new Gson().toJson(task);

    sendResponse("Task/Complete", new KeyValue("task", taskString));
  }

  public void taskUpdate(Task task) {
    String taskString = new Gson().toJson(task);

    sendResponse("Task/Update", new KeyValue("task", taskString));
  }

  private void sendResponse(String endpoint, KeyValue... parameters) {
    String queryString = "";

    for (KeyValue entry: parameters)
      queryString += String.format("%1s=%2s&", entry.key, entry.value);

    sendResponse(String.format("GET /%1s/?%2s HTTP/1.1", endpoint, queryString), 10);
  }

  private void sendResponse(String message, int retries) {
    try(Socket socket = new Socket(hostname, port)) {
      socket.getOutputStream().write(message.getBytes());
    } catch (ConnectException e) {
      throw new com.chaos.octopus.commons.exception.ConnectException("Connection to Orchestrator could not be established, check hostname and port", e);
    } catch (Exception e) {
      if (retries > 0) {
        sleep(250);
        sendResponse(message, --retries);
      }

      // TODO This exception should be handled at a higher level
      System.err.println("Couldn't connect to: " + "" + ":" + "");
      e.printStackTrace();
    }
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e1) {

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
