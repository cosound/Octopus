/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.sdk;

import com.chaos.sdk.v6.dto.PortalResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkGateway implements ChaosGateway {
  private String _Hostname;
  private final Gson gson;

  public NetworkGateway(String hostname) {
    _Hostname = hostname;
    gson = new Gson();
  }

  @Override
  public PortalResponse call(String method, String path, String query) throws IOException {
    if ("POST".equals(method)) return callPost(path, query);

    URL url = new URL(_Hostname + "/" + path + "?" + query + "&format=json2");
    StringBuffer result = new StringBuffer();

    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setRequestMethod(method);

    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
      String inputLine;

      while ((inputLine = in.readLine()) != null)
        result.append(inputLine);
    }

    return gson.fromJson(result.toString(), PortalResponse.class);
  }

  public PortalResponse callPost(String path, String query) throws IOException {
    query += "&format=json2";
    URL url = new URL(_Hostname + "/" + path);

    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");

    con.setDoOutput(true);

    try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
      wr.writeBytes(query);
    }

    StringBuffer result = new StringBuffer();

    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
      String inputLine;

      while ((inputLine = in.readLine()) != null)
        result.append(inputLine);
    }
    return gson.fromJson(result.toString(), PortalResponse.class);
  }
}
