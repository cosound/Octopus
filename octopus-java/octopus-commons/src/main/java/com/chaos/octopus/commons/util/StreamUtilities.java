package com.chaos.octopus.commons.util;

import java.io.*;

public class StreamUtilities
{

	public static String ReadString(InputStream stream) throws IOException, InterruptedException
	{
        InputStreamReader in = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(in);

        for(int i = 5000; i > 0; i--)
		{
            if(!reader.ready()) Thread.sleep(1);
		}

        StringBuilder builder = new StringBuilder();
        String line = null;

        while(reader.ready())
        {
            line = reader.readLine();
            builder.append(line);
        }

        return builder.toString();
	}

}
