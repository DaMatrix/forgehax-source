/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class WaterMovementEvent
extends Event {
    private Entity entity;
    private Vec3d movement;

    public WaterMovementEvent(Entity entity, Vec3d movement) {
        this.entity = entity;
        this.movement = movement;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Vec3d getMoveDir() {
        return this.movement;
    }
}

