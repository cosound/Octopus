package com.chaos.octopus.commons.core.http.test;

import com.chaos.octopus.commons.core.Request;
import com.chaos.octopus.commons.http.RequestParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jesper on 28-06-2016.
 */
public class RequestParserTest {
  private String request =  "GET /end/point/?key1=val 1&key2=val 2 HTTP/1.1\n"+
                            "Host: localhost:8080\n"+
                            "Accept-Encoding: gzip, deflate\n"+
                            "Connection: keep-alive\n";

  @Test
  public void parse_GivenValidRequest_Return() {
    Request rq = RequestParser.parse(request);

    Assert.assertEquals(rq.endpoint, "end/point");
    Assert.assertEquals(rq.queryString.get("key1"), "val 1");
    Assert.assertEquals(rq.queryString.get("key2"), "val 2");
  }
}
