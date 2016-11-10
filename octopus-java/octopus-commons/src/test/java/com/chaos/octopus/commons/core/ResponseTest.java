package com.chaos.octopus.commons.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;

public class ResponseTest {
  @Test
  public void nothing(){
    Response<MyResult> res1 = new Response<MyResult>();
    res1.Results.add(new MyResult());

    String json = new Gson().toJson(res1);

    Type type = new TypeToken<Response<MyResult>>() {
    }.getType();

    Response<MyResult> res11 = new Gson().fromJson(json, type);

    for (MyResult result : res11.Results) {
      System.out.println(result.value);
    }


  }

  private class MyResult {
    public String value = "MyResult";
  }

  private class MyOtherResult {
    public String value = "MyOtherResult";
  }
}
