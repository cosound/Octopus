/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.octopus.commons.core;

import com.chaos.octopus.commons.util.StreamUtilities;
import com.google.gson.Gson;

public class Message {
  private String action;

  public Message() {
  }

  public static Message createFromJson(String json) {
    return StreamUtilities.ReadJson(json, Message.class);
  }

  public static Message createWithAction(String action) {
    Message msg = new Message();
    msg.setAction(action);

    return msg;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String toJson() {
    Gson gson = new Gson();

    return gson.toJson(this);
  }

}

