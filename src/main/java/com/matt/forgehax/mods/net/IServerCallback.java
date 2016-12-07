/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.mods.net;

public interface IServerCallback {
    public static final int IGNORE = -1;
    public static final int DISCONNECT = 0;
    public static final int CONNECTED = 1;

    public void onConnecting();

    public void onClientConnected();
}

