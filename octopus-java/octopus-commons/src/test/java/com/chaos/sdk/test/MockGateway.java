package com.chaos.sdk.test;

import com.chaos.sdk.ChaosGateway;
import com.chaos.sdk.v6.dto.PortalResponse;
import com.google.gson.Gson;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:28
 */
public class MockGateway implements ChaosGateway
{
    private String url;
    private String result;
    private Gson   gson;

    public MockGateway(String url, String result)
    {
        this.url    = url;
        this.result = result;
        this.gson   = new Gson();
    }

    @Override
    public PortalResponse call(String method, String path, String query)
    {
        if((path + "?" + query).startsWith(url))
            return gson.fromJson(result, PortalResponse.class);

        return null;
    }
}
