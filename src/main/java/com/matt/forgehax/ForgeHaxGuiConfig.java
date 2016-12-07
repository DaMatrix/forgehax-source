/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraftforge.fml.client.config.GuiConfig
 *  net.minecraftforge.fml.client.config.IConfigElement
 */
package com.matt.forgehax;

import com.google.common.collect.Lists;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.mods.BaseMod;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ForgeHaxGuiConfig
extends GuiConfig {
    public ForgeHaxGuiConfig(GuiScreen parent) {
        super(parent, ForgeHaxGuiConfig.getConfigElements(), "forgehax", false, false, "ForgeHax configuration");
        this.titleLine2 = "settings.txt";
    }
    
    private static List<IConfigElement> getConfigElements() {
        ArrayList elements = Lists.newArrayList();
        for (Map.Entry<String, BaseMod> entry : ForgeHax.instance().mods.entrySet()) {
            entry.getValue().onConfigBuildGui(elements);
        }
        return elements;
    }
    @Override
    public void initGui() {
        super.initGui();
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
    }
}

