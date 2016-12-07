/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSleepMP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BedModeMod
extends ToggleMod {
    public BedModeMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
    	//TODO: Comment to remind me to change this for the next update
    	ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, BedModeMod.getLocalPlayer(), false, "sleeping", "field_71083_bS");;
    	ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, BedModeMod.getLocalPlayer(), 0, "sleepTimer", "field_71076_b");
    }

    @SubscribeEvent
    public void onGuiUpdate(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiSleepMP) {
            event.setCanceled(true);
        }
    }
}

