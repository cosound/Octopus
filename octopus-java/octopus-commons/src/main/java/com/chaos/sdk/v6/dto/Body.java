/**
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.chaos.sdk.v6.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class Body {
  private int Count;
  private int TotalCount;
  private ArrayList<HashMap<String, Object>> Results;

  public Body() {
    Results = new ArrayList<>();
  }

  public ArrayList<HashMap<String, Object>> getResults() {
    return Results;
  }
}
