/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.client.FMLClientHandler
 */
package com.matt.forgehax;

import com.matt.forgehax.ForgeHax;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ForgeHaxBase {
    public static final Minecraft MC = FMLClientHandler.instance().getClient();
    public static final ForgeHax MOD = ForgeHax.instance();

    public static final EntityPlayer getLocalPlayer() {
        return ForgeHaxBase.MC.thePlayer;
    }

    public static final World getWorld() {
        return ForgeHaxBase.MC.theWorld;
    }

    public static final NetworkManager getNetworkManager() {
        return FMLClientHandler.instance().getClientToServerNetworkManager();
    }
}

