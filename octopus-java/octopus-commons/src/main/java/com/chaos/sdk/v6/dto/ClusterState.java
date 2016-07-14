package com.chaos.sdk.v6.dto;

import java.util.ArrayList;
import java.util.List;

public class ClusterState {
  public String hostname;
  public int port;

  //public int cpuUsage;
  public int ramUsage;
  public int parallelism;
  public boolean hasAvailableSlots;
  public int queueSize;
  public int runningSize;
  public List<AgentState> agents = new ArrayList<>();

  public static class AgentState{
    public String state = "Connected";
    public String hostname;
    public int port;

    //public int cpuUsage;
    public int ramUsage;
    public int parallelism;
    public boolean hasAvailableSlots;
    public int queueSize;
    public int runningSize;
  }
}
