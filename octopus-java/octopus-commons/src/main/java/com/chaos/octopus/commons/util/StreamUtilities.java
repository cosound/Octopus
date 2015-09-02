/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtilities {
  private static Gson _gson = new Gson();

  public static <T> T ReadJson(InputStream stream, Class<T> type) throws IOException, InterruptedException {
    String value = ReadString(stream);

    return ReadJson(value, type);
  }

  public static <T> T ReadJson(String value, Class<T> type) throws JsonSyntaxException {
    try {
      return _gson.fromJson(value, type);
    } catch (JsonSyntaxException e) {
      System.err.println("Critial error: JsonSyntaxException ===");
      System.err.println(value);
      System.err.println("======================================");

      throw e;
    }
  }

  public static String ReadString(InputStream stream) throws IOException, InterruptedException {
    DataInputStream in = new DataInputStream(stream);
    int length = in.readInt();
    byte[] buffer = new byte[length];
    in.readFully(buffer);

    return new String(buffer);
  }
}
