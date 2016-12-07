/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.GuiScrollingList
 */
package com.matt.forgehax.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.matt.forgehax.gui.categories.PlayerListCategory;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.container.lists.PlayerList;
import com.matt.forgehax.util.draw.SurfaceUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiPlayerList
extends GuiScrollingList {
    private Minecraft MC;
    private GuiScreen parent;
    private PlayerList playerList;
    private List<Map.Entry<String, JsonElement>> selectedPlayerList = Lists.newArrayList();
    private List<ResourceLocation> playerSkins = Lists.newArrayList();
    private int selectedIndex = -1;

    public GuiPlayerList(GuiScreen parent, int x, int y, int width, int height, int slotHeight, int screenWidth, int screenHeight, PlayerList selectedList) {
        super(parent.mc, width, height, y, y + height, x, slotHeight, screenWidth, screenHeight);
        this.MC = parent.mc;
        this.parent = parent;
        if (selectedList != null) {
            this.playerList = selectedList;
            for (Map.Entry<String, JsonElement> entry : selectedList.entrySet()) {
                this.selectedPlayerList.add(entry);
                PlayerList.PlayerData data = new PlayerList.PlayerData(entry.getValue().getAsJsonObject()).setUuid(entry.getKey());
                ResourceLocation resourceLocation = AbstractClientPlayer.getLocationSkin((String)data.getName());
                AbstractClientPlayer.getDownloadImageSkin((ResourceLocation)resourceLocation, (String)data.getName());
                this.playerSkins.add(resourceLocation);
            }
        }
    }

    public boolean isValidIndex(int index) {
        return index > -1 && index < this.selectedPlayerList.size();
    }

    public Map.Entry<String, JsonElement> getCurrentlySelected() {
        return this.isValidIndex(this.selectedIndex) ? this.selectedPlayerList.get(this.selectedIndex) : null;
    }

    public int getSize() {
        return this.selectedPlayerList.size();
    }

    protected void elementClicked(int index, boolean doubleClick) {
        this.selectedIndex = index;
        if (this.parent instanceof PlayerListCategory.GuiPlayerManager) {
            ((PlayerListCategory.GuiPlayerManager)this.parent).updateButtonLocks();
        }
    }

    protected boolean isSelected(int index) {
        return this.selectedIndex == index;
    }

    protected void drawBackground() {
        int scale = 2;
        SurfaceUtils.drawRect(this.left - scale, this.top - scale, this.listWidth + 2 * scale, this.listHeight + 2 * scale, Utils.Colors.BLACK);
    }

    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        int x = entryRight - this.listWidth + slotBuffer / 2;
        int y = slotTop + 2;
        Map.Entry<String, JsonElement> selected = this.selectedPlayerList.get(slotIdx);
        if (selected != null) {
            PlayerList.PlayerData data = this.playerList.getPlayerData(selected.getKey());
            String name = data.getGuiName();
            String uuid = data.getUuid().toString();
            ResourceLocation skin = this.playerSkins.get(slotIdx);
            if (skin != null) {
                SurfaceUtils.drawHead(skin, x, y, 3.0f);
                x += 38;
            }
            SurfaceUtils.drawText(name, x, y, Utils.Colors.WHITE);
            SurfaceUtils.drawText(uuid, x, y + SurfaceUtils.getTextHeight() + 1, Utils.Colors.WHITE);
        }
    }
}

