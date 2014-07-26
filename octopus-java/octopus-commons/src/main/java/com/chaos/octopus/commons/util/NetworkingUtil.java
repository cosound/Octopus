/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class NetworkingUtil {
  private final String _hostname;
  private final int _port;

  public NetworkingUtil(String hostname, int port) {
    _hostname = hostname;
    _port = port;
  }

  public String sendWithReply(String message) {
    return send(message, true, 10);
  }

  public void send(String message) {
    send(message, false, 10);
  }

  private String send(String message, boolean handleResponse, int retries) {
    try {
      try (Socket socket = new Socket(_hostname, _port)) {
        OutputStream outputStream = socket.getOutputStream();
        send(message, outputStream);

        if (!handleResponse) return null;

        return StreamUtilities.ReadString(socket.getInputStream());

//                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//                out.write(message);
//                out.newLine();
//
//
//
//                StringBuffer result = new StringBuffer();
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                String inputLine;
//
//                while ((inputLine = in.readLine()) != null)
//                    result.append(inputLine);
//
//                return result.toString();

//                OutputStream out = socket.getOutputStream();
//                PrintStream ps = new PrintStream(out);
//                ps.println(message);
//                ps.flush();
//
//                if(handleResponse)
//                {
//                    InputStream in = socket.getInputStream();
//                    return StreamUtilities.ReadString(in);
//                }
      }
    } catch (ConnectException e) {
      throw new com.chaos.octopus.commons.exception.ConnectException("Connection to Orchestrator could not be established, check hostname and port", e);
    } catch (Exception e) {
      if (retries > 0) {
        sleep(500);
        send(message, handleResponse, --retries);
      }

      // TODO This exception should be handled at a higher level
      System.err.println("Couldn't connect to: " + _hostname + ":" + _port);
      e.printStackTrace();
    }

    return null;
  }

  public static void send(String message, OutputStream outputStream) throws IOException {
    DataOutputStream out = new DataOutputStream(outputStream);
    byte[] buffer = message.getBytes();
    out.writeInt(buffer.length);
    out.write(buffer);
    out.flush();
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e1) {

    }
  }
}
