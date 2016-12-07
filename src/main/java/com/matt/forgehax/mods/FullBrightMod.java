/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullBrightMod
extends ToggleMod {
    public FullBrightMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onEnabled() {
        FullBrightMod.MC.gameSettings.gammaSetting = 16.0f;
    }

    @Override
    public void onDisabled() {
        FullBrightMod.MC.gameSettings.gammaSetting = 1.0f;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        FullBrightMod.MC.gameSettings.gammaSetting = 16.0f;
    }
}

