/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.draw.RenderUtils;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import com.matt.forgehax.util.math.Angle;
import com.matt.forgehax.util.math.ProjectileUtils;
import com.matt.forgehax.util.math.VectorUtils;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ProjectilesMod
extends ToggleMod {
    private static final int TIME = 10;
    private static final double DETAIL = 0.2;

    public ProjectilesMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        double pitch;
        double yaw;
        EntityPlayerSP localPlayer = ProjectilesMod.MC.thePlayer;
        Angle viewAngles = LocalPlayerUtils.getViewAngles();
        Vec3d selfPos = ProjectileUtils.getFiringPos((Entity)localPlayer);
        ItemStack heldItem = localPlayer.getHeldItemMainhand();
        if (heldItem == null || !ProjectileUtils.isThrowable(heldItem)) {
            return;
        }
        RayTraceResult trace = localPlayer.rayTrace(9999.0, 0.0f);
        if (trace == null) {
            return;
        }
        Property autoProjectile = (Property)SETTINGS.get("autoprojectile-enabled");
        if (LocalPlayerUtils.isProjectileTargetAcquired()) {
            pitch = LocalPlayerUtils.getFakeViewAngles().getPitch();
            yaw = LocalPlayerUtils.getFakeViewAngles().getYaw();
        } else if (autoProjectile != null && autoProjectile.getBoolean()) {
            pitch = ProjectileUtils.getBestPitch(heldItem, trace.hitVec);
            yaw = viewAngles.getYaw();
        } else {
            pitch = viewAngles.getPitch();
            yaw = viewAngles.getYaw();
        }
        double force = ProjectileUtils.getForce(heldItem);
        Angle initAngle = new Angle(- pitch, yaw + 90.0, 0.0);
        double fixX = Math.cos(initAngle.getYaw(true) - 1.5707963267948966) * 0.16;
        double fixY = 0.10000000149011612;
        double fixZ = Math.sin(initAngle.getYaw(true) - 1.5707963267948966) * 0.16;
        Vec3d initPos = new Vec3d(- fixX, (double)localPlayer.getEyeHeight() - fixY, - fixZ);
        Vec3d velocity = initAngle.getCartesianCoords().normalize().scale(force);
        Vec3d acceleration = ProjectileUtils.getGravity(heldItem);
        Vec3d airResistance = ProjectileUtils.getAirResistance(heldItem);
        Vec3d startPos = VectorUtils.copy(initPos);
        Vec3d endPos = VectorUtils.copy(startPos);
        for (int i = 0; i < 100; ++i) {
            startPos = startPos.add(velocity);
            velocity = VectorUtils.multiplyBy(velocity, airResistance);
            velocity = velocity.add(acceleration);
            RenderUtils.drawLine(endPos, startPos, Utils.Colors.WHITE, true, 1.5f);
            RayTraceResult tr = ProjectilesMod.getWorld().rayTraceBlocks(selfPos.add(startPos), selfPos.add(endPos), false, true, false);
            if (tr != null && !ProjectilesMod.MC.theWorld.getBlockState(tr.getBlockPos()).getBlock().isPassable((IBlockAccess)ProjectilesMod.getWorld(), tr.getBlockPos())) break;
            endPos = startPos;
        }
    }
}

