/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.SoundEvent
 *  net.minecraftforge.client.event.RenderLivingEvent
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.event.entity.PlaySoundAtEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.entity.EntityUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiBatsMod
extends ToggleMod {
    public AntiBatsMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onEnabled() {
        EntityUtils.isBatsDisabled = true;
    }

    @Override
    public void onDisabled() {
        EntityUtils.isBatsDisabled = false;
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<?> event) {
        if (event.getEntity() instanceof EntityBat) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaySound(PlaySoundAtEntityEvent event) {
        if (event.getSound().equals((Object)SoundEvents.ENTITY_BAT_AMBIENT) || event.getSound().equals((Object)SoundEvents.ENTITY_BAT_DEATH) || event.getSound().equals((Object)SoundEvents.ENTITY_BAT_HURT) || event.getSound().equals((Object)SoundEvents.ENTITY_BAT_LOOP) || event.getSound().equals((Object)SoundEvents.ENTITY_BAT_TAKEOFF)) {
            event.setCanceled(true);
        }
    }
}

