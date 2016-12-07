/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.VertexBuffer
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package com.matt.forgehax.util.draw;

import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.entity.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderUtils
extends ForgeHaxBase {
    public static Vec3d getRenderPos() {
        return new Vec3d(RenderUtils.MC.thePlayer.lastTickPosX + (RenderUtils.MC.thePlayer.posX - RenderUtils.MC.thePlayer.lastTickPosX) * (double)MC.getRenderPartialTicks(), RenderUtils.MC.thePlayer.lastTickPosY + (RenderUtils.MC.thePlayer.posY - RenderUtils.MC.thePlayer.lastTickPosY) * (double)MC.getRenderPartialTicks(), RenderUtils.MC.thePlayer.lastTickPosY + (RenderUtils.MC.thePlayer.posY - RenderUtils.MC.thePlayer.lastTickPosY) * (double)MC.getRenderPartialTicks());
    }

    public static void drawLine(Vec3d startPos, Vec3d endPos, int color, boolean smooth, float width) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        Vec3d endVecPos = endPos.subtract(startPos);
        float r = (float)(color >> 16 & 255) / 255.0f;
        float g = (float)(color >> 8 & 255) / 255.0f;
        float b = (float)(color & 255) / 255.0f;
        float a = (float)(color >> 24 & 255) / 255.0f;
        if (smooth) {
            GL11.glEnable((int)2848);
        }
        GL11.glLineWidth((float)width);
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)startPos.xCoord, (double)startPos.yCoord, (double)startPos.zCoord);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.shadeModel((int)7425);
        vertexBuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        vertexBuffer.pos(0.0, 0.0, 0.0).color(r, g, b, a).endVertex();
        vertexBuffer.pos(endVecPos.xCoord, endVecPos.yCoord, endVecPos.zCoord).color(r, g, b, a).endVertex();
        tessellator.draw();
        if (smooth) {
            GL11.glDisable((int)2848);
        }
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void drawBox(Vec3d startPos, Vec3d endPos, int color, float width, boolean ignoreZ) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        Vec3d renderPos = EntityUtils.getInterpolatedPos((Entity)RenderUtils.getLocalPlayer(), MC.getRenderPartialTicks());
        Vec3d min = startPos.subtract(renderPos);
        Vec3d max = endPos.subtract(renderPos);
        double minX = min.xCoord;
        double minY = min.yCoord;
        double minZ = min.zCoord;
        double maxX = max.xCoord;
        double maxY = max.yCoord;
        double maxZ = max.zCoord;
        float r = (float)(color >> 16 & 255) / 255.0f;
        float g = (float)(color >> 8 & 255) / 255.0f;
        float b = (float)(color & 255) / 255.0f;
        float a = (float)(color >> 24 & 255) / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.shadeModel((int)7425);
        GlStateManager.glLineWidth((float)width);
        if (ignoreZ) {
            GlStateManager.disableDepth();
        }
        GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        buffer.begin(3, DefaultVertexFormats.POSITION);
        buffer.pos(minX, minY, minZ).endVertex();
        buffer.pos(maxX, minY, minZ).endVertex();
        buffer.pos(maxX, minY, maxZ).endVertex();
        buffer.pos(minX, minY, maxZ).endVertex();
        buffer.pos(minX, minY, minZ).endVertex();
        tessellator.draw();
        buffer.begin(3, DefaultVertexFormats.POSITION);
        buffer.pos(minX, maxY, minZ).endVertex();
        buffer.pos(maxX, maxY, minZ).endVertex();
        buffer.pos(maxX, maxY, maxZ).endVertex();
        buffer.pos(minX, maxY, maxZ).endVertex();
        buffer.pos(minX, maxY, minZ).endVertex();
        tessellator.draw();
        buffer.begin(1, DefaultVertexFormats.POSITION);
        buffer.pos(minX, minY, minZ).endVertex();
        buffer.pos(minX, maxY, minZ).endVertex();
        buffer.pos(maxX, minY, minZ).endVertex();
        buffer.pos(maxX, maxY, minZ).endVertex();
        buffer.pos(maxX, minY, maxZ).endVertex();
        buffer.pos(maxX, maxY, maxZ).endVertex();
        buffer.pos(minX, minY, maxZ).endVertex();
        buffer.pos(minX, maxY, maxZ).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void drawBox(BlockPos startPos, BlockPos endPos, int color, float width, boolean ignoreZ) {
        RenderUtils.drawBox(new Vec3d((double)startPos.getX(), (double)startPos.getY(), (double)startPos.getZ()), new Vec3d((double)endPos.getX(), (double)endPos.getY(), (double)endPos.getZ()), color, width, ignoreZ);
    }
}

