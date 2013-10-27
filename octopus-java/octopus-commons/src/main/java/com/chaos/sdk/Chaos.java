package com.chaos.sdk;

import com.chaos.sdk.model.Session;
import com.chaos.sdk.v6.dto.PortalResponse;

import java.io.IOException;

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
        PortalResponse session = _Gateway.call("v6/SiteAccess/Auth?apiKey=" + key);

        return new Session(session.Body.getResults().get(0).get("Guid"));
    }
}
