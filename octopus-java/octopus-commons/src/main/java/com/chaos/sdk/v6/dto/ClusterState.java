package com.chaos.sdk.v6.dto;

import java.util.ArrayList;

public class ClusterState {
  public int jobsInQueue = 0;
  public ArrayList<AgentState> agents = new ArrayList<>();

  public static class AgentState{
    public String state = "";
    public boolean hasAvailableSlots;
    public String hostname;
    public int port;
  }
}