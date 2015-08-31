package com.chaos.octopus.server.synchronization;

import com.chaos.sdk.HeartbeatGateway;

public class Heartbeat implements SynchronizationTask {
    private final HeartbeatGateway gateway;

    public Heartbeat(HeartbeatGateway gateway) {
        this.gateway = gateway;
    }

    public void action() {
        gateway.set();
    }
}
