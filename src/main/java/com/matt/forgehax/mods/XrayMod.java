/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.chunk.SetVisibility
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.texture.TextureMap
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.common.ForgeModContainer
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.asm.events.ComputeVisibilityEvent;
import com.matt.forgehax.asm.events.RenderBlockInLayerEvent;
import com.matt.forgehax.asm.events.RenderBlockLayerEvent;
import com.matt.forgehax.mods.ToggleMod;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class XrayMod
extends ToggleMod {
    public Property opacity;
    private boolean previousForgeLightPipelineEnabled = false;
    private boolean isInternalCall = false;

    public XrayMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    public void reloadRenderers() {
        if (XrayMod.MC.renderGlobal != null) {
            XrayMod.MC.renderGlobal.loadRenderers();
        }
    }

    @Override
    public void onEnabled() {
        this.previousForgeLightPipelineEnabled = ForgeModContainer.forgeLightPipelineEnabled;
        ForgeModContainer.forgeLightPipelineEnabled = false;
        ForgeHaxHooks.SHOULD_UPDATE_ALPHA = true;
        this.reloadRenderers();
    }

    @Override
    public void onDisabled() {
        ForgeModContainer.forgeLightPipelineEnabled = this.previousForgeLightPipelineEnabled;
        ForgeHaxHooks.SHOULD_UPDATE_ALPHA = false;
        this.reloadRenderers();
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.opacity = configuration.get(this.getModName(), "opacity", 150, "How transparent the blocks will be", 0, 255);
        Property[] arrproperty = new Property[]{this.opacity};
        this.addSettings(arrproperty);
        ForgeHaxHooks.COLOR_MULTIPLIER_ALPHA = (float)(this.opacity.getDouble() / 255.0);
    }

    @Override
    public void onConfigUpdated(List<Property> changed) {
        if (changed.contains((Object)this.opacity)) {
            ForgeHaxHooks.COLOR_MULTIPLIER_ALPHA = (float)(this.opacity.getDouble() / 255.0);
            this.reloadRenderers();
        }
    }

    @SubscribeEvent
    public void onPreRenderBlockLayer(RenderBlockLayerEvent.Pre event) {
        if (!this.isInternalCall) {
            if (!event.getRenderLayer().equals((Object)BlockRenderLayer.TRANSLUCENT)) {
                event.setCanceled(true);
            } else if (event.getRenderLayer().equals((Object)BlockRenderLayer.TRANSLUCENT)) {
                this.isInternalCall = true;
                Entity renderEntity = MC.getRenderViewEntity();
                GlStateManager.disableAlpha();
                XrayMod.MC.renderGlobal.renderBlockLayer(BlockRenderLayer.SOLID, event.getPartialTicks(), 0, renderEntity);
                GlStateManager.enableAlpha();
                XrayMod.MC.renderGlobal.renderBlockLayer(BlockRenderLayer.CUTOUT_MIPPED, event.getPartialTicks(), 0, renderEntity);
                MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                XrayMod.MC.renderGlobal.renderBlockLayer(BlockRenderLayer.CUTOUT, event.getPartialTicks(), 0, renderEntity);
                MC.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
                GlStateManager.disableAlpha();
                this.isInternalCall = false;
            }
        }
    }

    @SubscribeEvent
    public void onPostRenderBlockLayer(RenderBlockLayerEvent.Post event) {
    }

    @SubscribeEvent
    public void onRenderBlockInLayer(RenderBlockInLayerEvent event) {
        if (event.getLayer().equals((Object)BlockRenderLayer.TRANSLUCENT) && !event.getLayer().equals((Object)event.getBlock().getBlockLayer())) {
            event.setReturnValue(true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onComputeVisibility(ComputeVisibilityEvent event) {
        if (!((Property)SETTINGS.get("nocaveculling-enabled")).getBoolean()) {
            event.getSetVisibility().setAllVisible(true);
        }
    }
}

