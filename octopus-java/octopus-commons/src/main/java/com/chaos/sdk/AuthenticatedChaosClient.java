package com.chaos.sdk;

import com.chaos.octopus.commons.core.Job;
import com.chaos.sdk.ChaosGateway;
import com.chaos.sdk.model.McmObject;
import com.chaos.sdk.v6.dto.PortalResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuthenticatedChaosClient
{
    private ChaosGateway gateway;
    private String sessionId;

    public AuthenticatedChaosClient(ChaosGateway gateway, String sessionId)
    {
        this.gateway = gateway;
        this.sessionId = sessionId;
    }

    public McmObject objectCreate(String guid, int objectTypeId, int folderId) throws IOException
    {
        PortalResponse session = gateway.call("GET", "v6/Object/Create", "sessionGUID="+sessionId+"&objectTypeID="+objectTypeId+"&folderID=" + folderId);

        return new McmObject(session.Body.getResults().get(0).get("Guid").toString());
    }

    public int metadataSet(String objectGuid, String metadataSchemaGuid, String languageCode, String revisionID, String metadataXml) throws IOException
    {
        PortalResponse response = gateway.call("POST", "v6/Metadata/Set", "sessionGUID=" +sessionId+ "&objectGuid=" + objectGuid + "&metadataSchemaGuid=" + metadataSchemaGuid + "&languageCode=" + languageCode + "&revisionID=" + revisionID + "&metadataXml=" + metadataXml);

        Double value = Double.parseDouble(response.Body.getResults().get(0).get("Value").toString());
        return value.intValue();
    }

    public Iterable<Job> jobGet() throws IOException
    {
        PortalResponse response = gateway.call("GET", "v6/Job/Get", "sessionGUID=" + sessionId + "&status=incomplete");
        ArrayList<Job> jobs = new ArrayList<>();
        Gson gson = new Gson();

        for(HashMap<String, Object> job : response.Body.getResults())
        {
            String data = job.get("Data").toString();

            jobs.add(gson.fromJson(data, Job.class));
        }

        return jobs;
    }

    public void jobSet(Iterable<Job> jobs) throws IOException
    {
        Gson gson = new Gson();

        for(Job job : jobs)
        {
            String data = gson.toJson(job);
            PortalResponse response = gateway.call("POST", "v6/Job/Set", "sessionGUID=" + sessionId + "&data=" + data);
        }
    }
}
