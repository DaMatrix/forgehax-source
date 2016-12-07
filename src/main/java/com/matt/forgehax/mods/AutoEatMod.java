/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.FoodStats
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.key.Bindings;
import com.matt.forgehax.util.key.KeyBindingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoEatMod
extends ToggleMod {
    private boolean isEating = false;

    public AutoEatMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
        FoodStats foodStats = AutoEatMod.getLocalPlayer().getFoodStats();
        int foodSlot = -1;
        ItemStack foodStack = null;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = AutoEatMod.getLocalPlayer().inventory.getStackInSlot(i);
            if (stack == null || !(stack.getItem() instanceof ItemFood)) continue;
            foodSlot = i;
            foodStack = stack;
            break;
        }
        if (foodStack != null) {
            ItemFood itemFood = (ItemFood)foodStack.getItem();
            if (20 - foodStats.getFoodLevel() >= itemFood.getHealAmount(foodStack)) {
                this.isEating = true;
                AutoEatMod.MC.thePlayer.inventory.currentItem = foodSlot;
                Bindings.use.setPressed(true);
                return;
            }
        }
        if (this.isEating) {
            Bindings.use.setPressed(false);
            this.isEating = false;
        }
    }
}

