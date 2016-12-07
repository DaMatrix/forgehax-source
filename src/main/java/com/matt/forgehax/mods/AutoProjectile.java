/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import com.matt.forgehax.util.math.Angle;
import com.matt.forgehax.util.math.ProjectileUtils;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoProjectile
extends ToggleMod {
    public AutoProjectile(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onSendingPacket(PacketEvent.Send.Pre event) {
        EntityPlayerSP localPlayer = AutoProjectile.MC.thePlayer;
        if (!LocalPlayerUtils.isProjectileTargetAcquired() && !LocalPlayerUtils.isFakeAnglesActive()) {
            if (event.getPacket() instanceof CPacketPlayerDigging && ((CPacketPlayerDigging)event.getPacket()).getAction().equals((Object)CPacketPlayerDigging.Action.RELEASE_USE_ITEM) && !Utils.OUTGOING_PACKET_IGNORE_LIST.contains((Object)event.getPacket())) {
                ItemStack heldItem = localPlayer.getHeldItemMainhand();
                RayTraceResult trace = localPlayer.rayTrace(9999.0, 0.0f);
                if (heldItem != null && AutoProjectile.getNetworkManager() != null && trace != null && ProjectileUtils.isBow(heldItem)) {
                    Angle oldViewAngles = LocalPlayerUtils.getViewAngles();
                    LocalPlayerUtils.sendRotatePacket(ProjectileUtils.getBestPitch(heldItem, trace.hitVec), oldViewAngles.getYaw());
                    CPacketPlayerDigging usePacket = new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
                    Utils.OUTGOING_PACKET_IGNORE_LIST.add((Packet)usePacket);
                    AutoProjectile.getNetworkManager().sendPacket((Packet)usePacket);
                    LocalPlayerUtils.sendRotatePacket(oldViewAngles);
                    event.setCanceled(true);
                }
            } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && ((CPacketPlayerTryUseItem)event.getPacket()).getHand().equals((Object)EnumHand.MAIN_HAND) && !Utils.OUTGOING_PACKET_IGNORE_LIST.contains((Object)event.getPacket())) {
                ItemStack heldItem = localPlayer.getHeldItemMainhand();
                RayTraceResult trace = localPlayer.rayTrace(9999.0, 0.0f);
                if (heldItem != null && trace != null && ProjectileUtils.isThrowable(heldItem) && !ProjectileUtils.isBow(heldItem)) {
                    LocalPlayerUtils.sendRotatePacket(ProjectileUtils.getBestPitch(heldItem, trace.hitVec), LocalPlayerUtils.getViewAngles().getYaw());
                    CPacketPlayerTryUseItem usePacket = new CPacketPlayerTryUseItem(((CPacketPlayerTryUseItem)event.getPacket()).getHand());
                    Utils.OUTGOING_PACKET_IGNORE_LIST.add((Packet)usePacket);
                    AutoProjectile.getNetworkManager().sendPacket((Packet)usePacket);
                    LocalPlayerUtils.sendRotatePacket(LocalPlayerUtils.getViewAngles());
                    event.setCanceled(true);
                }
            }
        }
    }
}

