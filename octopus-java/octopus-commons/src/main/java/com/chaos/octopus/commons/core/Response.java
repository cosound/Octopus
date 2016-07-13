package com.chaos.octopus.commons.core;

import com.chaos.sdk.v6.dto.ClusterState;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {
  private static Gson _json = new Gson();
  public List<T> Results = new ArrayList<>();

  public String toJson(){
    return _json.toJson(this, Response.class);
  }

  public class Result {
    public List<String> Keys = new ArrayList<>();
  }
}
