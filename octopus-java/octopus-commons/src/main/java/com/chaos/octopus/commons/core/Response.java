package com.chaos.octopus.commons.core;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesper on 27-06-2016.
 */
public class Response {
  private static Gson _json = new Gson();
  public List<Result> Results = new ArrayList<>();

  public String toJson(){
    return _json.toJson(this, Response.class);
  }

  public class Result {
    public List<String> Keys = new ArrayList<>();
  }
}
