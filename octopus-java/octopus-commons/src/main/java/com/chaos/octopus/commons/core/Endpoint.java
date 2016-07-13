package com.chaos.octopus.commons.core;

/**
 * Created by Jesper on 28-06-2016.
 */
public interface Endpoint {
  Response invoke(Request request);
}
