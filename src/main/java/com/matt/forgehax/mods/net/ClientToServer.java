/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.mods.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientToServer {
    private final int port;

    public ClientToServer(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void sendDisconnectMessage() {
        this.sendMessage(0);
    }

    public void sendConnectedMessage() {
        this.sendMessage(1);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sendMessage(int message) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", this.port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
        }
        catch (UnknownHostException e) {}
        catch (IOException e) {}
        finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            }
            catch (IOException e) {}
        }
    }
}

