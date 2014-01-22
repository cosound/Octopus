package com.chaos.sdk;

import com.chaos.octopus.commons.core.Job;
import com.chaos.sdk.model.McmObject;
import com.chaos.sdk.model.Session;
import com.chaos.sdk.v6.dto.PortalResponse;

import java.io.IOException;
import java.util.List;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:23
 */
public class Chaos
{
    private ChaosGateway _Gateway;

    public Chaos(String chaosLocation)
    {
        this(new NetworkGateway(chaosLocation));
    }

    public Chaos(ChaosGateway gateway)
    {
        _Gateway = gateway;
    }

    public Session authenticate(String key) throws IOException
    {
        PortalResponse session = _Gateway.call("GET", "v6/SiteAccess/Auth","apiKey=" + key);

        return new Session(session.Body.getResults().get(0).get("Guid").toString());
    }

    public McmObject objectCreate(String sessionId, String guid, int objectTypeId, int folderId) throws IOException
    {
        PortalResponse session = _Gateway.call("GET", "v6/Object/Create", "sessionGUID="+sessionId+"&objectTypeID="+objectTypeId+"&folderID=" + folderId);

        return new McmObject(session.Body.getResults().get(0).get("Guid").toString());
    }

    public int metadataSet(String sessionId, String objectGuid, String metadataSchemaGuid, String languageCode, String revisionID, String metadataXml) throws IOException
    {
        PortalResponse response = _Gateway.call("POST", "v6/Metadata/Set", "sessionGUID=" +sessionId+ "&objectGuid=" + objectGuid + "&metadataSchemaGuid=" + metadataSchemaGuid + "&languageCode=" + languageCode + "&revisionID=" + revisionID + "&metadataXml=" + metadataXml);

        Double value = Double.parseDouble(response.Body.getResults().get(0).get("Value").toString());
        return value.intValue();
    }

    public Iterable<Job> jobGet()
    {
        return null;
    }

    public void jobSet(List<Job> jobs)
    {
    }
}
