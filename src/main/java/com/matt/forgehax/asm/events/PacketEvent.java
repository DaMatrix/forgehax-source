/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketEvent
extends Event {
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packetIn) {
        this.packet = packetIn;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public static class Received
    extends PacketEvent {
        public Received(Packet<?> packetIn) {
            super(packetIn);
        }

        public static class Post
        extends Received {
            public Post(Packet<?> packetIn) {
                super(packetIn);
            }
        }

        @Cancelable
        public static class Pre
        extends Received {
            public Pre(Packet<?> packetIn) {
                super(packetIn);
            }
        }

    }

    public static class Send
    extends PacketEvent {
        public Send(Packet<?> packetIn) {
            super(packetIn);
        }

        public static class Post
        extends Send {
            public Post(Packet<?> packetIn) {
                super(packetIn);
            }
        }

        @Cancelable
        public static class Pre
        extends Send {
            public Pre(Packet<?> packetIn) {
                super(packetIn);
            }
        }

    }

}

