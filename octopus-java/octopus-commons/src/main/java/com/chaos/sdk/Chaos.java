package com.chaos.sdk;

import com.chaos.octopus.commons.core.Job;
import com.chaos.sdk.model.McmObject;
import com.chaos.sdk.v6.dto.AuthenticatedChaosClient;
import com.chaos.sdk.v6.dto.PortalResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:23
 */
public class Chaos
{
    private ChaosGateway gateway;

    public Chaos(String chaosLocation)
    {
        this(new NetworkGateway(chaosLocation));
    }

    public Chaos(ChaosGateway gateway)
    {
        this.gateway = gateway;
    }

    public AuthenticatedChaosClient authenticate(String key) throws IOException
    {
        PortalResponse session = gateway.call("GET", "v6/SiteAccess/Auth","apiKey=" + key);
        String sessionId = session.Body.getResults().get(0).get("Guid").toString();

        return createAuthenticatedClient(sessionId);
    }

    private AuthenticatedChaosClient createAuthenticatedClient(String sessionId) throws IOException
    {
        return new AuthenticatedChaosClient(gateway, sessionId);
    }

    public McmObject objectCreate(String sessionId, String guid, int objectTypeId, int folderId) throws IOException
    {
        return createAuthenticatedClient(sessionId).objectCreate(guid, objectTypeId, folderId);
    }

    public int metadataSet(String sessionId, String objectGuid, String metadataSchemaGuid, String languageCode, String revisionID, String metadataXml) throws IOException
    {
        return createAuthenticatedClient(sessionId).metadataSet(objectGuid, metadataSchemaGuid, languageCode, revisionID, metadataXml);
    }
}
