/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.MobEffects
 *  net.minecraft.potion.Potion
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.event.entity.living.LivingEvent
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.mods.ToggleMod;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiEffectsMod
extends ToggleMod {
    public Property noParticles;

    public AntiEffectsMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.noParticles = configuration.get(this.getModName(), "anti_particles", true, "Stops the particle effect from rendering on other entities");
        Property[] arrproperty = new Property[]{this.noParticles};
        this.addSettings(arrproperty);
    }

    @Override
    public void onDisabled() {
        if (AntiEffectsMod.MC.theWorld != null) {
            for (Entity entity : AntiEffectsMod.MC.theWorld.loadedEntityList) {
                if (!(entity instanceof EntityLivingBase)) continue;
                entity.setInvisible(true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.equals((Object)AntiEffectsMod.MC.thePlayer)) {
            living.setInvisible(false);
            living.removePotionEffect(MobEffects.NAUSEA);
            living.removePotionEffect(MobEffects.INVISIBILITY);
            living.removePotionEffect(MobEffects.BLINDNESS);
            //living.resetPotionEffectMetadata();
        } else if (this.noParticles.getBoolean()) {
            living.setInvisible(false);
            //living.resetPotionEffectMetadata();
        }
    }
}

