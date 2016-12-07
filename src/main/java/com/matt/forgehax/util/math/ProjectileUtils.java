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
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 */
package com.matt.forgehax.util.math;

import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.entity.EntityUtils;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import com.matt.forgehax.util.math.Angle;
import com.matt.forgehax.util.math.VectorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;

public class ProjectileUtils
extends ForgeHaxBase {
    private static final int SIMULATION_ITERATIONS = 150;
    private static final int BESTPOS_ITERATIONS = 10;
    public static final double PROJECTILE_SHOOTPOS_OFFSET = 0.10000000149011612;

    public static Vec3d getFiringPos(Entity entity) {
        return entity.getPositionVector();
    }

    public static boolean isThrowable(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        switch (Item.getIdFromItem((Item)itemStack.getItem())) {
            case 261: 
            case 332: 
            case 344: 
            case 346: 
            case 368: {
                return true;
            }
        }
        return false;
    }

    public static boolean isBow(ItemStack itemStack) {
        return itemStack != null && Item.getIdFromItem((Item)itemStack.getItem()) == 261;
    }

    public static boolean isAttackableThrowable(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        switch (Item.getIdFromItem((Item)itemStack.getItem())) {
            case 261: 
            case 332: 
            case 344: 
            case 346: {
                return true;
            }
        }
        return false;
    }

    public static double getForce(ItemStack itemStack) {
        double force = 0.0;
        switch (Item.getIdFromItem((Item)itemStack.getItem())) {
            case 261: {
                int duration = itemStack.getMaxItemUseDuration() - ProjectileUtils.MC.thePlayer.getItemInUseCount();
                force = (double)duration / 20.0;
                if (force > 1.0) {
                    force = 1.0;
                }
                force *= 3.0;
                break;
            }
            case 332: 
            case 344: 
            case 346: 
            case 368: {
                force = 1.5;
            }
        }
        return force;
    }

    public static double getMinForce(ItemStack itemStack) {
        switch (Item.getIdFromItem((Item)itemStack.getItem())) {
            case 261: {
                return 0.15;
            }
            case 332: 
            case 344: 
            case 346: 
            case 368: {
                return 1.5;
            }
        }
        return 0.0;
    }

    public static double getMaxForce(ItemStack itemStack) {
        switch (Item.getIdFromItem((Item)itemStack.getItem())) {
            case 261: {
                return 3.0;
            }
            case 332: 
            case 344: 
            case 346: 
            case 368: {
                return 1.5;
            }
        }
        return 0.0;
    }

    public static Vec3d getGravity(ItemStack itemStack) {
        Vec3d gravity = new Vec3d(0.0, 0.0, 0.0);
        switch (Item.getIdFromItem((Item)itemStack.getItem())) {
            case 261: {
                gravity = new Vec3d(0.0, -0.05, 0.0);
                break;
            }
            case 332: 
            case 344: 
            case 368: {
                gravity = new Vec3d(0.0, -0.03, 0.0);
                break;
            }
            case 346: {
                gravity = new Vec3d(0.0, -0.03999999910593033, 0.0);
            }
        }
        return gravity;
    }

    public static Vec3d getAirResistance(ItemStack itemStack) {
        Vec3d ar = new Vec3d(0.0, 0.0, 0.0);
        switch (Item.getIdFromItem((Item)itemStack.getItem())) {
            case 261: 
            case 332: 
            case 344: 
            case 368: {
                ar = new Vec3d(0.99, 0.99, 0.99);
                break;
            }
            case 346: {
                ar = new Vec3d(0.92, 0.92, 0.92);
            }
        }
        return ar;
    }

    public static Vec3d getImpactPos(ItemStack itemStack, Vec3d initPos, Vec3d hitPos, Angle angle) {
        RayTraceResult trace;
        double force = ProjectileUtils.getForce(itemStack);
        Angle initAngle = new Angle(- angle.getPitch(), angle.getYaw() + 90.0, 0.0);
        double fixX = Math.cos(initAngle.getYaw(true) - 1.5707963267948966) * 0.16;
        double fixY = 0.10000000149011612;
        double fixZ = Math.sin(initAngle.getYaw(true) - 1.5707963267948966) * 0.16;
        initPos = initPos.subtract(fixX, fixY, fixZ);
        Vec3d velocity = initAngle.getCartesianCoords().normalize().scale(force);
        Vec3d acceleration = ProjectileUtils.getGravity(itemStack);
        Vec3d airResistance = ProjectileUtils.getAirResistance(itemStack);
        double bestDistance = -1.0;
        Vec3d startPos = VectorUtils.copy(initPos);
        Vec3d endPos = VectorUtils.copy(startPos);
        for (int i = 1; i < 150; ++i) {
            startPos = startPos.add(velocity);
            velocity = VectorUtils.multiplyBy(velocity, airResistance);
            velocity = velocity.add(acceleration);
            double x = startPos.xCoord - hitPos.xCoord;
            double z = startPos.zCoord - hitPos.zCoord;
            double distance = x * x + z * z;
            if (distance != -1.0 && distance >= bestDistance) break;
            bestDistance = distance;
            endPos = VectorUtils.copy(startPos);
        }
        if ((trace = ProjectileUtils.MC.theWorld.rayTraceBlocks(startPos, endPos)) != null && trace.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK)) {
            return trace.hitVec;
        }
        return initPos;
    }

    public static Angle getShootAngle(ItemStack itemStack, Vec3d startPos, Vec3d targetPos, double force) {
        Angle angle = new Angle();
        Vec3d initPos = startPos.subtract(0.0, 0.10000000149011612, 0.0);
        initPos = initPos.subtract(targetPos);
        angle.setYaw(Utils.getLookAtAngles(targetPos).getYaw(false));
        Vec3d acceleration = ProjectileUtils.getGravity(itemStack);
        Vec3d airResistance = ProjectileUtils.getAirResistance(itemStack);
        double x = Math.sqrt(initPos.xCoord * initPos.xCoord + initPos.zCoord * initPos.zCoord);
        double g = acceleration.yCoord;
        double root = Math.pow(force, 4.0) - g * (g * Math.pow(x, 2.0) + 2.0 * initPos.yCoord * Math.pow(force *= airResistance.yCoord, 2.0));
        if (root < 0.0) {
            return null;
        }
        double A = (Math.pow(force, 2.0) + Math.sqrt(root)) / (g * x);
        double B = (Math.pow(force, 2.0) - Math.sqrt(root)) / (g * x);
        angle.setPitch(Math.toDegrees(Math.atan(Math.max(A, B))));
        return angle.normalize();
    }

    public static Angle getShootAngle(ItemStack itemStack, Vec3d startPos, Vec3d targetPos) {
        return ProjectileUtils.getShootAngle(itemStack, startPos, targetPos, ProjectileUtils.getForce(itemStack));
    }

    public static double getBestPitch(ItemStack itemStack, Vec3d hitPos) {
        EntityPlayerSP localPlayer = ProjectileUtils.MC.thePlayer;
        Vec3d initPos = EntityUtils.getEyePos((Entity)localPlayer);
        Angle angle = new Angle();
        angle.setYaw(LocalPlayerUtils.getViewAngles().getYaw());
        double minAngle = localPlayer.rotationPitch;
        double maxAngle = minAngle - 45.0;
        double bestOffset = -1.0;
        double bestDistance = -1.0;
        for (int i = 0; i < 10; ++i) {
            double offset = Utils.scale(0.5, 0.0, 1.0, minAngle, maxAngle);
            angle.setPitch(offset);
            Vec3d pos = ProjectileUtils.getImpactPos(itemStack, initPos, hitPos, angle);
            double distance = pos.distanceTo(hitPos);
            if (bestDistance == -1.0 || distance < bestDistance) {
                bestDistance = distance;
                bestOffset = offset;
            }
            if (pos.yCoord - hitPos.yCoord < 0.0) {
                minAngle = offset;
                continue;
            }
            maxAngle = offset;
        }
        return bestOffset;
    }

    public static boolean projectileTrajectoryHitsEntity(Entity target, Vec3d shootPos, Vec3d targetPos, ProjectileTraceResult result) {
        EntityPlayerSP localPlayer = ProjectileUtils.MC.thePlayer;
        Vec3d selfPos = localPlayer.getPositionVector();
        ItemStack heldItem = localPlayer.getHeldItemMainhand();
        double min = ProjectileUtils.getMinForce(heldItem), max = 0D;
        block0 : for (double force = max = ProjectileUtils.getMaxForce((ItemStack)heldItem); force >= min; force -= min) {
            Vec3d startPos;
            Angle angle = ProjectileUtils.getShootAngle(heldItem, shootPos, targetPos, force);
            if (angle == null) continue;
            Angle initAngle = new Angle(- angle.getPitch(), angle.getYaw() + 90.0, 0.0);
            double fixX = Math.cos(initAngle.getYaw(true) - 1.5707963267948966) * 0.16;
            double fixY = 0.10000000149011612;
            double fixZ = Math.sin(initAngle.getYaw(true) - 1.5707963267948966) * 0.16;
            Vec3d initPos = new Vec3d(- fixX, (double)localPlayer.getEyeHeight() - fixY, - fixZ);
            Vec3d acceleration = ProjectileUtils.getGravity(heldItem);
            Vec3d airResistance = ProjectileUtils.getAirResistance(heldItem);
            Vec3d velocity = initAngle.getCartesianCoords().normalize().scale(force);
            Vec3d endPos = startPos = initPos;
            for (int i = 0; i < 100; ++i) {
                startPos = startPos.add(velocity);
                velocity = VectorUtils.multiplyBy(velocity, airResistance);
                velocity = velocity.add(acceleration);
                Vec3d wrlStart = selfPos.add(startPos);
                Vec3d wrlEnd = selfPos.add(endPos);
                RayTraceResult tr = ProjectileUtils.getWorld().rayTraceBlocks(wrlStart, wrlEnd);
                if (tr != null && !ProjectileUtils.getWorld().getBlockState(tr.getBlockPos()).getBlock().isPassable((IBlockAccess)ProjectileUtils.getWorld(), tr.getBlockPos())) continue block0;
                tr = target.getEntityBoundingBox().calculateIntercept(wrlStart, wrlEnd);
                if (tr != null) {
                    if (result != null) {
                        result.maxForce = force;
                        result.shootAngle = angle;
                    }
                    return true;
                }
                endPos = startPos;
            }
        }
        return false;
    }

    public static class ProjectileTraceResult {
        public double maxForce = 0.0;
        public Angle shootAngle = new Angle();
    }

}

