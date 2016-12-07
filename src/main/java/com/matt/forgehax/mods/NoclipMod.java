/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoclipMod
extends ToggleMod {
    public NoclipMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onDisabled() {
        if (NoclipMod.getLocalPlayer() != null) {
            NoclipMod.getLocalPlayer().noClip = false;
        }
    }

    @SubscribeEvent
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
        EntityPlayer localPlayer = NoclipMod.getLocalPlayer();
        localPlayer.noClip = true;
        localPlayer.onGround = false;
        localPlayer.fallDistance = 0.0f;
    }
}

