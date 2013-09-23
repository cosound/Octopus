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
				byte[] buffer = new byte[4096];
				
				int read = stream.read(buffer);
			
				return new String(buffer, 0, read);
			}
			Thread.sleep(1);
		}
		
		throw new IOException("no data received");
	}

}
