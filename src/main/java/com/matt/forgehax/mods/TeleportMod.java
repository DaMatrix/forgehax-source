/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.mods.ToggleMod;
import java.util.Scanner;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TeleportMod
extends ToggleMod {
    public Property mode;
    public Property relative;

    public TeleportMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.mode = configuration.get(this.getModName(), "teleport_mode", 0, "Packet teleport types", 0, 5);
        Property[] arrproperty = new Property[]{this.mode, this.relative = configuration.get(this.getModName(), "teleport_relative", false, ".setpos will tp you relative to your current position")};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onPacketSent(PacketEvent.Send.Pre event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String message = ((CPacketChatMessage)event.getPacket()).getMessage();
            Scanner scanner = new Scanner(message);
            scanner.useDelimiter(" ");
            if (scanner.next().equals(".setpos")) {
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                boolean onGround = true;
                try {
                    x = Double.parseDouble(scanner.next());
                    y = Double.parseDouble(scanner.next());
                    z = Double.parseDouble(scanner.next());
                    if (scanner.hasNext()) {
                        try {
                            onGround = Boolean.parseBoolean(scanner.next());
                        }
                        catch (Exception e) {
                            ForgeHax.instance().printStackTrace(e);
                        }
                    }
                    if (this.relative.getBoolean()) {
                        Vec3d pos = TeleportMod.getLocalPlayer().getPositionVector();
                        x = pos.xCoord + x;
                        y = pos.yCoord + y;
                        z = pos.zCoord + z;
                    }
                    switch (this.mode.getInt()) {
                        default: {
                            if (TeleportMod.getLocalPlayer().isRiding() && TeleportMod.getLocalPlayer().getRidingEntity() != null) {
                                TeleportMod.getLocalPlayer().getRidingEntity().setPosition(x, y, z);
                                break;
                            }
                            TeleportMod.getLocalPlayer().setPosition(x, y, z);
                            break;
                        }
                        case 1: {
                            TeleportMod.getNetworkManager().sendPacket((Packet)new CPacketPlayer.Position(x, y, z, onGround));
                            break;
                        }
                        case 2: {
                            TeleportMod.getNetworkManager().sendPacket((Packet)new CPacketConfirmTeleport());
                        }
                    }
                    TeleportMod.getLocalPlayer().addChatMessage(new TextComponentString("Attempted teleport using mode " + this.mode.getInt()));
                }
                catch (Exception e) {
                    ForgeHax.instance().printStackTrace(e);
                }
                event.setCanceled(true);
            }
            scanner.close();
        }
    }
}

