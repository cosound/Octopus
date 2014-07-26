/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.sdk.model;

public class Session {
  private String Id;

  public Session(String id) {
    setId(id);
  }

  public String getId() {
    return Id;
  }

  private void setId(String id) {
    Id = id;
  }
}
