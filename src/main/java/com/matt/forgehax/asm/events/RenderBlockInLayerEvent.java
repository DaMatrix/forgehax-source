/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraftforge.fml.common.eventhandler.Cancelable
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package com.matt.forgehax.asm.events;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderBlockInLayerEvent
extends Event {
    private final Block block;
    private final IBlockState state;
    private final BlockRenderLayer layer;
    private boolean returnValue;

    public RenderBlockInLayerEvent(Block block, IBlockState state, BlockRenderLayer layer, boolean returnValue) {
        this.block = block;
        this.state = state;
        this.layer = layer;
        this.returnValue = returnValue;
    }

    public Block getBlock() {
        return this.block;
    }

    public BlockRenderLayer getLayer() {
        return this.layer;
    }

    public IBlockState getState() {
        return this.state;
    }

    public boolean getReturnValue() {
        return this.returnValue;
    }

    public void setReturnValue(boolean returnValue) {
        this.returnValue = returnValue;
    }
}

