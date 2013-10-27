package com.chaos.sdk.model;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:21
 */
public class Session
{
    private String Id;

    public Session(String id)
    {
        setId(id);
    }

    public String getId()
    {
        return Id;
    }

    private void setId(String id)
    {
        Id = id;
    }
}
