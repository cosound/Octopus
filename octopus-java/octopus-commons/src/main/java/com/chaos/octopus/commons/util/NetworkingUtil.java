package com.chaos.octopus.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by Jesper Fyhr Knudsen on 10-03-14.
 */
public class NetworkingUtil
{
    private final String _hostname;
    private final int _port;

    public NetworkingUtil(String hostname, int port)
    {
        _hostname = hostname;
        _port = port;
    }

    public String sendWithReply(String message)
    {
        return send(message, true, 10);
    }

    public void send(String message)
    {
        send(message, false, 10);
    }

    private String send(String message, boolean handleResponse, int retries)
    {
        try
        {
            try(Socket socket = new Socket(_hostname, _port))
            {
                send(message, socket);

                if(handleResponse) return receive(socket);
            }

        }
        catch (ConnectException e)
        {
            throw new com.chaos.octopus.commons.exception.ConnectException("Connection to Orchestrator could not be established, check hostname and port", e);
        }
        catch (Exception e)
        {
            if(retries > 0)
            {
                sleep(500);
                send(message, handleResponse, --retries);
            }

            // TODO This exception should be handled at a higher level
            System.err.println("Couldn't connect to: " + _hostname + ":" + _port);
            e.printStackTrace();
        }

        return null;
    }

    private void send(String message, Socket socket) throws IOException
    {
        OutputStream out = socket.getOutputStream();
        PrintStream ps = new PrintStream(out);
        ps.println(message);
    }

    private String receive(Socket socket) throws IOException, InterruptedException
    {
        InputStream in = socket.getInputStream();
        return StreamUtilities.ReadString(in);
    }

    private void sleep(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e1)
        {

        }
    }
}
