/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.mods;

import com.google.common.collect.Lists;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxConfig;
import com.matt.forgehax.mods.BaseMod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;

public abstract class ToggleMod
extends BaseMod {
    private Property enabled = null;
    private KeyBinding toggleBind = null;
    private boolean defaultValue;

    public ToggleMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, description);
        this.defaultValue = defaultValue;
        if (key != -1) {
            this.toggleBind = this.addBind(modName, key);
        }
    }

    public ToggleMod(String modName, boolean defaultValue, String description) {
        this(modName, defaultValue, description, -1);
    }

    @Override
    public final void initialize(Configuration configuration) {
        super.initialize(configuration);
        this.enabled = configuration.get(this.getModName(), "enabled", this.defaultValue, this.getModDescription());
        Property[] arrproperty = new Property[]{this.enabled};
        this.addSettings(arrproperty);
        this.loadConfig(configuration);
    }

    @Override
    public final void toggle() {
        this.enabled.set(!this.enabled.getBoolean());
        this.update();
        MOD.getConfig().save();
    }

    @Override
    public final boolean isEnabled() {
        return this.enabled.getBoolean();
    }

    @Override
    public final void update() {
        ArrayList changed = Lists.newArrayList();
        if (this.hasSettingsChanged(changed)) {
            if (this.enabled.getBoolean()) {
                if (this.register()) {
                    this.onEnabled();
                    MOD.getLog().info(String.format("%s enabled", this.getModName()));
                }
            } else if (this.unregister()) {
                this.onDisabled();
                MOD.getLog().info(String.format("%s disabled", this.getModName()));
            }
            this.onConfigUpdated(changed);
        }
    }

    @Override
    public void onBindPressed(KeyBinding bind) {
        if (bind.equals((Object)this.toggleBind)) {
            this.toggle();
        }
    }
}

