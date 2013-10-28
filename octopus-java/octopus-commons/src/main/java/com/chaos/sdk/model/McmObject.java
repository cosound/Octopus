package com.chaos.sdk.model;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 28-10-13
 * Time: 13:38
 */
public class McmObject
{
    private String _Id;

    public McmObject(String id)
    {
        _Id = id;
    }

    public String getId()
    {
        return _Id;
    }
}
