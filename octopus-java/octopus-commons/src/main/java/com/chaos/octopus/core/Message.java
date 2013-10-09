package com.chaos.octopus.core;

import com.google.gson.Gson;

public class Message
{
	private String action;

    public Message() { }
    public Message(String action)
    {
        setAction(action);
    }

    public String getAction()
	{
		return action;
	}

    public void setAction(String action)
    {
        this.action = action;
    }

    public String toJson()
    {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
