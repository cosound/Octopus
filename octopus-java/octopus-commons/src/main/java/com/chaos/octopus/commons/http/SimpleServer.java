package com.chaos.octopus.commons.http;

import com.chaos.octopus.commons.core.Endpoint;
import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer implements Runnable{
  private Thread _thread;
  private boolean _isRunning = true;
  private ServerSocket _serverSocket;
  private ExecutorService _pool = Executors.newFixedThreadPool(64);
  private Map<String, Endpoint> _endpoints = new HashMap<>();

  public SimpleServer(int listeningPort){
    try {
      _serverSocket = new ServerSocket(listeningPort);
      _thread = new Thread(this);
      _thread.setName("CHAOS Webserver");
      _thread.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stop(){
    _isRunning = false;
    try {
      _serverSocket.close();
      _pool.shutdown();
    } catch (IOException ignored) { }
  }

  public void run() {
    while (_isRunning) {
        try{
          final Socket socket = _serverSocket.accept();
          _pool.execute(new RequestHandler(socket));
        } catch (IOException e) {
        }
    }
  }

  public void addEndpoint(String route, Endpoint endpoint) {
    _endpoints.put(route, endpoint);
  }

  public boolean getIsRunning() {
    return _isRunning;
  }

  private class RequestHandler implements Runnable {
    private Socket socket;

    public RequestHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        Request request = RequestParser.parse(readRequestFromStream());
        Response res = route(request);

        SendResponse(res);
        socket.close();
      } catch (SocketException se) {
        // if the socket is closed it means the server is turned off, so we can ignore the exception
        if (!socket.isClosed()) se.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    Response route(Request request) {
      if(_endpoints.containsKey(request.endpoint))
        return _endpoints.get(request.endpoint).invoke(request);

      return new Response<>(new Response.Error("Endpoint not found: " + request.endpoint));
    }

    String readRequestFromStream() throws IOException {
      while(socket.getInputStream().available() == 0);

      String requestString = "";

      while(socket.getInputStream().available() != 0){
        byte[] buffer = new byte[socket.getInputStream().available()];
        socket.getInputStream().read(buffer);

        requestString += new String(buffer);
      }

      return requestString;
    }

    void SendResponse(Response response) throws IOException {
      String content = response.toJson();
      byte[] contentBytes = content.getBytes();

      String responseString = "HTTP/1.x 200 OK\n" +
          "Connection: close\n" +
          "Content-Type: application/json\n" +
          "Content-Length: " + contentBytes.length +"\n\n" + content;

      socket.getOutputStream().write(responseString.getBytes());
    }
  }
}
