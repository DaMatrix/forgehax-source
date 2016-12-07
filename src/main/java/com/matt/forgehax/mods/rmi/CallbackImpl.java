/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.mods.rmi;

import com.matt.forgehax.mods.rmi.ICallback;
import com.matt.forgehax.mods.rmi.IRemoteCallback;
import java.io.PrintStream;
import java.rmi.RemoteException;

public class CallbackImpl
implements ICallback {
    private Object callback;

    protected CallbackImpl() throws RemoteException {
    }

    @Override
    public void setCallback(Object cb) throws RemoteException {
        this.callback = cb;
    }

    @Override
    public void onConnecting() throws RemoteException {
        if (this.callback instanceof IRemoteCallback) {
            ((IRemoteCallback)this.callback).onCalled();
        }
        System.out.printf("\n\nCalled onConnecting\n\n", new Object[0]);
    }
}

