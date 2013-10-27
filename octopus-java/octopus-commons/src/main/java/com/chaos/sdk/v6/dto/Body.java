package com.chaos.sdk.v6.dto;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Jesper Fyhr Knudsen
 * Date: 27-10-13
 * Time: 20:46
 */
public class Body
{
    private int Count;
    private int TotalCount;
    private ArrayList<HashMap<String, String>> Results;

    public Body()
    {
        Results = new ArrayList<HashMap<String, String>>();
    }

    public ArrayList<HashMap<String, String>> getResults()
    {
        return Results;
    }
}
