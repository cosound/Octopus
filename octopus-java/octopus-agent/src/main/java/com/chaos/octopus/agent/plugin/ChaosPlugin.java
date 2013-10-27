package com.chaos.octopus.agent.plugin;

import com.chaos.octopus.commons.core.Plugin;
import com.chaos.octopus.commons.core.Task;
import com.chaos.sdk.Chaos;
import com.chaos.sdk.model.Session;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 23-10-13
 * Time: 17:06
 */
public class ChaosPlugin implements Plugin
{
    private Task _Task;

    @Override
    public String getId()
    {
        return "com.chaos.octopus.plugins.ChaosPlugin, 1.0.0";
    }

    public ChaosPlugin(Task task)
    {
        setTask(task);
    }

    @Override
    public Task getTask()
    {
        return _Task;
    }

    private void setTask(Task task)
    {
        _Task = task;
    }

    @Override
    public void execute() throws Exception
    {
        // TODO once more actions are implemented, replace with Strategy pattern
        if(getAction() == "object.create")
        {
            Chaos api = new Chaos(getChaosLocation());

            Session session = api.authenticate("b22058bb0c7b2fe4bd3cbffe99fe456b396cbe2083be6c0fdcc50b706d8b4270");

            System.out.println(session.getId());
            //String sessionGuid = CreateSession();
            //String guid = CreateObject(sessionGuid);
            //SetMetadata(sessionGuid, guid);
        }
    }

    private void SetMetadata()
    {
        // TODO Add Metadata to Object
    }

    private void CreateObject()
    {
        // TODO Create Object
    }

    private void CreateSession()
    {
        // TODO Get a session
        // TODO Authenticate session
    }

    @Override
    public void rollback()
    {
    }

    @Override
    public void commit()
    {
    }

    public String getAction()
    {
        return getTask().properties.get("action");
    }

    public String getInputXml()
    {
        return getTask().properties.get("input-xmlfilepath");
    }

    public String getChaosLocation()
    {
        return getTask().properties.get("chaos-location");
    }
}
