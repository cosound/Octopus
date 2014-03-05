package com.chaos.octopus.commons.util;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtilities
{

	public static String ReadString(InputStream stream) throws IOException, InterruptedException
	{
		for(int i = 1000; i > 0; i--)
		{
			int available = stream.available();

            if(available > 0)
            {
                StringBuilder sb = new StringBuilder();

                while(available > 0)
                {
                    byte[] buffer = new byte[available];

                    int read = stream.read(buffer);

                    sb.append(new String(buffer, 0, read));
                    available = stream.available();
                }

                return sb.toString();
            }

			Thread.sleep(1);
		}
		
		throw new IOException("no data received");
	}

}
