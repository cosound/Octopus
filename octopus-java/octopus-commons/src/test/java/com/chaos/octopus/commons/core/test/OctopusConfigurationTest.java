package com.chaos.octopus.commons.core.test;

import com.chaos.octopus.commons.core.OctopusConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class OctopusConfigurationTest
{
    @Test
    public void constructor_GivenFileWithAllProperties_PropertiesAreSet() throws Exception
    {
        OctopusConfiguration result = new OctopusConfiguration();

        assertEquals(26000, result.getListeningPort());
        assertEquals("127.0.0.1", result.getOrchestratorIp());
        assertEquals(25000, result.getOrchestratorPort());
        assertEquals(4, result.getNumberOfParallelTasks());
        assertEquals("api url", result.getChaosApiUrl());
        assertEquals("api key", result.getChaosApiKey());
    }

    @Test
    public void getIsAgent_IpAndPortSet_ReturnTrue() throws ConfigurationException
    {
        OctopusConfiguration config = new OctopusConfiguration();

        Boolean result = config.getIsAgent();

        assertTrue(result);
    }
}
