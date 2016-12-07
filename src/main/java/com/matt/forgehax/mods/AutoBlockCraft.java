/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiCrafting
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoBlockCraft
extends ToggleMod {
    public Property blockToCraft;
    public Property sleepTime;

    public AutoBlockCraft(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    public CraftableBlocks getSelectedOption() {
        for (CraftableBlocks block : CraftableBlocks.values()) {
            if (!this.blockToCraft.getString().equals(block.name())) continue;
            return block;
        }
        return null;
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.blockToCraft = configuration.get(this.getModName(), "crafting_block", CraftableBlocks.GOLD_BLOCK.name(), "Block to craft", Utils.toArray((Enum[])CraftableBlocks.values()));
        Property[] arrproperty = new Property[]{this.blockToCraft, this.sleepTime = configuration.get(this.getModName(), "sleep_delay", 100, "Time between clicks in ms")};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiCrafting) {
            try {
                GuiCraftingOverride override = new GuiCraftingOverride(AutoBlockCraft.MC.thePlayer.inventory, (World)AutoBlockCraft.MC.theWorld, this.getSelectedOption().getRecipeBlock(), this.sleepTime.getInt());
                event.setGui((GuiScreen)override);
            }
            catch (Exception e) {
                MOD.printStackTrace(e);
            }
        }
    }

    private static class GuiCraftingOverride
    extends GuiCrafting {
        private long sleepTime;
        private ResourceLocation toCraft;
        public static final int CRAFTING_RESULT_SLOT = 0;
        public static final int START_INDEX = 10;

        public GuiCraftingOverride(InventoryPlayer playerInv, World worldIn, ResourceLocation blockToCraft, int sleepTime) {
            super(playerInv, worldIn);
            this.toCraft = blockToCraft;
            this.sleepTime = sleepTime;
            this.autoCraft();
        }

        protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
            super.handleMouseClick(slotIn, slotId, mouseButton, type);
        }

        public Container getInventory() {
            return this.inventorySlots;
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

        public int findItem(@Nullable ItemStack stack, int startIndex) {
            for (int i = startIndex; i < this.getInventory().inventorySlots.size(); ++i) {
                Slot slot = this.getInventory().getSlot(i);
                if (stack == null && !slot.getHasStack()) {
                    return slot.slotNumber;
                }
                if (stack == null || !stack.isItemEqual(slot.getStack()) || ForgeHax.getStackSizeFromItemStack(slot.getStack()) < ForgeHax.getStackSizeFromItemStack(stack)) continue;
                return slot.slotNumber;
            }
            return -1;
        }

        public int findItem(@Nullable ItemStack stack) {
            return this.findItem(stack, 10);
        }

        public int findEmptySlot() {
            return this.findItem(null);
        }

        public int findExtraInUsedSlot(ItemStack item) {
            for (int i = 10; i < this.getInventory().inventorySlots.size(); ++i) {
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

        public synchronized void autoCraft() {
            try {
                final GuiCraftingOverride INSTANCE = this;
                Item itemToCraft = Item.getByNameOrId((String)this.toCraft.toString());
                final ItemStack recipeStack = new ItemStack(itemToCraft, 64);
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        block6 : {
                            try {
                                while (INSTANCE.equals((Object)ForgeHaxBase.MC.currentScreen)) {
                                    GuiCraftingOverride.this.sleep();
                                    int nextStartIndex = 1;
                                    for (int i = 0; i < 9; ++i) {
                                        int slotFound = GuiCraftingOverride.this.findItem(recipeStack, nextStartIndex);
                                        if (slotFound != -1) {
                                            nextStartIndex = slotFound + 1;
                                            continue;
                                        }
                                        break block6;
                                    }
                                    for (int slotIndex = 1; slotIndex <= 9 && (GuiCraftingOverride.this.getSlot(slotIndex).getHasStack() || GuiCraftingOverride.this.fillSlotWithItemFromInv(slotIndex, recipeStack)); ++slotIndex) {
                                    }
                                    GuiCraftingOverride.this.quickMove(0);
                                    GuiCraftingOverride.this.sleep();
                                }
                            }
                            catch (Exception e) {
                                ForgeHaxBase.MOD.printStackTrace(e);
                            }
                        }
                    }
                }).start();
            }
            catch (Exception e) {
                ForgeHaxBase.MOD.printStackTrace(e);
            }
        }

        private void sleep() throws Exception {
            Thread.sleep(this.sleepTime);
        }

    }

    public static enum CraftableBlocks {
        GOLD_BLOCK("minecraft:gold_ingot"),
        DIAMOND_BLOCK("minecraft:diamond"),
        IRON_BLOCK("minecraft:iron_ingot"),
        EMERALD_BLOCK("minecraft:emerald"),
        COAL_BLOCK("minecraft:coal"),
        REDSTONE_BLOCK("minecraft:redstone"),
        HAYBALE_BLOCK("minecraft:wheat"),
        SLIME_BLOCK("minecraft:slime_ball"),
        MELON_BLOCK("minecraft:melon"),
        GOLD_INGOT("minecraft:gold_nugget");
        
        private final ResourceLocation recipeBlock;

        private CraftableBlocks(String recipe) {
            this.recipeBlock = new ResourceLocation(recipe);
        }

        public ResourceLocation getRecipeBlock() {
            return this.recipeBlock;
        }
    }

}

