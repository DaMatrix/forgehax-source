/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.asm.events.DoBlockCollisionsEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlowdown
extends ToggleMod {
    public NoSlowdown(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onEnabled() {
        ForgeHaxHooks.isNoSlowDownActivated = true;
    }

    @Override
    public void onDisabled() {
        ForgeHaxHooks.isNoSlowDownActivated = false;
    }

    @SubscribeEvent
    public void onDoApplyBlockMovement(DoBlockCollisionsEvent event) {
        if (event.getEntity().equals((Object)NoSlowdown.getLocalPlayer()) && Block.getIdFromBlock((Block)event.getState().getBlock()) == 88) {
            event.setCanceled(true);
        }
    }
}

