/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraftforge.client.event.RenderLivingEvent
 *  net.minecraftforge.client.event.RenderLivingEvent$Post
 *  net.minecraftforge.client.event.RenderLivingEvent$Pre
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.entity.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ChamsMod
extends ToggleMod {
    public Property players;
    public Property hostileMobs;
    public Property friendlyMobs;

    public ChamsMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    public boolean shouldDraw(EntityLivingBase entity) {
        return !entity.equals((Object)ChamsMod.MC.thePlayer) && !entity.isDead && (this.hostileMobs.getBoolean() && EntityUtils.isHostileMob((Entity)entity) || this.players.getBoolean() && EntityUtils.isPlayer((Entity)entity) || this.friendlyMobs.getBoolean() && EntityUtils.isFriendlyMob((Entity)entity));
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.players = configuration.get(this.getModName(), "players", true, "Enables player chams");
        Property[] arrproperty = new Property[]{this.players, this.hostileMobs = configuration.get(this.getModName(), "hostile mobs", true, "Enables hostile mob chams"), this.friendlyMobs = configuration.get(this.getModName(), "friendly mobs", true, "Enables friendly mob chams")};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        GL11.glEnable((int)32823);
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1000000.0f);
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        GL11.glDisable((int)32823);
        GlStateManager.doPolygonOffset((float)1.0f, (float)1000000.0f);
        GlStateManager.disablePolygonOffset();
    }
}

