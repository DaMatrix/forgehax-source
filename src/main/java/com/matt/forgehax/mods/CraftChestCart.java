/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.mods.ToggleMod;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CraftChestCart
extends ToggleMod {
    private Property sleep;

    public CraftChestCart(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.sleep = configuration.get(this.getModName(), "sleepTime", 75, "Sleep time in ms");
        Property[] arrproperty = new Property[]{this.sleep};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() != null && event.getGui() instanceof GuiInventory) {
            GuiInventoryOverride list = new GuiInventoryOverride((EntityPlayer)CraftChestCart.MC.thePlayer, this.sleep.getInt());
            event.setGui((GuiScreen)list);
            list.autoCraftCarts();
        }
    }

    public static class GuiInventoryOverride
    extends GuiInventory {
        public static final int CRAFTING_SLOT1_INDEX = 1;
        public static final int CRAFTING_SLOT2_INDEX = 2;
        public static final int CRAFTING_SLOT3_INDEX = 3;
        public static final int CRAFTING_SLOT4_INDEX = 4;
        public static final int CRAFTING_SLOT_RESULT_INDEX = 0;
        public static final int START_INDEX = 5;
        private long sleepTime = 0;
        private static final Item CHEST = Item.getItemById((int)54);
        private static final Item CART = Item.getItemById((int)328);

        public GuiInventoryOverride(EntityPlayer player, long sleep) {
            super(player);
            this.sleepTime = sleep;
        }

        protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
            super.handleMouseClick(slotIn, slotId, mouseButton, type);
        }

        public Container getInventory() {
            return ForgeHaxBase.MC.thePlayer.inventoryContainer;
        }

        public InventoryPlayer getPlayerInventory() {
            return ForgeHaxBase.MC.thePlayer.inventory;
        }

        public Slot getSlot(int index) {
            return this.getInventory().getSlot(index);
        }

        public void simulateClick(int id, int mouseButton, ClickType type) {
            this.handleMouseClick(null, id, mouseButton, type);
        }

        public int findItem(@Nullable ItemStack stack) {
            for (int i = 5; i < this.getInventory().inventorySlots.size() - 1; ++i) {
                Slot slot = this.getInventory().getSlot(i);
                if (stack == null && !slot.getHasStack()) {
                    return slot.slotNumber;
                }
                if (stack == null || !stack.isItemEqual(slot.getStack()) || ForgeHax.getStackSizeFromItemStack(slot.getStack()) < ForgeHax.getStackSizeFromItemStack(stack)) continue;
                return slot.slotNumber;
            }
            return -1;
        }

        public int findEmptySlot() {
            return this.findItem(null);
        }

        public int findExtraInUsedSlot(ItemStack item) {
            for (int i = 5; i < this.getInventory().inventorySlots.size() - 1; ++i) {
                Slot slot = this.getInventory().getSlot(i);
                if (!slot.getHasStack() || !item.isItemEqual(slot.getStack()) || ForgeHax.getStackSizeFromItemStack(slot.getStack()) >= slot.getStack().getMaxStackSize()) continue;
                return slot.slotNumber;
            }
            return -1;
        }

        public void pickupSlot(int id) {
            this.simulateClick(id, 0, ClickType.PICKUP);
        }

        public void placeSlot(int id) {
            this.pickupSlot(id);
        }

        public void pickupAllSlot(int id) {
            this.simulateClick(id, 0, ClickType.PICKUP_ALL);
        }

        public void quickMove(int id) {
            this.simulateClick(id, 0, ClickType.QUICK_MOVE);
        }

        public void throwInHand() {
            this.simulateClick(-999, 0, ClickType.THROW);
        }

        public boolean isValidStacks(ItemStack recipeStack, ItemStack stack) {
            return recipeStack.isItemEqual(stack) && ForgeHax.getStackSizeFromItemStack(stack) >= ForgeHax.getStackSizeFromItemStack(recipeStack);
        }

        public boolean fillSlotWithItemFromInv(int id, ItemStack stack) throws Exception {
            int buy = this.findItem(stack);
            if (buy == -1) {
                return false;
            }
            this.pickupSlot(buy);
            this.sleep();
            this.placeSlot(id);
            this.sleep();
            return true;
        }

        public boolean placeHeldItemIntoInventory() throws Exception {
            ItemStack stack = this.getPlayerInventory().getItemStack();
            while (stack != null) {
                int place = this.findExtraInUsedSlot(stack);
                if (place == -1) {
                    place = this.findEmptySlot();
                }
                if (place == -1) {
                    return false;
                }
                this.placeSlot(place);
                stack = this.getPlayerInventory().getItemStack();
                this.sleep();
            }
            return true;
        }

        public void moveStackToInv(int index) throws Exception {
            if (this.getSlot(index).getHasStack()) {
                this.pickupSlot(index);
                this.sleep();
                this.placeHeldItemIntoInventory();
            }
        }

        public void dropHeldItem() {
            if (this.getPlayerInventory().getItemStack() != null) {
                this.throwInHand();
            }
        }

        public boolean hasStackInSlot(int index, ItemStack stack) {
            Slot slot = this.getSlot(index);
            if (slot.getHasStack()) {
                ItemStack slotStack = slot.getStack();
                return slotStack != null && slotStack.getItem().equals((Object)stack.getItem()) && ForgeHax.getStackSizeFromItemStack(slotStack) < ForgeHax.getStackSizeFromItemStack(stack);
            }
            return false;
        }

        public synchronized void autoCraftCarts() {
            final GuiInventoryOverride INSTANCE = this;
            new Thread(new Runnable(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public void run() {
                    GuiInventoryOverride guiInventoryOverride = INSTANCE;
                    synchronized (guiInventoryOverride) {
                        try {
                            GuiInventoryOverride.this.sleep();
                            ItemStack STACK_CHEST = new ItemStack(CHEST, 1);
                            ItemStack STACK_CART = new ItemStack(CART);
                            while ((GuiInventoryOverride.this.getSlot(1).getHasStack() || GuiInventoryOverride.this.fillSlotWithItemFromInv(1, STACK_CHEST)) && (GuiInventoryOverride.this.getSlot(3).getHasStack() || GuiInventoryOverride.this.fillSlotWithItemFromInv(3, STACK_CART))) {
                                GuiInventoryOverride.this.moveStackToInv(0);
                                GuiInventoryOverride.this.sleep();
                            }
                            GuiInventoryOverride.this.moveStackToInv(1);
                            GuiInventoryOverride.this.sleep();
                            GuiInventoryOverride.this.moveStackToInv(3);
                            GuiInventoryOverride.this.sleep();
                            GuiInventoryOverride.this.dropHeldItem();
                        }
                        catch (Exception e) {
                            ForgeHaxBase.MOD.printStackTrace(e);
                        }
                    }
                }
            }).start();
        }

        private void sleep() throws Exception {
            Thread.sleep(this.sleepTime);
        }

    }

}

