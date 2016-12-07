/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.config.ConfigCategory
 *  net.minecraftforge.common.config.ConfigElement
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.client.config.DummyConfigElement
 *  net.minecraftforge.fml.client.config.DummyConfigElement$DummyCategoryElement
 *  net.minecraftforge.fml.client.config.IConfigElement
 *  net.minecraftforge.fml.client.registry.ClientRegistry
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 */
package com.matt.forgehax.mods;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.mod.ModProperty;
import java.util.List;
import java.util.Map;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public abstract class BaseMod
extends ForgeHaxBase {
    public static final Map<String, Property> SETTINGS = Maps.newHashMap();
    public static final Map<String, KeyBinding> BINDINGS = Maps.newHashMap();
    private String modName;
    private String modDescription;
    private ConfigCategory modCategory = null;
    protected final List<ModProperty> properties = Lists.newArrayList();
    protected final List<KeyBinding> binds = Lists.newArrayList();
    private boolean isHiddenMod = false;
    private boolean registered = false;

    public BaseMod(String name, String desc) {
        this.modName = name;
        this.modDescription = desc;
    }

    public void initialize(Configuration configuration) {
        this.properties.clear();
        this.modCategory = configuration.getCategory(this.modName);
    }

    public final String getModName() {
        return this.modName;
    }

    public final String getModDescription() {
        return this.modDescription;
    }

    public final ConfigCategory getModCategory() {
        return this.modCategory;
    }

    public final boolean register() {
        if (!this.registered) {
            MinecraftForge.EVENT_BUS.register((Object)this);
            this.registered = true;
            return true;
        }
        return false;
    }

    public final boolean unregister() {
        if (this.registered) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            this.registered = false;
            return true;
        }
        return false;
    }

    public final boolean isRegisterd() {
        return this.registered;
    }

    protected final /* varargs */ void addSettings(Property ... props) {
        for (Property prop : props) {
            this.properties.add(new ModProperty(prop));
            SETTINGS.put(this.modName + "-" + prop.getName(), prop);
        }
    }

    protected final boolean hasSettingsChanged(List<Property> changed) {
        for (ModProperty prop : this.properties) {
            if (!prop.hasChanged()) continue;
            changed.add(prop.property);
            prop.update();
        }
        return changed.size() > 0;
    }

    public final List<ModProperty> getProperties() {
        return this.properties;
    }

    protected final KeyBinding addBind(String name, int keyCode) {
        KeyBinding bind = new KeyBinding(name, keyCode, "ForgeHax");
        ClientRegistry.registerKeyBinding((KeyBinding)bind);
        BINDINGS.put(name, bind);
        this.binds.add(bind);
        return bind;
    }

    public final List<KeyBinding> getKeyBinds() {
        return this.binds;
    }

    protected final void setHidden(boolean b) {
        this.isHiddenMod = b;
    }

    public final boolean isHidden() {
        return this.isHiddenMod;
    }

    public boolean isEnabled() {
        return true;
    }

    public void toggle() {
    }

    public void update() {
    }

    public void onConfigBuildGui(List<IConfigElement> elements) {
        elements.add((IConfigElement)new DummyConfigElement.DummyCategoryElement(this.getModName(), "", new ConfigElement(this.getModCategory()).getChildElements()));
    }

    public void onConfigUpdated(List<Property> changed) {
    }

    public void loadConfig(Configuration configuration) {
    }

    public void onEnabled() {
    }

    public void onDisabled() {
    }

    public void onBindPressed(KeyBinding bind) {
    }

    public void onBindKeyDown(KeyBinding bind) {
    }
}

