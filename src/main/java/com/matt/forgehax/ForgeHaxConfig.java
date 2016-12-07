/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.fml.client.event.ConfigChangedEvent
 *  net.minecraftforge.fml.client.event.ConfigChangedEvent$OnConfigChangedEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.mods.BaseMod;
import java.io.File;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeHaxConfig
extends ForgeHaxBase {
    public Configuration config;

    public ForgeHaxConfig(File file) {
        this.config = new Configuration(file);
        this.config.load();
        for (Map.Entry<String, BaseMod> entry : ForgeHaxConfig.MOD.mods.entrySet()) {
            entry.getValue().initialize(this.config);
        }
        this.save();
    }

    public void save() {
        if (this.config.hasChanged()) {
            this.config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("forgehax")) {
            this.save();
            for (Map.Entry<String, BaseMod> entry : ForgeHaxConfig.MOD.mods.entrySet()) {
                entry.getValue().update();
            }
        }
    }
}

