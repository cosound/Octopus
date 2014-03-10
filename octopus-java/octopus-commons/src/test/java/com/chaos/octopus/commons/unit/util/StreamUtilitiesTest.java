package com.chaos.octopus.commons.unit.util;

import static org.junit.Assert.*;

import java.io.*;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.junit.Test;

import com.chaos.octopus.commons.util.StreamUtilities;

public class StreamUtilitiesTest
{

	@Test
	public void test() throws IOException, InterruptedException
	{
		String expected = "some string";
		InputStream stream = new ByteArrayInputStream(expected.getBytes());
		
		String result = StreamUtilities.ReadString(stream);
		
		assertEquals(expected, result);
	}
}
