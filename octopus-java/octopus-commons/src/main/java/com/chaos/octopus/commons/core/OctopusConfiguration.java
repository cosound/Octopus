package com.chaos.octopus.commons.core;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created by JesperFyhr on 05-02-14.
 */
public class OctopusConfiguration
{
    private int port;

    private String orchestratorIp;
    private int orchestratorPort;

    public OctopusConfiguration()
    {
        try
        {
            String wd = System.getProperty("user.dir");
            Configuration config = new PropertiesConfiguration(wd + "\\octopus.properties");
            port = config.getInt("listening.port");
            orchestratorIp = config.getString("orchestrator.ip");
            orchestratorPort = config.getInt("orchestrator.port");

        }
        catch (ConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public int getPort()
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
}
