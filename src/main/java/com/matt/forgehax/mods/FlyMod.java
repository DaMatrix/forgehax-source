/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.key.Bindings;
import com.matt.forgehax.util.key.KeyBindingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FlyMod
extends ToggleMod {
    private static final int VANILLA_Y_CHANGE_TIME = 2000;
    public Property speed;
    public Property vanillaBypass;
    private long lastPosResetTime = 0;

    public FlyMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.speed = configuration.get(this.getModName(), "speed", 0.05, "Flight speed");
        Property[] arrproperty = new Property[]{this.speed, this.vanillaBypass = configuration.get(this.getModName(), "vanilla_bypass", false, "Bypass vanilla fly checks")};
        this.addSettings(arrproperty);
    }

    @Override
    public void onEnabled() {
        this.lastPosResetTime = System.currentTimeMillis();
    }

    @Override
    public void onDisabled() {
        this.lastPosResetTime = 0;
    }

    @SubscribeEvent
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
        EntityPlayer localPlayer = FlyMod.getLocalPlayer();
        FlyMod.MC.thePlayer.capabilities.isFlying = false;
        double speedFactor = this.speed.getDouble();
        localPlayer.motionX = 0.0;
        localPlayer.motionY = 0.0;
        localPlayer.motionZ = 0.0;
        localPlayer.jumpMovementFactor = (float)speedFactor;
        if (Bindings.jump.getBinding().isKeyDown()) {
            localPlayer.motionY += speedFactor;
        }
        if (Bindings.sneak.getBinding().isKeyDown()) {
            localPlayer.motionY -= speedFactor;
        }
    }
}

