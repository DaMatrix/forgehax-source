/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ServerData
 */
package com.matt.forgehax.util.mod;

import net.minecraft.client.multiplayer.ServerData;

public class ServerQueueManager {
    private final ServerData joiningServer;
    private int initPos = -1;
    private long initJoinTime = -1;
    private int pos = -1;

    public ServerQueueManager(ServerData data) {
        this.joiningServer = data;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int pos) {
        if (this.needsInitPos() || this.hasLostPlaceInQueue(pos)) {
            this.pos = this.initPos = pos;
            this.initJoinTime = System.currentTimeMillis();
        } else {
            this.pos = pos;
        }
    }

    public boolean needsInitPos() {
        return this.pos == -1;
    }

    public boolean hasLostPlaceInQueue(int pos) {
        return this.pos > pos;
    }

    public boolean isFirst() {
        return this.pos == 1;
    }

    public long getEstimatedTime() {
        long deltaTime = System.currentTimeMillis() - this.initJoinTime;
        long deltaPos = this.initPos - this.pos;
        return deltaTime / (deltaPos + 1);
    }

    public boolean equals(Object o) {
        if (o instanceof ServerData) {
            return this.joiningServer.serverIP.equals(((ServerData)o).serverIP);
        }
        return super.equals(o);
    }
}

