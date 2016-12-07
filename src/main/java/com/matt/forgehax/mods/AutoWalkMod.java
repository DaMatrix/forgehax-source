/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.key.Bindings;
import com.matt.forgehax.util.key.KeyBindingHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoWalkMod
extends ToggleMod {
    public Property stopAtUnloadedChunks;
    private boolean isBound = false;

    public AutoWalkMod(String categoryName, boolean defaultValue, String description, int key) {
        super(categoryName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.stopAtUnloadedChunks = configuration.get(this.getModName(), "stop_at_unloaded_chunks", true, "Stop moving at unloaded chunks");
        Property[] arrproperty = new Property[]{this.stopAtUnloadedChunks};
        this.addSettings(arrproperty);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        if (this.isBound) {
            Bindings.forward.setPressed(false);
            Bindings.forward.unbind();
            this.isBound = false;
        }
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
        if (!this.isBound) {
            Bindings.forward.bind();
            this.isBound = true;
        }
        if (!Bindings.forward.getBinding().isKeyDown()) {
            Bindings.forward.setPressed(true);
        }
        if (this.stopAtUnloadedChunks.getBoolean() && !AutoWalkMod.getWorld().getChunkFromBlockCoords(AutoWalkMod.getLocalPlayer().getPosition()).isLoaded()) {
            Bindings.forward.setPressed(false);
        }
    }
}

