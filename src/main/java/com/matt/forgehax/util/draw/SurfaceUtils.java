/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.VertexBuffer
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package com.matt.forgehax.util.draw;

import com.matt.forgehax.ForgeHaxBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class SurfaceUtils
extends ForgeHaxBase {
    public static void drawLine(int startX, int startY, int endX, int endY, int color) {
        float r = (float)(color >> 16 & 255) / 255.0f;
        float g = (float)(color >> 8 & 255) / 255.0f;
        float b = (float)(color & 255) / 255.0f;
        float a = (float)(color >> 24 & 255) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos((double)startX, (double)startY, 0.0).endVertex();
        vertexbuffer.pos((double)endX, (double)endY, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(int x, int y, int w, int h, int color) {
        GL11.glLineWidth((float)1.0f);
        Gui.drawRect((int)x, (int)y, (int)(x + w), (int)(y + h), (int)color);
    }

    public static void drawOutlinedRect(int x, int y, int w, int h, int color, float width) {
        float r = (float)(color >> 16 & 255) / 255.0f;
        float g = (float)(color >> 8 & 255) / 255.0f;
        float b = (float)(color & 255) / 255.0f;
        float a = (float)(color >> 24 & 255) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        GL11.glLineWidth((float)width);
        vertexbuffer.begin(2, DefaultVertexFormats.POSITION);
        vertexbuffer.pos((double)x, (double)y, 0.0).endVertex();
        vertexbuffer.pos((double)x, (double)y + (double)h, 0.0).endVertex();
        vertexbuffer.pos((double)x + (double)w, (double)y + (double)h, 0.0).endVertex();
        vertexbuffer.pos((double)x + (double)w, (double)y, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlinedRect(int x, int y, int w, int h, int color) {
        SurfaceUtils.drawOutlinedRect(x, y, w, h, color, 1.0f);
    }

    public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel) {
        float f = 0.00390625f;
        float f1 = 0.00390625f;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625f), (double)((float)(textureY + height) * 0.00390625f)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625f), (double)((float)(textureY + height) * 0.00390625f)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625f), (double)((float)(textureY + 0) * 0.00390625f)).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625f), (double)((float)(textureY + 0) * 0.00390625f)).endVertex();
        tessellator.draw();
    }

    public static void drawText(String msg, int x, int y, int color) {
        SurfaceUtils.MC.fontRendererObj.drawString(msg, x, y, color);
    }

    public static void drawTextShadow(String msg, int x, int y, int color) {
        SurfaceUtils.MC.fontRendererObj.drawStringWithShadow(msg, (float)x, (float)y, color);
    }

    public static void drawText(String msg, int x, int y, int color, double scale, boolean shadow) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.scale((double)scale, (double)scale, (double)scale);
        SurfaceUtils.MC.fontRendererObj.drawString(msg, (float)((int)((double)x * (1.0 / scale))), (float)((int)((double)y * (1.0 / scale))), color, shadow);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void drawText(String msg, int x, int y, int color, double scale) {
        SurfaceUtils.drawText(msg, x, y, color, scale, false);
    }

    public static void drawTextShadow(String msg, int x, int y, int color, double scale) {
        SurfaceUtils.drawText(msg, x, y, color, scale, true);
    }

    public static int getTextWidth(String text, double scale) {
        return (int)((double)SurfaceUtils.MC.fontRendererObj.getStringWidth(text) * scale);
    }

    public static int getTextWidth(String text) {
        return SurfaceUtils.getTextWidth(text, 1.0);
    }

    public static int getTextHeight() {
        return SurfaceUtils.MC.fontRendererObj.FONT_HEIGHT;
    }

    public static int getTextHeight(double scale) {
        return (int)((double)SurfaceUtils.MC.fontRendererObj.FONT_HEIGHT * scale);
    }

    public static void drawItem(ItemStack item, int x, int y) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        SurfaceUtils.MC.getRenderItem().zLevel = 100.0f;
        MC.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
        SurfaceUtils.MC.getRenderItem().zLevel = 0.0f;
        GlStateManager.popMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawItemWithOverlay(ItemStack item, int x, int y) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        SurfaceUtils.MC.getRenderItem().zLevel = 100.0f;
        MC.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
        MC.getRenderItem().renderItemOverlays(SurfaceUtils.MC.fontRendererObj, item, x, y);
        SurfaceUtils.MC.getRenderItem().zLevel = 0.0f;
        GlStateManager.popMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawPotionEffect(PotionEffect potion, int x, int y) {
        int index = potion.getPotion().getStatusIconIndex();
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        MC.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
        SurfaceUtils.drawTexturedRect(x, y, index % 8 * 18, 198 + index / 8 * 18, 18, 18, 100);
        potion.getPotion().renderHUDEffect(x, y, potion, MC, 255.0f);
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
    }

    public static void drawHead(ResourceLocation skinResource, int x, int y, float scale) {
        GlStateManager.pushMatrix();
        SurfaceUtils.MC.renderEngine.bindTexture(skinResource);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.scale((float)scale, (float)scale, (float)scale);
        Gui.drawScaledCustomSizeModalRect((int)((int)((float)x * (1.0f / scale))), (int)((int)((float)y * (1.0f / scale))), (float)8.0f, (float)8.0f, (int)8, (int)8, (int)12, (int)12, (float)64.0f, (float)64.0f);
        Gui.drawScaledCustomSizeModalRect((int)((int)((float)x * (1.0f / scale))), (int)((int)((float)y * (1.0f / scale))), (float)40.0f, (float)8.0f, (int)8, (int)8, (int)12, (int)12, (float)64.0f, (float)64.0f);
        GlStateManager.popMatrix();
    }

    public static int getHeadWidth(float scale) {
        return (int)(scale * 12.0f);
    }

    public static int getHeadWidth() {
        return SurfaceUtils.getHeadWidth(1.0f);
    }

    public static int getHeadHeight(float scale) {
        return (int)(scale * 12.0f);
    }

    public static int getHeadHeight() {
        return SurfaceUtils.getHeadWidth(1.0f);
    }
}

