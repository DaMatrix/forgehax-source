/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.chunk.SetVisibility
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.events.ComputeVisibilityEvent;
import com.matt.forgehax.mods.ToggleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoCaveCulling
extends ToggleMod {
    public NoCaveCulling(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    public void reloadRenderers() {
        if (NoCaveCulling.MC.renderGlobal != null) {
            NoCaveCulling.MC.renderGlobal.loadRenderers();
        }
    }

    @SubscribeEvent
    public void onComputeVisibility(ComputeVisibilityEvent event) {
        event.getSetVisibility().setAllVisible(true);
    }
}

