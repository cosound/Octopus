package com.chaos.octopus.commons.core;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {
  private static Gson _json = new Gson();
  public List<T> Results = new ArrayList<>();

  public Response() {}

  public Response(T result) {
    Results.add(result);
  }

  public String toJson(){
    return _json.toJson(this, Response.class);
  }

  public static class Error{
    public String message = "Error";

    public Error(String message) {
      this.message = message;
    }
  }
}
