package com.chaos.octopus.commons.core;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.nio.file.Paths;

/**
 * Created by JesperFyhr on 05-02-14.
 */
public class OctopusConfiguration
{
    private int port;

    private String orchestratorIp = null;
    private int orchestratorPort  = 0;
    private int numberOfParallelTasks = 8;
    private String chaosApiUrl = null;
    private String chaosApiKey = null;

    public OctopusConfiguration() throws ConfigurationException
    {
        String wd = System.getProperty("user.dir");
        String path = Paths.get(wd, "octopus.properties").toString();
        Configuration config = new PropertiesConfiguration(path);
        port = config.getInt("listening.port");

        if(config.containsKey("agent.numberOfParallelTasks"))
            numberOfParallelTasks = config.getInt("agent.numberOfParallelTasks");

        if(config.containsKey("orchestrator.ip"))
            orchestratorIp = config.getString("orchestrator.ip");

        if(config.containsKey("orchestrator.port"))
            orchestratorPort = config.getInt("orchestrator.port");

        if(config.containsKey("orchestrator.chaosApiUrl"))
            chaosApiUrl = config.getString("orchestrator.chaosApiUrl");

        if(config.containsKey("orchestrator.chaosApiKey"))
            chaosApiKey = config.getString("orchestrator.chaosApiKey");
    }

    public int getListeningPort()
    {
        return port;
    }

    public String getOrchestratorIp()
    {
        return orchestratorIp;
    }

    public int getOrchestratorPort()
    {
        return orchestratorPort;
    }

    public Boolean getIsAgent()
    {
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
}
