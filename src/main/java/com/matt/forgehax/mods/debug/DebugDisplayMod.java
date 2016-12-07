/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods.debug;

import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.draw.SurfaceUtils;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DebugDisplayMod
extends ToggleMod {
    public DebugDisplayMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
        this.setHidden(true);
    }

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Text event) {
        int posX = event.getResolution().getScaledWidth();
        int posY = 1;
        for (Map.Entry<String, ForgeHaxHooks.DebugData> entry : ForgeHaxHooks.responding.entrySet()) {
            boolean isResponding = entry.getValue().isResponding;
            Object[] arrobject = new Object[3];
            arrobject[0] = entry.getKey();
            arrobject[1] = isResponding ? "true" : "false";
            arrobject[2] = entry.getValue().hasResponded ? "true" : "false";
            String text = String.format("%s[isResponding:%s, hasResponded:%s]", arrobject);
            int color = isResponding ? Utils.toRGBA(0, 255, 0, 255) : Utils.toRGBA(255, 0, 0, 255);
            SurfaceUtils.drawTextShadow(text, posX - SurfaceUtils.getTextWidth(text) - 1, posY, color);
            posY += SurfaceUtils.getTextHeight() + 1;
        }
    }
}

