/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.ActiveRenderInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.util.vector.Matrix
 *  org.lwjgl.util.vector.Matrix4f
 *  org.lwjgl.util.vector.Vector3f
 */
package com.matt.forgehax.util.math;

import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.math.Angle;
import java.nio.FloatBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class VectorUtils
extends ForgeHaxBase {
    static Matrix4f viewMatrix = new Matrix4f();
    static Matrix4f projectionMatrix = new Matrix4f();

    private static Vector3f Vec3TransformCoordinate(Vector3f vec, Matrix4f matrix) {
        Vector3f vOutput = new Vector3f(0.0f, 0.0f, 0.0f);
        vOutput.x = vec.x * matrix.m00 + vec.y * matrix.m10 + vec.z * matrix.m20 + matrix.m30;
        vOutput.y = vec.x * matrix.m01 + vec.y * matrix.m11 + vec.z * matrix.m21 + matrix.m31;
        vOutput.z = vec.x * matrix.m02 + vec.y * matrix.m12 + vec.z * matrix.m22 + matrix.m32;
        float w = 1.0f / (vec.x * matrix.m03 + vec.y * matrix.m13 + vec.z * matrix.m23 + matrix.m33);
        vOutput.x *= w;
        vOutput.y *= w;
        vOutput.z *= w;
        return vOutput;
    }

    public static ScreenPos toScreen(double x, double y, double z) {
        Entity view = MC.getRenderViewEntity();
        Vec3d viewNormal = view.getLook(MC.getRenderPartialTicks()).normalize();
        Vec3d camPos = view.getLookVec();
        Vec3d eyePos = view.getLookVec();
        float vecX = (float)(camPos.xCoord + eyePos.xCoord - x);
        float vecY = (float)(camPos.yCoord + eyePos.yCoord - y);
        float vecZ = (float)(camPos.zCoord + eyePos.zCoord - z);
        Vector3f pos = new Vector3f(vecX, vecY, vecZ);
        //viewMatrix.load(ActiveRenderInfo.field_178812_b.asReadOnlyBuffer());
        //projectionMatrix.load(ActiveRenderInfo.field_178813_c.asReadOnlyBuffer());
        pos = VectorUtils.Vec3TransformCoordinate(pos, viewMatrix);
        pos = VectorUtils.Vec3TransformCoordinate(pos, projectionMatrix);
        ScaledResolution scaledRes = new ScaledResolution(MC);
        pos.x = (float)((double)scaledRes.getScaledWidth() * ((double)pos.x + 1.0) / 2.0);
        pos.y = (float)((double)scaledRes.getScaledHeight() * (1.0 - ((double)pos.y + 1.0) / 2.0));
        boolean bVisible = false;
        double dot = viewNormal.xCoord * (double)vecX + viewNormal.yCoord * (double)vecY + viewNormal.zCoord * (double)vecZ;
        if (dot < 0.0) {
            bVisible = true;
        }
        if (pos.x < 0.0f || pos.y < 0.0f || pos.x > (float)scaledRes.getScaledWidth() || pos.y > (float)scaledRes.getScaledHeight()) {
            bVisible = false;
        }
        return new ScreenPos(pos.x, pos.y, bVisible);
    }

    public static ScreenPos toScreen(Vec3d vec3d) {
        return VectorUtils.toScreen(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
    }

    public static Angle vectorAngle(Vec3d vec3d) {
        double yaw;
        double pitch;
        if (vec3d.xCoord == 0.0 && vec3d.zCoord == 0.0) {
            yaw = 0.0;
            pitch = 90.0;
        } else {
            yaw = Math.toDegrees(Math.atan2(vec3d.zCoord, vec3d.xCoord)) - 90.0;
            double mag = Math.sqrt(vec3d.xCoord * vec3d.xCoord + vec3d.zCoord * vec3d.zCoord);
            pitch = Math.toDegrees(-1.0 * Math.atan2(vec3d.yCoord, mag));
        }
        return new Angle(pitch, yaw);
    }

    public static Vec3d multiplyBy(Vec3d vec1, Vec3d vec2) {
        return new Vec3d(vec1.xCoord * vec2.xCoord, vec1.yCoord * vec2.yCoord, vec1.zCoord * vec2.zCoord);
    }

    public static Vec3d copy(Vec3d toCopy) {
        return new Vec3d(toCopy.xCoord, toCopy.yCoord, toCopy.zCoord);
    }

    public static class ScreenPos {
        public final int x;
        public final int y;
        public final boolean isVisible;

        public ScreenPos(double x, double y, boolean isVisible) {
            this.x = (int)x;
            this.y = (int)y;
            this.isVisible = isVisible;
        }
    }

}

