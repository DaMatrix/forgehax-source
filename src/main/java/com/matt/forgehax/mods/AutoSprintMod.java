/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.key.Bindings;
import com.matt.forgehax.util.key.KeyBindingHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoSprintMod
extends ToggleMod {
    private boolean isBound = false;

    public AutoSprintMod(String categoryName, boolean defaultValue, String description, int key) {
        super(categoryName, defaultValue, description, key);
    }

    private void startSprinting() {
        if (!this.isBound) {
            Bindings.sprint.bind();
            this.isBound = true;
        }
        if (!Bindings.sprint.getBinding().isKeyDown()) {
            Bindings.sprint.setPressed(true);
        }
    }

    private void stopSprinting() {
        if (this.isBound) {
            Bindings.sprint.setPressed(false);
            Bindings.sprint.unbind();
            this.isBound = false;
        }
    }

    @Override
    public void onDisabled() {
        this.stopSprinting();
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
        if (event.getEntityLiving().moveForward > 0.0f && !event.getEntityLiving().isCollidedHorizontally && !event.getEntityLiving().isSneaking()) {
            this.startSprinting();
        }
    }
}

