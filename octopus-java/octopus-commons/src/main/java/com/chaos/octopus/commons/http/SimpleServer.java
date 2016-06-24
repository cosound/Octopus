package com.chaos.octopus.commons.http;

import java.io.IOException;
import java.net.*;

/**
 * Created by Jesper on 23-06-2016.
 */
public class SimpleServer implements Runnable{

  public boolean _isRunning = true;
  private ServerSocket _serverSocket;

  public SimpleServer(){
    try {
      _serverSocket = new ServerSocket(8080);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    while (_isRunning) {
        try(Socket socket = _serverSocket.accept()){
          new HttpRequestHander(socket).invoke();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  private class HttpRequestHander {
    private Socket socket;

    public HttpRequestHander(Socket socket) {
      this.socket = socket;
    }

    public void invoke() throws IOException {
      while(socket.getInputStream().available() == 0);

      String requestString = "";

      while(socket.getInputStream().available() != 0){
        byte[] buffer = new byte[socket.getInputStream().available()];
        socket.getInputStream().read(buffer);

        requestString += new String(buffer);
      }

      //System.out.println(requestString);
      SendResponse("{\"Hello\":\"World\"}");
    }

    private void SendResponse(String content) throws IOException {
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
