/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockDynamicLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemFishingRod
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoFishMod
extends ToggleMod {
    private static final int CAST_DELAY = 20;
    private int castTickDelay = 0;
    private boolean lineCasted = false;
    private int tickToRecast = 0;

    public AutoFishMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onEnabled() {
        this.castTickDelay = 0;
        this.lineCasted = false;
        this.tickToRecast = 0;
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
        EntityPlayer localPlayer = AutoFishMod.getLocalPlayer();
        ItemStack heldStack = localPlayer.getHeldItemMainhand();
        if (this.castTickDelay > 0) {
            --this.castTickDelay;
        }
        if (this.tickToRecast > 0) {
            --this.tickToRecast;
        }
        if (heldStack != null && heldStack.getItem() instanceof ItemFishingRod) {
            if (localPlayer.fishEntity != null) {
                EntityFishHook fishHook = localPlayer.fishEntity;
                Block blockOn = AutoFishMod.getWorld().getBlockState(new BlockPos(fishHook.posX, fishHook.posY - 1.0, fishHook.posY)).getBlock();
                if (this.lineCasted && !blockOn.equals((Object)Blocks.FLOWING_WATER) && fishHook.motionX == 0.0 && fishHook.motionY != 0.0 && fishHook.motionZ == 0.0) {
                	//TODO: Comment to remind me to change this for the next update
                    ForgeHax.callPrivateMethod(MC.getClass(), MC, new Object[] {}, "rightClickMouse", "func_147121_ag");
                    this.lineCasted = false;
                    this.castTickDelay = 20;
                }
            } else if (!(this.lineCasted && this.tickToRecast > 0 || this.castTickDelay > 0)) {
            	//TODO: Comment to remind me to change this for the next update
            	ForgeHax.callPrivateMethod(MC.getClass(), MC, new Object[] {}, "rightClickMouse", "func_147121_ag");
                this.lineCasted = true;
                this.tickToRecast = 20;
                this.castTickDelay = 0;
            }
        }
    }
}

