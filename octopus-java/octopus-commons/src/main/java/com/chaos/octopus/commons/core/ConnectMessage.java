/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.core;

import com.chaos.octopus.commons.util.StreamUtilities;

public class ConnectMessage extends Message {
  private String hostname;
  private int port;

  public ConnectMessage() {
  }

  public ConnectMessage(String hostname, int port) {
    setAction("connect");
    this.hostname = hostname;
    this.port = port;
  }

  public static ConnectMessage createFromJson(String json) {
    return StreamUtilities.ReadJson(json, ConnectMessage.class);
  }

  public String get_Hostname() {
    return hostname;
  }

  public int get_Port() {
    return port;
  }
}
