/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class HurtCamEffectEvent
extends Event {
    private final float partialTicks;

    public HurtCamEffectEvent(float pt) {
        this.partialTicks = pt;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

