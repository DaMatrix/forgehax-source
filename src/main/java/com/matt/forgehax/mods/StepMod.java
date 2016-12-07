/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.google.common.collect.Lists;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StepMod
extends ToggleMod {
    public static final float DEFAULT_STEP_HEIGHT = 0.6f;
    private CPacketPlayer previousPositionPacket = null;

    public StepMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onDisabled() {
        if (StepMod.getLocalPlayer() != null) {
            StepMod.getLocalPlayer().stepHeight = 0.6f;
        }
    }

    @SubscribeEvent
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
        EntityPlayer localPlayer = (EntityPlayer)event.getEntityLiving();
        localPlayer.stepHeight = localPlayer.onGround ? 1.0f : 0.6f;
    }

    @SubscribeEvent
    public void onPacketSending(PacketEvent.Send.Pre event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            double diffY;
            CPacketPlayer packetPlayer = (CPacketPlayer)event.getPacket();
            if (this.previousPositionPacket != null && !Utils.OUTGOING_PACKET_IGNORE_LIST.contains((Object)event.getPacket()) && (diffY = packetPlayer.getY(0.0) - this.previousPositionPacket.getY(0.0)) > 0.6000000238418579 && diffY <= 1.2491870787) {
                ArrayList<Packet> sendList = Lists.newArrayList();
                double x = this.previousPositionPacket.getX(0.0);
                double y = this.previousPositionPacket.getY(0.0);
                double z = this.previousPositionPacket.getZ(0.0);
                sendList.add(new CPacketPlayer.Position(x, y + 0.4199999869, z, true));
                sendList.add(new CPacketPlayer.Position(x, y + 0.7531999805, z, true));
                sendList.add(new CPacketPlayer.Position(packetPlayer.getX(0.0), packetPlayer.getY(0.0), packetPlayer.getZ(0.0), packetPlayer.isOnGround()));
                for (Packet toSend : sendList) {
                    Utils.OUTGOING_PACKET_IGNORE_LIST.add(toSend);
                    StepMod.getNetworkManager().sendPacket(toSend);
                }
                event.setCanceled(true);
            }
            this.previousPositionPacket = (CPacketPlayer)event.getPacket();
        }
    }
}

