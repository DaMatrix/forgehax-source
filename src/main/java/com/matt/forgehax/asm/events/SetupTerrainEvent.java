/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SetupTerrainEvent
extends Event {
    private final Entity renderEntity;
    private boolean culling;

    public SetupTerrainEvent(Entity renderEntity, boolean culling) {
        this.renderEntity = renderEntity;
        this.culling = culling;
    }

    public Entity getRenderEntity() {
        return this.renderEntity;
    }

    public boolean isCulling() {
        return this.culling;
    }

    public void setCulling(boolean culling) {
        this.culling = culling;
    }
}

