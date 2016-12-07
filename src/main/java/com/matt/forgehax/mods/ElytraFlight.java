/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ElytraFlight
extends ToggleMod {
    private Property speed;

    public ElytraFlight(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.speed = configuration.get(this.getModName(), "speed", 0.05, "Flight speed");
        Property[] arrproperty = new Property[]{this.speed};
        this.addSettings(arrproperty);
    }

    @Override
    public void onDisabled() {
        if (ElytraFlight.MC.thePlayer != null) {
            ElytraFlight.MC.thePlayer.capabilities.isFlying = false;
            ElytraFlight.getNetworkManager().sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlight.MC.thePlayer, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }

    @SubscribeEvent
    @SideOnly(value=Side.CLIENT)
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
        if (ElytraFlight.MC.thePlayer.isElytraFlying()) {
            ElytraFlight.MC.thePlayer.capabilities.isFlying = true;
        }
        ElytraFlight.MC.thePlayer.capabilities.setFlySpeed((float)this.speed.getDouble());
    }
}

