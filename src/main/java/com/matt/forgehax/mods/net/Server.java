/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.mods.net;

import com.matt.forgehax.mods.net.IServerCallback;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private final int port;
    private final IServerCallback callback;

    public Server(int port, IServerCallback callback) {
        this.port = port;
        this.callback = callback;
    }

    public void startServerThreaded() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                Server.this.startServer();
            }
        }).start();
    }

    public void startServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        do {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(reader);
                switch (scanner.nextInt()) {
                    case -1: {
                        break;
                    }
                    case 0: {
                        this.callback.onConnecting();
                        break;
                    }
                    case 1: {
                        this.callback.onClientConnected();
                        break;
                    }
                }
                socket.close();
                scanner.close();
                serverSocket.close();
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        } while (true);
    }

    public int getPort() {
        return this.port;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int findOpenPort(int startingPort, int maxPort) {
        for (int i = startingPort; i < maxPort + 1; ++i) {
            Socket socket = null;
            try {
                socket = new Socket("localhost", i);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(-1);
            }
            catch (Exception e) {
                int n = i;
                return n;
            }
            finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public static int getTalkPort(int port) {
        return port % 2 == 0 ? port + 1 : port - 1;
    }

}

