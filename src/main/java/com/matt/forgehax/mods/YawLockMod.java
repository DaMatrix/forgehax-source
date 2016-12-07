/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class YawLockMod
extends ToggleMod {
    public static YawLockMod INSTANCE;
    public Property doOnce;
    public Property autoAngle;
    public Property customAngle;

    public static YawLockMod instance() {
        return INSTANCE;
    }

    public YawLockMod(String categoryName, boolean defaultValue, String description, int key) {
        super(categoryName, defaultValue, description, key);
        INSTANCE = this;
    }

    public double getYawDirection() {
        return (float)Math.round((LocalPlayerUtils.getViewAngles().getYaw() + 1.0) / 45.0) * 45.0f;
    }

    @Override
    public void loadConfig(Configuration configuration) {
        super.loadConfig(configuration);
        this.doOnce = configuration.get(this.getModName(), "once", false, "Will only fire update once");
        Property[] arrproperty = new Property[]{this.doOnce, this.autoAngle = configuration.get(this.getModName(), "auto", true, "Automatically finds angle to snap to based on the direction you're facing"), this.customAngle = configuration.get(this.getModName(), "angle", 0.0, "Custom angle to snap to", -180.0, 180.0)};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
        double yaw = this.getYawDirection();
        if (!this.autoAngle.getBoolean()) {
            yaw = this.customAngle.getDouble();
        }
        LocalPlayerUtils.setViewAngles(event.getEntityLiving().rotationPitch, yaw);
        if (this.isEnabled() && this.doOnce.getBoolean()) {
            this.toggle();
        }
    }
}

