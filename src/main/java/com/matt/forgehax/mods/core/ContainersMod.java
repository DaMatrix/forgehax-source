/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraftforge.fml.client.config.DummyConfigElement
 *  net.minecraftforge.fml.client.config.DummyConfigElement$DummyCategoryElement
 *  net.minecraftforge.fml.client.config.GuiConfig
 *  net.minecraftforge.fml.client.config.GuiConfigEntries
 *  net.minecraftforge.fml.client.config.GuiConfigEntries$CategoryEntry
 *  net.minecraftforge.fml.client.config.IConfigElement
 */
package com.matt.forgehax.mods.core;

import com.google.common.collect.Lists;
import com.matt.forgehax.gui.categories.ItemListCategory;
import com.matt.forgehax.gui.categories.PlayerListCategory;
import com.matt.forgehax.mods.BaseMod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ContainersMod
extends BaseMod {
    public static List<IConfigElement> getContainers() {
        ArrayList elements = Lists.newArrayList();
        elements.add(new DummyConfigElement.DummyCategoryElement("Blocks", "", ItemListCategory.class));
        elements.add(new DummyConfigElement.DummyCategoryElement("Players", "", PlayerListCategory.class));
        return elements;
    }

    public ContainersMod(String name, String desc) {
        super(name, desc);
        this.setHidden(true);
    }

    @Override
    public void onConfigBuildGui(List<IConfigElement> elements) {
        elements.add((IConfigElement)new DummyConfigElement.DummyCategoryElement(this.getModName(), "", GuiContainer.class));
    }

    public static class GuiContainer
    extends GuiConfigEntries.CategoryEntry {
        public GuiContainer(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
            super(owningScreen, owningEntryList, configElement);
        }

        protected GuiScreen buildChildScreen() {
            return new GuiConfig((GuiScreen)this.owningScreen, ContainersMod.getContainers(), this.owningScreen.modID, "general", this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, (this.owningScreen.titleLine2 == null ? "" : this.owningScreen.titleLine2) + " > " + this.name);
        }
    }

}

