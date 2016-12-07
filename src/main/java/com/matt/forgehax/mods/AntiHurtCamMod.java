/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.events.HurtCamEffectEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiHurtCamMod
extends ToggleMod {
    public AntiHurtCamMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onHurtCamEffect(HurtCamEffectEvent event) {
        event.setCanceled(true);
    }
}

