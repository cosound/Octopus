package com.chaos.octopus.commons.http;

import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesper on 23-06-2016.
 */
public class SimpleServer implements Runnable{

  private boolean _isRunning = true;
  private ServerSocket _serverSocket;

  public SimpleServer(){
    try {
      _serverSocket = new ServerSocket(8080);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stop(){
    _isRunning = false;
    try {
      _serverSocket.close();
    } catch (IOException e) { }
  }

  @Override
  public void run() {
    while (_isRunning) {
        try(Socket socket = _serverSocket.accept()){
          new HttpRequestHandler(socket).invoke();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  private class HttpRequestHandler {
    private Socket socket;

    public HttpRequestHandler(Socket socket) {
      this.socket = socket;
    }

    public void invoke() throws IOException {
      while(socket.getInputStream().available() == 0);

      Request requestString = parseRequest();

      Response res = new Response();
      Response.Result result = res.new Result();
      res.Results.add(result);

      for (String val : requestString.queryString.values()) {
        result.Keys.add(val);
      }

      SendResponse(res);
    }

    private Request parseRequest() throws IOException {
      String requestString = "";

      while(socket.getInputStream().available() != 0){
        byte[] buffer = new byte[socket.getInputStream().available()];
        socket.getInputStream().read(buffer);

        requestString += new String(buffer);
      }

      Request request = new Request();
      request.method = "GET";
      request.queryString = parseQueryString(requestString);

      return request;
    }

    private Map<String, String> parseQueryString(String requestString) throws UnsupportedEncodingException {
      String query = requestString.substring(6, requestString.indexOf("HTTP"));
      query = URLDecoder.decode(query.trim(), "UTF-8");

      Map<String, String> parameters = new HashMap<>();

      if("".equals(query)) return parameters;

      for (String pair : query.split("&")) {
        String key = pair.split("=")[0];
        String value = pair.split("=")[1];

        parameters.put(key, value);
      }

      return parameters;
    }

    private void SendResponse(Response response) throws IOException {
      String content = response.toJson();
      byte[] contentBytes = content.getBytes();

      String responseString = "HTTP/1.x 200 OK\n" +
          "Connection: close\n" +
          "Content-Type: application/json\n" +
          "Content-Length: " + contentBytes.length +"\n\n";

      socket.getOutputStream().write(responseString.getBytes());
      socket.getOutputStream().write(contentBytes);
    }
  }
}
