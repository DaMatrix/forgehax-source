/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.mods.BaseMod;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.LagCompensator;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.draw.SurfaceUtils;
import java.util.Collection;
import java.util.Map;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ActiveModListMod
extends ToggleMod {
    public ActiveModListMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
        this.setHidden(true);
    }

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Text event) {
        int posX = 1;
        int posY = 1;
        SurfaceUtils.drawTextShadow(String.format("Tick-rate: %d", LagCompensator.getInstance().getTickRate()), posX, posY, Utils.Colors.WHITE);
        posY += SurfaceUtils.getTextHeight() + 1;
        for (BaseMod mod : ActiveModListMod.MOD.mods.values()) {
            if (!mod.isEnabled() || mod.isHidden()) continue;
            SurfaceUtils.drawTextShadow(">" + mod.getModName(), posX, posY, Utils.Colors.WHITE);
            posY += SurfaceUtils.getTextHeight() + 1;
        }
    }
}

