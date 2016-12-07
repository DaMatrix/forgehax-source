/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.event.entity.EntityEvent
 */
package com.matt.forgehax.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;

public class EntityRemovedEvent
extends EntityEvent {
    public EntityRemovedEvent(Entity entity) {
        super(entity);
    }
}

