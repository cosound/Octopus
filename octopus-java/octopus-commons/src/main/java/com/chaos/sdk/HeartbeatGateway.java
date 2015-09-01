package com.chaos.sdk;

import com.chaos.sdk.v6.dto.ClusterState;

public interface HeartbeatGateway {
    void set(ClusterState heartbeat);
}
