package com.chaos.octopus.commons.core;

import java.util.ArrayList;

/**
 * Copyright (c) 2014 Chaos ApS. All rights reserved. See LICENSE.TXT for details.
 */
public class AgentConfigurationMessage extends Message
{
    private int numberOfSimulataniousTasks = 0;
    private ArrayList<String> supportedPlugins = new ArrayList<>();

    public AgentConfigurationMessage()
    {

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
}
