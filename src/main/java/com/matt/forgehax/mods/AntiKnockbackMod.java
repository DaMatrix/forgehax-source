/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.events.ApplyCollisionMotionEvent;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.asm.events.WaterMovementEvent;
import com.matt.forgehax.asm.events.WebMotionEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiKnockbackMod
extends ToggleMod {
    private Property multiplierX;
    private Property multiplierY;
    private Property multiplierZ;

    public AntiKnockbackMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.multiplierX = configuration.get(this.getModName(), "multiplierX", 0.0, "X motion multiplier");
        Property[] arrproperty = new Property[]{this.multiplierX, this.multiplierY = configuration.get(this.getModName(), "multiplierY", 0.0, "Y motion multiplier"), this.multiplierZ = configuration.get(this.getModName(), "multiplierZ", 0.0, "Z motion multiplier")};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onPacketRecieved(PacketEvent.Received.Pre event) {
        if (event.getPacket() instanceof SPacketExplosion) {
        	//TODO: Comment to remind me to change this for the next update
        	ObfuscationReflectionHelper.setPrivateValue(SPacketExplosion.class, (SPacketExplosion) event.getPacket(), (float)((double)((SPacketExplosion)event.getPacket()).getMotionX() * this.multiplierX.getDouble()), "motionX", "field_149152_f");
        	ObfuscationReflectionHelper.setPrivateValue(SPacketExplosion.class, (SPacketExplosion) event.getPacket(), (float)((double)((SPacketExplosion)event.getPacket()).getMotionY() * this.multiplierY.getDouble()), "motionY", "field_149153_g");
        	ObfuscationReflectionHelper.setPrivateValue(SPacketExplosion.class, (SPacketExplosion) event.getPacket(), (float)((double)((SPacketExplosion)event.getPacket()).getMotionZ() * this.multiplierZ.getDouble()), "motionZ", "field_149159_h");
        }
        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == AntiKnockbackMod.MC.thePlayer.getEntityId()) {
            double multiX = this.multiplierX.getDouble();
            double multiY = this.multiplierY.getDouble();
            double multiZ = this.multiplierZ.getDouble();
            if (multiX == 0.0 && multiY == 0.0 && multiZ == 0.0) {
                event.setCanceled(true);
            } else {
            	ObfuscationReflectionHelper.setPrivateValue(SPacketEntityVelocity.class, (SPacketEntityVelocity) event.getPacket(), (int)((double)((SPacketEntityVelocity)event.getPacket()).getMotionX() * multiX), "motionX", "field_149415_b");
            	ObfuscationReflectionHelper.setPrivateValue(SPacketEntityVelocity.class, (SPacketEntityVelocity) event.getPacket(), (int)((double)((SPacketEntityVelocity)event.getPacket()).getMotionY() * multiY), "motionY", "field_149416_c");
            	ObfuscationReflectionHelper.setPrivateValue(SPacketEntityVelocity.class, (SPacketEntityVelocity) event.getPacket(), (int)((double)((SPacketEntityVelocity)event.getPacket()).getMotionZ() * multiZ), "motionZ", "field_149414_d");
            }
        }
    }

    @SubscribeEvent
    public void onWaterMovementEvent(WaterMovementEvent event) {
        if (event.getEntity().equals((Object)AntiKnockbackMod.MC.thePlayer)) {
            Vec3d moveDir = event.getMoveDir().normalize();
            event.getEntity().motionX += moveDir.xCoord * 0.014 * this.multiplierX.getDouble();
            event.getEntity().motionY += moveDir.yCoord * 0.014 * this.multiplierY.getDouble();
            event.getEntity().motionZ += moveDir.zCoord * 0.014 * this.multiplierZ.getDouble();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onApplyCollisionMotion(ApplyCollisionMotionEvent event) {
        if (event.getEntity().equals((Object)AntiKnockbackMod.MC.thePlayer)) {
            event.getEntity().addVelocity(event.getMotionX() * this.multiplierX.getDouble(), event.getMotionY() * this.multiplierY.getDouble(), event.getMotionZ() * this.multiplierZ.getDouble());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onWebMotion(WebMotionEvent event) {
        if (event.getEntity().equals((Object)AntiKnockbackMod.MC.thePlayer)) {
            double modifier = 1.0;
            event.setX(event.getX() * (0.25 * modifier));
            event.setY(event.getY() * (0.05000000074505806 * modifier));
            event.setZ(event.getZ() * (0.25 * modifier));
            event.setCanceled(true);
        }
    }
}

