/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockPortal
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityEndGateway
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.events.BlockRenderEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.draw.RenderUtils;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PortalFinderMod
extends ToggleMod {
    public PortalFinderMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for (TileEntity tileEntity : PortalFinderMod.getWorld().loadedTileEntityList) {
            BlockPos pos = tileEntity.getPos();
            if (!(tileEntity instanceof TileEntityEndGateway)) continue;
            RenderUtils.drawBox(pos, pos.add(1, 1, 1), Utils.Colors.GREEN, 2.0f, true);
        }
    }

    @SubscribeEvent
    public void onBlockRender(BlockRenderEvent event) {
        Block block = event.getState().getBlock();
        if (block instanceof BlockPortal) {
            AxisAlignedBB bb = event.getState().getBoundingBox(event.getAccess(), event.getPos());
        }
    }
}

