/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.event.entity.living.LivingEvent
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package com.matt.forgehax.asm.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class ApplyClimbableBlockMovement
extends LivingEvent {
    public ApplyClimbableBlockMovement(EntityLivingBase entity) {
        super(entity);
    }
}

