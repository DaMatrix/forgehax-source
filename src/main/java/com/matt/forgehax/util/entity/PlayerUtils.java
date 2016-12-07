/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 */
package com.matt.forgehax.util.entity;

import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.container.ContainerManager;
import com.matt.forgehax.util.container.lists.PlayerList;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerUtils {
    public static boolean isLocalPlayer(EntityPlayer player) {
        EntityPlayer localPlayer = ForgeHaxBase.getLocalPlayer();
        return localPlayer != null && localPlayer.equals((Object)player);
    }

    public static boolean isFriend(EntityPlayer player) {
        String uuid = player.getUniqueID().toString();
        for (Object o : ContainerManager.getContainerCollection(ContainerManager.Category.PLAYERS)) {
            PlayerList playerList = (PlayerList)o;
            if (!playerList.containsPlayer(uuid)) continue;
            return true;
        }
        return false;
    }
}

