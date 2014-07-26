/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.sdk.model;

public class McmObject {
  private String _Id;

  public McmObject(String id) {
    _Id = id;
  }

  public String getId() {
    return _Id;
  }
}
