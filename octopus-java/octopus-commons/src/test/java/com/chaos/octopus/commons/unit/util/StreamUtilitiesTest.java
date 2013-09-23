package com.chaos.octopus.commons.unit.util;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
