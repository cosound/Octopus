package com.chaos.octopus.commons.core.http.test;

import com.chaos.octopus.commons.http.SimpleServer;
import org.junit.Test;

public class SimpleServerTest {
  @Test
  public void nothing(){
    SimpleServer ss = new SimpleServer();
    ss.run();
  }
}
