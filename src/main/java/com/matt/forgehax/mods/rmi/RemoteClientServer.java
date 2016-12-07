/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.mods.rmi;

import com.matt.forgehax.mods.rmi.ICallback;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import javax.rmi.PortableRemoteObject;

public class RemoteClientServer {
    private final Registry registry;
    private final String address;
    private ICallback callback;

    public RemoteClientServer(Registry registry, String address) {
        this.registry = registry;
        this.address = address;
    }

    private ICallback attemptLookup() {
        if (this.callback == null) {
            Remote o = null;
            try {
                o = this.registry.lookup(this.address);
                this.callback = (ICallback)PortableRemoteObject.narrow(o, ICallback.class);
            }
            catch (RemoteException e) {
            }
            catch (NotBoundException e) {
                // empty catch block
            }
        }
        return this.callback;
    }

    public String getAddress() {
        return this.address;
    }

    public Registry getRegistry() {
        return this.registry;
    }

    public ICallback getCallback() {
        return this.attemptLookup();
    }

    public void onConnect() {
        if (this.attemptLookup() != null) {
            try {
                this.callback.onConnecting();
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

