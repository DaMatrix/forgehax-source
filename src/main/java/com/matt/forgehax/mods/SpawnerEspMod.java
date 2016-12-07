/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityMobSpawner
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnerEspMod
extends ToggleMod {
    public SpawnerEspMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for (TileEntity tileEntity : SpawnerEspMod.getWorld().loadedTileEntityList) {
            BlockPos pos = tileEntity.getPos();
            if (!(tileEntity instanceof TileEntityMobSpawner)) continue;
            RenderUtils.drawBox(pos, pos.add(1, 1, 1), Utils.Colors.RED, 2.0f, true);
        }
    }
}

