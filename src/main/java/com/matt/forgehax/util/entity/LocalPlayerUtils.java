/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.client.FMLClientHandler
 */
package com.matt.forgehax.util.entity;

import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.math.Angle;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;

public class LocalPlayerUtils
extends ForgeHaxBase {
    private static boolean projectileTargetAcquired = false;
    private static boolean activeFakeAngles = false;
    private static Entity targetEntity = null;
    private static Angle fakeViewAngles = null;
    private static Angle lastAngle = new Angle(0.0, 0.0, 0.0);

    public static boolean isFakeAnglesActive() {
        return activeFakeAngles;
    }

    public static void setActiveFakeAngles(boolean aiming) {
        activeFakeAngles = aiming;
    }

    public static boolean isProjectileTargetAcquired() {
        return projectileTargetAcquired;
    }

    public static void setProjectileTargetAcquired(boolean projectileTargetAcquired) {
        LocalPlayerUtils.projectileTargetAcquired = projectileTargetAcquired;
    }

    public static void setViewAngles(Angle angles) {
        LocalPlayerUtils.setViewAngles(angles.getPitch(), angles.getYaw());
    }

    public static void setViewAngles(double p, double y) {
        LocalPlayerUtils.getLocalPlayer().rotationYaw = (float)y;
        LocalPlayerUtils.getLocalPlayer().rotationPitch = (float)p;
    }

    public static Angle getViewAngles() {
        return new Angle(LocalPlayerUtils.getLocalPlayer().rotationPitch, LocalPlayerUtils.getLocalPlayer().rotationYaw);
    }

    public static void setFakeViewAngles(Angle fakeViewAngles) {
        LocalPlayerUtils.fakeViewAngles = fakeViewAngles;
    }

    public static Angle getFakeViewAngles() {
        return fakeViewAngles;
    }

    public static Vec3d getVelocity() {
        return new Vec3d(LocalPlayerUtils.getLocalPlayer().motionX, LocalPlayerUtils.getLocalPlayer().motionY, LocalPlayerUtils.getLocalPlayer().motionZ);
    }

    public static Entity getTargetEntity() {
        return targetEntity;
    }

    public static void setTargetEntity(@Nullable Entity targetEntity) {
        LocalPlayerUtils.targetEntity = targetEntity;
    }

    public static boolean isTargetEntity(Entity entity) {
        return targetEntity != null && targetEntity.equals((Object)entity);
    }

    public static void sendRotatePacket(double pitch, double yaw) {
        if (lastAngle.getPitch() != pitch || lastAngle.getYaw() != yaw) {
            FMLClientHandler.instance().getClientToServerNetworkManager().sendPacket((Packet)new CPacketPlayer.Rotation((float)yaw, (float)pitch, LocalPlayerUtils.MC.thePlayer.onGround));
            lastAngle.setPitch(pitch);
            lastAngle.setYaw(yaw);
        }
    }

    public static void sendRotatePacket(Angle angle) {
        LocalPlayerUtils.sendRotatePacket(angle.getPitch(), angle.getYaw());
    }
}

