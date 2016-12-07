/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.client.GuiScrollingList
 */
package com.matt.forgehax.gui;

import com.google.common.collect.Lists;
import com.matt.forgehax.util.container.lists.ItemList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiItemList
extends GuiScrollingList {
    private final ItemList itemList;
    private final List<List<ItemStack>> ITEMS = Lists.newArrayList();

    public GuiItemList(GuiScreen parent, int x, int y, int width, int height, int slotHeight, int screenWidth, int screenHeight, List<ItemStack> allItems, ItemList selectedList) {
        super(parent.mc, width, height, y, y + height, x, slotHeight, screenWidth, screenHeight);
        java.lang.Object current = null;
        int pos = 0;
        for (int i = 0; i < allItems.size(); ++i) {
            if (pos >= this.ITEMS.size()) continue;
        }
        this.itemList = selectedList;
    }

    protected int getSize() {
        return this.itemList.size();
    }

    protected void elementClicked(int index, boolean doubleClick) {
    }

    protected boolean isSelected(int index) {
        return false;
    }

    protected void drawBackground() {
    }

    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
    }
}

