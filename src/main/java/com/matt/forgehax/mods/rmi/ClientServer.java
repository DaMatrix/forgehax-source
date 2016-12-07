/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.mods.rmi;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.mods.rmi.CallbackImpl;
import com.matt.forgehax.mods.rmi.ICallback;
import com.matt.forgehax.mods.rmi.RemoteClientServer;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.apache.logging.log4j.Logger;

public class ClientServer {
    private final Registry registry;
    private String address;
    private String talkAddress;
    private RemoteClientServer remoteClientServer;
    private ICallback callback;

    public ClientServer(Registry registry, String address) {
        this.registry = registry;
        this.address = address;
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public String getTalkAddress() {
        return this.talkAddress;
    }

    public String getAddress() {
        return this.address;
    }

    public RemoteClientServer getRemoteClient() {
        return this.remoteClientServer;
    }

    public ICallback getCallback() {
        return this.callback;
    }

    public void startServer() {
        for (int index = 0; index < 2; ++index) {
            try {
                this.registry.lookup(this.address + String.valueOf(index));
                continue;
            }
            catch (NotBoundException e) {
                try {
                    this.callback = new CallbackImpl();
                    UnicastRemoteObject.exportObject((Remote)this.callback, 4044);
                    this.registry.rebind(this.address + String.valueOf(index), this.callback);
                    this.address = this.address + String.valueOf(index);
                    this.talkAddress = this.address + (index == 0 ? "1" : "0");
                    ForgeHax.instance().getLog().info(String.format("Created new registry '%s'", this.address));
                    this.remoteClientServer = new RemoteClientServer(this.registry, this.address);
                    return;
                }
                catch (RemoteException e1) {
                    e1.printStackTrace();
                    continue;
                }
            }
            catch (AccessException e) {
                continue;
            }
            catch (RemoteException e) {
                // empty catch block
            }
        }
    }
}

