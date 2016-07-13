package com.chaos.sdk.v6.dto;

import java.util.ArrayList;
import java.util.List;

public class ClusterState {
  public int jobsInQueue = 0;
  public List<AgentState> agents = new ArrayList<>();

  public static class AgentState{
    public String state = "";
    public boolean hasAvailableSlots;
    public String hostname;
    public int port;
    public int cpuUsage;
    public int ramUsage;
    public int queueSize;
    public int runningSize;
    public int parallelism;
  }
}
