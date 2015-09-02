/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.core;

import com.chaos.octopus.commons.core.message.Message;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AgentConfigurationMessage extends Message {
  private int numberOfSimulataniousTasks = 0;
  private ArrayList<String> supportedPlugins = new ArrayList<>();

  public AgentConfigurationMessage() {

  }

  public static AgentConfigurationMessage create(String config) {
    return new Gson().fromJson(config, AgentConfigurationMessage.class);
  }

  public int getNumberOfSimulataniousTasks() {
    return numberOfSimulataniousTasks;
  }

  public void setNumberOfSimulataniousTasks(int numberOfSimulataniousTasks) {
    this.numberOfSimulataniousTasks = numberOfSimulataniousTasks;
  }

  public ArrayList<String> getSupportedPlugins() {
    return supportedPlugins;
  }

  public void setSupportedPlugins(ArrayList<String> supportedPlugins) {
    this.supportedPlugins = supportedPlugins;
  }

  public String toJson() {
    return new Gson().toJson(this);
  }
}
