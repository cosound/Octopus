package com.chaos.sdk;

import com.chaos.sdk.v6.dto.PortalResponse;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:27
 */
public interface ChaosGateway
{
    PortalResponse call(String method, String path, String query) throws IOException;
}
