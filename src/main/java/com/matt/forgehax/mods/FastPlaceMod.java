/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastPlaceMod
extends ToggleMod {
    public FastPlaceMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
    	//TODO: Comment to remind me to change this for the next update
    	ObfuscationReflectionHelper.setPrivateValue(Minecraft.class, MC, 0, "rightClickDelayTimer", "field_71467_ac");
    }
}

