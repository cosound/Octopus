/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.server.messageSpecification;

import com.chaos.octopus.commons.core.message.ConnectMessage;
import com.chaos.octopus.commons.exception.ConnectException;
import com.chaos.octopus.server.AgentProxy;
import com.chaos.octopus.server.AllocationHandler;

public class ConnectSpecification {
  private final AllocationHandler allocationHandler;

  public ConnectSpecification(AllocationHandler allocationHandler) {
    this.allocationHandler = allocationHandler;
  }

  public void invoke(String json) {
    ConnectMessage connect = ConnectMessage.createFromJson(json);

    try {
      AgentProxy ap = new AgentProxy(connect.get_Hostname(), connect.get_Port());
      ap.InitializeAgent();

      allocationHandler.addAgent(ap);
    } catch (ConnectException e) {
      System.err.println("Connection to Agent could not be established, hostname: " + connect.get_Hostname() + ", port: " + connect.get_Port());
      e.printStackTrace();
    }
  }
}
