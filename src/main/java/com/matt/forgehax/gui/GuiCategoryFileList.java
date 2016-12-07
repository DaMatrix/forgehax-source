/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  jline.internal.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraftforge.fml.client.GuiScrollingList
 */
package com.matt.forgehax.gui;

import com.google.common.collect.Lists;
import com.matt.forgehax.gui.categories.IGuiCategory;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.container.ContainerList;
import com.matt.forgehax.util.draw.SurfaceUtils;
import java.util.Collection;
import java.util.List;
import jline.internal.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiCategoryFileList
extends GuiScrollingList {
    private final GuiScreen parent;
    private final IGuiCategory callback;
    private final List<Object> elements = Lists.newArrayList();
    private int selectedIndex = -1;

    public GuiCategoryFileList(GuiScreen parent, int x, int y, int width, int height, int slotHeight, int screenWidth, int screenHeight, Collection<?> listCollection) {
        super(parent.mc, width, height, y, y + height, x, slotHeight, screenWidth, screenHeight);
        this.parent = parent;
        this.callback = parent instanceof IGuiCategory ? (IGuiCategory)parent : null;
        for (Object list : listCollection) {
            this.elements.add(list);
        }
    }

    public boolean isValidIndex(int index) {
        return index > -1 && index < this.elements.size();
    }

    public void setSelectedIndex(int index) {
        this.setSelected(this.elements.get(index));
    }

    public void setSelected(@Nullable Object selected) {
        if (selected != null) {
            int index = this.elements.indexOf(selected);
            if (this.callback != null && this.isValidIndex(index)) {
                this.selectedIndex = index;
                this.callback.onSelectedFileFromList(selected);
            }
        } else {
            this.selectedIndex = -1;
        }
    }

    protected int getSize() {
        return this.elements.size();
    }

    protected void elementClicked(int index, boolean doubleClick) {
        this.setSelectedIndex(index);
    }

    protected boolean isSelected(int index) {
        return this.selectedIndex == index;
    }

    protected void drawBackground() {
        int scale = 2;
        SurfaceUtils.drawRect(this.left - scale, this.top - scale, this.listWidth + 2 * scale, this.listHeight + 2 * scale, Utils.Colors.BLACK);
    }

    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        int x = entryRight - this.listWidth + slotBuffer + 2;
        int y = slotTop;
        Object atIndex = this.elements.get(slotIdx);
        if (atIndex instanceof ContainerList) {
            ContainerList list = (ContainerList)atIndex;
            SurfaceUtils.drawTextShadow(list.getName(), x, y, Utils.Colors.WHITE);
        }
    }
}

