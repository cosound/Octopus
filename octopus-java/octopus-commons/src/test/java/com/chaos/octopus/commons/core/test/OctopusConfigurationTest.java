package com.chaos.octopus.commons.core.test;

import com.chaos.octopus.commons.core.OctopusConfiguration;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class OctopusConfigurationTest
{
    @Test
    public void constructor_GivenFileWithAllProperties_PropertiesAreSet() throws Exception
    {
        OctopusConfiguration result = new OctopusConfiguration();

        assertEquals(26000, result.getPort());
        assertEquals("127.0.0.1", result.getOrchestratorIp());
        assertEquals(25000, result.getOrchestratorPort());
    }
}
