/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.VertexBuffer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockRenderEvent
extends Event {
    private final BlockPos pos;
    private final IBlockState state;
    private final IBlockAccess access;
    private final VertexBuffer buffer;

    public BlockRenderEvent(BlockPos pos, IBlockState state, IBlockAccess access, VertexBuffer buffer) {
        this.pos = pos;
        this.state = state;
        this.access = access;
        this.buffer = buffer;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public IBlockAccess getAccess() {
        return this.access;
    }

    public IBlockState getState() {
        return this.state;
    }

    public VertexBuffer getBuffer() {
        return this.buffer;
    }
}

