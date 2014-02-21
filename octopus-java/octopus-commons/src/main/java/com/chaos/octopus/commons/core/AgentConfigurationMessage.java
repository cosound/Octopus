package com.chaos.octopus.commons.core;

import com.chaos.octopus.commons.util.StreamUtilities;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
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

    public static AgentConfigurationMessage create(InputStream stream) throws IOException, InterruptedException
    {
        String responseString = StreamUtilities.ReadString(stream);

        return new Gson().fromJson(responseString, AgentConfigurationMessage.class);
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

    public String toJson()
    {
        return new Gson().toJson(this);
    }
}
