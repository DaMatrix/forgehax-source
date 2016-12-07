/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.PlayerCapabilities
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.util.Session
 *  net.minecraft.world.World
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreecamMod
extends ToggleMod {
    public Property speed;
    private double posX;
    private double posY;
    private double posZ;
    private float pitch;
    private float yaw;
    private EntityOtherPlayerMP clonedPlayer;

    public FreecamMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.speed = configuration.get(this.getModName(), "speed", 0.05, "Freecam speed");
        Property[] arrproperty = new Property[]{this.speed};
        this.addSettings(arrproperty);
    }

    @Override
    public void onEnabled() {
        EntityPlayer localPlayer = FreecamMod.getLocalPlayer();
        if (localPlayer != null) {
            this.posX = localPlayer.posX;
            this.posY = localPlayer.posY;
            this.posZ = localPlayer.posY;
            this.pitch = localPlayer.rotationPitch;
            this.yaw = localPlayer.rotationYaw;
            this.clonedPlayer = new EntityOtherPlayerMP((World)FreecamMod.MC.theWorld, MC.getSession().getProfile());
            this.clonedPlayer.clonePlayer(localPlayer, false);
            this.clonedPlayer.copyLocationAndAnglesFrom((Entity)localPlayer);
            this.clonedPlayer.rotationYawHead = localPlayer.rotationYawHead;
            FreecamMod.MC.theWorld.addEntityToWorld(-100, (Entity)this.clonedPlayer);
            FreecamMod.MC.thePlayer.capabilities.isFlying = true;
            FreecamMod.MC.thePlayer.capabilities.setFlySpeed((float)this.speed.getDouble());
            FreecamMod.MC.thePlayer.noClip = true;
        }
    }

    @Override
    public void onDisabled() {
        EntityPlayer localPlayer = FreecamMod.getLocalPlayer();
        if (localPlayer != null) {
            FreecamMod.MC.thePlayer.setPositionAndRotation(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
            FreecamMod.MC.theWorld.removeEntityFromWorld(-100);
            this.clonedPlayer = null;
            this.posZ = 0.0;
            this.posY = 0.0;
            this.posX = 0.0;
            this.yaw = 0.0f;
            this.pitch = 0.0f;
            FreecamMod.MC.thePlayer.capabilities.isFlying = false;
            FreecamMod.MC.thePlayer.capabilities.setFlySpeed(0.05f);
            FreecamMod.MC.thePlayer.noClip = false;
            FreecamMod.MC.thePlayer.motionZ = 0.0;
            FreecamMod.MC.thePlayer.motionY = 0.0;
            FreecamMod.MC.thePlayer.motionX = 0.0;
        }
    }

    @SubscribeEvent
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
        FreecamMod.MC.thePlayer.capabilities.isFlying = true;
        FreecamMod.MC.thePlayer.capabilities.setFlySpeed((float)this.speed.getDouble());
        FreecamMod.MC.thePlayer.noClip = true;
        FreecamMod.MC.thePlayer.onGround = false;
        FreecamMod.MC.thePlayer.fallDistance = 0.0f;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send.Pre event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            event.setCanceled(true);
        }
    }
}

