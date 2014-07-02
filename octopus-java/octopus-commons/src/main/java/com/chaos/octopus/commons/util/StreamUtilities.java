package com.chaos.octopus.commons.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;

public class StreamUtilities
{
    private static Gson _gson = new Gson();

    public static <T> T ReadJson(InputStream stream, Class<T> type) throws IOException, InterruptedException
    {
        String value = ReadString(stream);

        return ReadJson(value, type);
    }

    public static <T> T ReadJson(String value, Class<T> type) throws JsonSyntaxException
    {
        try
        {
            return _gson.fromJson(value, type);
        }
        catch (JsonSyntaxException e)
        {
            System.err.println("Critial error: JsonSyntaxException ===");
            System.err.println(value);
            System.err.println("======================================");

            throw e;
        }
    }

	public static String ReadString(InputStream stream) throws IOException, InterruptedException
	{
        DataInputStream in = new DataInputStream(stream);
        int length = in.readInt();
        byte[] buffer = new byte[length];
        in.readFully(buffer);

        return new String(buffer);
//        InputStreamReader in = new InputStreamReader(stream);
//        BufferedReader reader = new BufferedReader(in);
//
//        for(int i = 5000; i > 0; i--)
//		{
//            if(!reader.ready()) Thread.sleep(1);
//		}
//
//        StringBuilder builder = new StringBuilder();
//        String line = null;
//
//        while(reader.ready())
//        {
//            line = reader.readLine();
//            builder.append(line);
//        }
//
//        return builder.toString();
	}
}
