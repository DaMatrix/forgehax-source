/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.event.world.WorldEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.util;

import com.matt.forgehax.asm.events.PacketEvent;
import java.util.Arrays;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LagCompensator {
    private static final LagCompensator INSTANCE = new LagCompensator();
    private static final int MAX_TICKRATE = 20;
    private static final int MIN_TICKRATE = 0;
    private static final int SAMPLE_SIZE = 100;
    private final float[] tickRates = new float[100];
    private final LagCompensatorEventHandler eventHandler;
    private int nextIndex;
    private long timeLastTimeUpdate;

    public static LagCompensator getInstance() {
        return INSTANCE;
    }

    public LagCompensator() {
        this.eventHandler = new LagCompensatorEventHandler(this);
        this.nextIndex = 0;
        this.reset();
    }

    public int getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (float tickRate : this.tickRates) {
            if (tickRate <= 0.0f) continue;
            sumTickRates += tickRate;
            numTicks += 1.0f;
        }
        return Math.round(MathHelper.clamp_float((float)(sumTickRates / numTicks), (float)0.0f, (float)20.0f));
    }

    public LagCompensatorEventHandler getEventHandler() {
        return this.eventHandler;
    }

    public void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1) {
            float timeElapsed = (float)(System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            this.tickRates[this.nextIndex % this.tickRates.length] = MathHelper.clamp_float((float)(20.0f / timeElapsed), (float)0.0f, (float)20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }

    public void reset() {
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1;
        Arrays.fill(this.tickRates, 0.0f);
    }

    private class LagCompensatorEventHandler {
        private final LagCompensator lagCompensator;

        public LagCompensatorEventHandler(LagCompensator lagCompensator2) {
            this.lagCompensator = lagCompensator2;
        }

        @SubscribeEvent
        public void onWorldLoaded(WorldEvent.Load event) {
            this.lagCompensator.reset();
        }

        @SubscribeEvent
        public void onPacketPreceived(PacketEvent.Received.Pre event) {
            if (event.getPacket() instanceof SPacketTimeUpdate) {
                this.lagCompensator.onTimeUpdate();
            }
        }
    }

}

