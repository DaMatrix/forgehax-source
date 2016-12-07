/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.mods.rmi;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICallback
extends Remote,
Serializable {
    public void setCallback(Object var1) throws RemoteException;

    public void onConnecting() throws RemoteException;
}

