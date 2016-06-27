package com.chaos.octopus.commons.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesper on 27-06-2016.
 */
public class Request {
  public String method;
  public Map<String, String> queryString = new HashMap<>();
}
