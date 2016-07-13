package com.chaos.octopus.commons.http;

import com.chaos.octopus.commons.core.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesper on 28-06-2016.
 */
public class RequestParser {
  public static Request parse(String requestString) {
    int startOfEndpoint = requestString.indexOf(" ") + 1;
    int endOfEndpoint = requestString.indexOf("?") != -1 ?
        requestString.indexOf("?"):
        requestString.lastIndexOf("HTTP/");
    String endpoint = requestString.substring(startOfEndpoint, endOfEndpoint)
        .replaceAll("/$|^/", "");

    Request request = new Request(endpoint);
    request.queryString = parseQueryString(requestString);


    return request;
  }

  private static Map<String, String> parseQueryString(String requestString) {
    Map<String, String> parameters = new HashMap<>();
    int startOfQueryString = requestString.indexOf("?");

    if(startOfQueryString == -1) return parameters;

    String query = requestString.substring(startOfQueryString +1, requestString.indexOf("HTTP"));
    try {
      query = URLDecoder.decode(query.trim(), "UTF-8");
    } catch (UnsupportedEncodingException e) { }

    if("".equals(query)) return parameters;

    for (String pair : query.split("&")) {
      String key = pair.split("=")[0];
      String value = pair.split("=")[1];

      parameters.put(key, value);
    }

    return parameters;
  }
}
