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

    public OctopusConfiguration() throws ConfigurationException
    {
        System.out.println("OctopusConfiguration");
        String wd = System.getProperty("user.dir");
        System.out.println(wd);
        String path = Paths.get(wd, "octopus.properties").toString();
        System.out.println(path);
        Configuration config = new PropertiesConfiguration(path);
        port = config.getInt("listening.port");

        if(config.containsKey("orchestrator.ip"))
            orchestratorIp = config.getString("orchestrator.ip");

        if(config.containsKey("orchestrator.port"))
            orchestratorPort = config.getInt("orchestrator.port");
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
}
