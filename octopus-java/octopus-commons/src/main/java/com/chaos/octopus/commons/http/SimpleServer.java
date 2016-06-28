package com.chaos.octopus.commons.http;

import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.core.Response;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer implements Runnable{

  private boolean _isRunning = true;
  private ServerSocket _serverSocket;
  private ExecutorService pool = Executors.newFixedThreadPool(16);

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
    } catch (IOException ignored) { }
  }

  @Override
  public void run() {
    while (_isRunning) {
        try{
          final Socket socket = _serverSocket.accept();
          pool.execute(new RequestHandler(socket));
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  private class RequestHandler implements Runnable {
    private Socket socket;

    public RequestHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        String requestString = readRequestFromStream();

        Request request = RequestParser.parse(requestString);

        Response res = route(request);

        SendResponse(res);
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    Response route(Request request) {
      Response res = new Response();
      Response.Result result = res.new Result();
      res.Results.add(result);

      for (String val : request.queryString.values()) {
        result.Keys.add(val);
      }

      return res;
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
          "Content-Length: " + contentBytes.length +"\n\n";

      socket.getOutputStream().write(responseString.getBytes());
      socket.getOutputStream().write(contentBytes);
    }
  }
}
