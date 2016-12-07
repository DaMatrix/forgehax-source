/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoRespawnMod
extends ToggleMod {
    public AutoRespawnMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
        if (AutoRespawnMod.getLocalPlayer().getHealth() <= 0.0f) {
            AutoRespawnMod.getLocalPlayer().respawnPlayer();
        }
    }
}

