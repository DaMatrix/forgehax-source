/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureType
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.monster.EntityIronGolem
 *  net.minecraft.entity.monster.EntityPigZombie
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package com.matt.forgehax.util.entity;

import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import com.matt.forgehax.util.entity.PlayerUtils;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class EntityUtils
extends ForgeHaxBase {
    public static boolean isBatsDisabled = false;

    public static boolean isMobAggressive(Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry()) {
                if (!((EntityPigZombie)entity).isAngry()) {
                    //TODO: Comment to remind me to change this for the next update
                	ObfuscationReflectionHelper.setPrivateValue(EntityPigZombie.class, (EntityPigZombie) entity, 400, "angerLevel", "field_70837_d");
                }
                return true;
            }
        } else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf)entity).isAngry() && !EntityUtils.MC.thePlayer.equals((Object)((EntityWolf)entity).getOwner());
            }
            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman)entity).isScreaming();
            }
        }
        return false;
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static boolean isValidEntity(Entity entity) {
        return entity.ticksExisted > 1;
    }

    public static boolean isAlive(Entity entity) {
        return EntityUtils.isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }

    public static boolean isFriendlyMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.CREATURE, false) && !EntityUtils.isNeutralMob(entity) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) && !isBatsDisabled || entity instanceof EntityVillager || entity instanceof EntityIronGolem || EntityUtils.isNeutralMob(entity) && !EntityUtils.isMobAggressive(entity);
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtils.isNeutralMob(entity) || EntityUtils.isMobAggressive(entity);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posY - entity.lastTickPosY) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return EntityUtils.getInterpolatedAmount(entity, vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return EntityUtils.getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosY).add(EntityUtils.getInterpolatedAmount(entity, ticks));
    }

    public static Vec3d getInterpolatedEyePos(Entity entity, float ticks) {
        return EntityUtils.getInterpolatedPos(entity, ticks).addVector(0.0, (double)entity.getEyeHeight(), 0.0);
    }

    public static Vec3d getEyePos(Entity entity) {
        return new Vec3d(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posY);
    }

    public static Vec3d getOBBCenter(Entity entity) {
        AxisAlignedBB obb = entity.getEntityBoundingBox();
        return new Vec3d((obb.maxX + obb.minX) / 2.0, (obb.maxY + obb.minY) / 2.0, (obb.maxZ + obb.minZ) / 2.0);
    }

    public static RayTraceResult traceEntity(World world, Vec3d start, Vec3d end, List<Entity> filter) {
        RayTraceResult result = null;
        double hitDistance = -1.0;
        for (Object obj : world.loadedEntityList) {
            Entity entity = (Entity)obj;
            if (filter.contains((Object)entity)) continue;
            double distance = start.distanceTo(entity.getPositionVector());
            RayTraceResult trace = entity.getEntityBoundingBox().calculateIntercept(start, end);
            if (trace == null || hitDistance != -1.0 && distance >= hitDistance) continue;
            hitDistance = distance;
            result = trace;
            result.entityHit = entity;
        }
        return result;
    }

    public static int getDrawColor(EntityLivingBase living) {
        if (LocalPlayerUtils.isTargetEntity((Entity)living)) {
            return Utils.Colors.WHITE;
        }
        if (EntityUtils.isPlayer((Entity)living)) {
            if (PlayerUtils.isFriend((EntityPlayer)living)) {
                return Utils.Colors.GREEN;
            }
            return Utils.Colors.RED;
        }
        if (EntityUtils.isHostileMob((Entity)living)) {
            return Utils.Colors.ORANGE;
        }
        if (EntityUtils.isFriendlyMob((Entity)living)) {
            return Utils.Colors.GREEN;
        }
        return Utils.Colors.WHITE;
    }
}

