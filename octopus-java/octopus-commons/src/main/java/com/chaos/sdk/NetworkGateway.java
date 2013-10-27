package com.chaos.sdk;

import com.chaos.sdk.v6.dto.PortalResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 22:10
 */
public class NetworkGateway implements ChaosGateway
{
    private String _Hostname;

    public NetworkGateway(String hostname)
    {
        _Hostname = hostname;
    }

    @Override
    public PortalResponse call(String address) throws IOException
    {
        Gson gson = new Gson();
        URL  url  = new URL(_Hostname + "/" + address + "&format=json2");
        StringBuffer result = new StringBuffer();

        try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream())))
        {
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                result.append(inputLine);
        }

        return gson.fromJson(result.toString(), PortalResponse.class);
    }
}
