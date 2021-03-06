/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.core;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.nio.file.Paths;

public class OctopusConfiguration {
  private int port;

  private String orchestratorIp = null;
  private int orchestratorPort = 0;
  private int numberOfParallelTasks = 8;
  private String chaosApiUrl = null;
  private String chaosApiKey = null;

  public OctopusConfiguration() throws ConfigurationException {
    String wd = System.getProperty("user.dir");
    String path = Paths.get(wd, "octopus.properties").toString();
    Configuration config = new PropertiesConfiguration(path);
    port = config.getInt("listening.port");

    if (config.containsKey("agent.numberOfParallelTasks"))
      numberOfParallelTasks = config.getInt("agent.numberOfParallelTasks");

    if (config.containsKey("orchestrator.ip"))
      orchestratorIp = config.getString("orchestrator.ip");

    if (config.containsKey("orchestrator.port"))
      orchestratorPort = config.getInt("orchestrator.port");

    if (config.containsKey("orchestrator.chaosApiUrl"))
      chaosApiUrl = config.getString("orchestrator.chaosApiUrl");

    if (config.containsKey("orchestrator.chaosApiKey"))
      chaosApiKey = config.getString("orchestrator.chaosApiKey");
  }

  public OctopusConfiguration(boolean useFile) throws ConfigurationException {

  }

  public int getListeningPort() {
    return port;
  }

  public String getOrchestratorIp() {
    return orchestratorIp;
  }

  public int getOrchestratorPort() {
    return orchestratorPort;
  }

  public Boolean getIsAgent() {
    return getOrchestratorIp() != null && getOrchestratorPort() != 0;
  }

  public int getNumberOfParallelTasks() {

    return numberOfParallelTasks;
  }

  public String getChaosApiUrl() {
    return chaosApiUrl;
  }

  public String getChaosApiKey() {
    return chaosApiKey;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setOrchestratorIp(String orchestratorIp) {
    this.orchestratorIp = orchestratorIp;
  }

  public void setOrchestratorPort(int orchestratorPort) {
    this.orchestratorPort = orchestratorPort;
  }

  public void setNumberOfParallelTasks(int numberOfParallelTasks) {
    this.numberOfParallelTasks = numberOfParallelTasks;
  }

  public void setChaosApiUrl(String chaosApiUrl) {
    this.chaosApiUrl = chaosApiUrl;
  }

  public void setChaosApiKey(String chaosApiKey) {
    this.chaosApiKey = chaosApiKey;
  }
}
