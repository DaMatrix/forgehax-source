/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.event.entity.EntityEvent
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 */
package com.matt.forgehax.asm.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class DoBlockCollisionsEvent
extends EntityEvent {
    private final BlockPos pos;
    private final IBlockState state;

    public DoBlockCollisionsEvent(Entity entity, BlockPos pos, IBlockState state) {
        super(entity);
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public IBlockState getState() {
        return this.state;
    }
}

