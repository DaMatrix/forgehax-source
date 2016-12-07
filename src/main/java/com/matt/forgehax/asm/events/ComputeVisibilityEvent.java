/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.chunk.SetVisibility
 *  net.minecraft.client.renderer.chunk.VisGraph
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ComputeVisibilityEvent
extends Event {
    private final VisGraph visGraph;
    private final SetVisibility setVisibility;

    public ComputeVisibilityEvent(VisGraph visGraph, SetVisibility setVisibility) {
        this.visGraph = visGraph;
        this.setVisibility = setVisibility;
    }

    public VisGraph getVisGraph() {
        return this.visGraph;
    }

    public SetVisibility getSetVisibility() {
        return this.setVisibility;
    }
}

