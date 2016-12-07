/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ApplyCollisionMotionEvent
extends Event {
    private Entity entity;
    private Entity collidedWithEntity;
    private double motionX;
    private double motionY;
    private double motionZ;

    public ApplyCollisionMotionEvent(Entity entity, Entity collidedWithEntity, double mX, double mY, double mZ) {
        this.entity = entity;
        this.collidedWithEntity = collidedWithEntity;
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Entity getCollidedWithEntity() {
        return this.collidedWithEntity;
    }

    public double getMotionX() {
        return this.motionX;
    }

    public double getMotionY() {
        return this.motionY;
    }

    public double getMotionZ() {
        return this.motionZ;
    }
}

