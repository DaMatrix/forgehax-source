/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityMinecartChest
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityDispenser
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityFurnace
 *  net.minecraft.tileentity.TileEntityHopper
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.draw.RenderUtils;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StorageESPMod
extends ToggleMod {
    public StorageESPMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        BlockPos pos;
        for (TileEntity tileEntity : StorageESPMod.getWorld().loadedTileEntityList) {
            pos = tileEntity.getPos();
            if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityDispenser) {
                RenderUtils.drawBox(pos, pos.add(1, 1, 1), Utils.Colors.ORANGE, 2.0f, true);
                continue;
            }
            if (tileEntity instanceof TileEntityEnderChest) {
                RenderUtils.drawBox(pos, pos.add(1, 1, 1), Utils.Colors.PURPLE, 2.0f, true);
                continue;
            }
            if (tileEntity instanceof TileEntityFurnace) {
                RenderUtils.drawBox(pos, pos.add(1, 1, 1), Utils.Colors.GRAY, 2.0f, true);
                continue;
            }
            if (!(tileEntity instanceof TileEntityHopper)) continue;
            RenderUtils.drawBox(pos, pos.add(1, 1, 1), Utils.Colors.DARK_RED, 2.0f, true);
        }
        for (Entity entity : StorageESPMod.getWorld().loadedEntityList) {
            if (!(entity instanceof EntityMinecartChest)) continue;
            pos = entity.getPosition();
            RenderUtils.drawBox(pos, pos.add(1, 1, 1), Utils.Colors.ORANGE, 2.0f, true);
        }
    }
}

