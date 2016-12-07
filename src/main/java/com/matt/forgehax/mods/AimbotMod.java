/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.google.common.collect.Lists;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.LagCompensator;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.entity.EntityUtils;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import com.matt.forgehax.util.entity.PlayerUtils;
import com.matt.forgehax.util.key.Bindings;
import com.matt.forgehax.util.key.KeyBindingHandler;
import com.matt.forgehax.util.math.Angle;
import com.matt.forgehax.util.math.ProjectileUtils;
import com.matt.forgehax.util.math.VectorUtils;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AimbotMod
extends ToggleMod {
    public Property silent;
    public Property autoAttack;
    public Property projectileAutoAttack;
    public Property holdTarget;
    public Property visibilityCheck;
    public Property projectileAimbot;
    public Property projectileTraceCheck;
    public Property fov;
    public Property range;
    public Property projectileRange;
    public Property cooldownPercent;
    public Property players;
    public Property hostileMobs;
    public Property friendlyMobs;
    public Property lagCompensation;
    private final LagCompensator compensator = LagCompensator.getInstance();

    public AimbotMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    private float getLagComp() {
        if (this.lagCompensation.getBoolean()) {
            return - 20 - this.compensator.getTickRate();
        }
        return 0.0f;
    }

    public boolean canAttack(EntityPlayer localPlayer, Entity target) {
        return (double)localPlayer.getCooledAttackStrength(this.getLagComp()) >= this.cooldownPercent.getDouble() / 100.0 && (this.autoAttack.getBoolean() || Bindings.attack.getBinding().isKeyDown());
    }

    public boolean isHoldingProjectileItem() {
        return ProjectileUtils.isThrowable(AimbotMod.MC.thePlayer.getHeldItemMainhand());
    }

    public boolean isProjectileAimbotActivated() {
        return this.projectileAimbot.getBoolean() && this.isHoldingProjectileItem();
    }

    public boolean isVisible(Entity target) {
        if (this.isProjectileAimbotActivated() && this.projectileTraceCheck.getBoolean()) {
            return ProjectileUtils.projectileTrajectoryHitsEntity(target, EntityUtils.getEyePos((Entity)AimbotMod.getLocalPlayer()), this.getAimPos(target), null);
        }
        return !this.visibilityCheck.getBoolean() || AimbotMod.getLocalPlayer().canEntityBeSeen(target);
    }

    public Vec3d getAimPos(Entity entity) {
        return EntityUtils.getInterpolatedPos(entity, 1.0f).addVector(0.0, (double)(entity.getEyeHeight() / 2.0f), 0.0);
    }

    public boolean isValidTarget(Entity entity, Vec3d entPos, Vec3d selfPos, Vec3d lookVec, Angle viewAngles) {
        return EntityUtils.isLiving(entity) && EntityUtils.isAlive(entity) && !entity.equals((Object)AimbotMod.MC.thePlayer) && EntityUtils.isValidEntity(entity) && (EntityUtils.isPlayer(entity) && this.players.getBoolean() && /*!PlayerUtils.isFriend((EntityPlayer)entity) || EntityUtils.isHostileMob(entity) &&*/ this.hostileMobs.getBoolean() || EntityUtils.isFriendlyMob(entity) && this.friendlyMobs.getBoolean()) && this.isInRange(entPos, selfPos) && this.isInFOVRange(viewAngles, entPos.subtract(selfPos)) && this.isVisible(entity);
    }

    public boolean isInRange(Vec3d fromPos, Vec3d toPos) {
        double dist = this.isProjectileAimbotActivated() ? this.projectileRange.getDouble() : this.range.getDouble();
        return dist <= 0.0 || fromPos.distanceTo(toPos) <= dist;
    }

    public boolean isInFOVRange(Angle selfAngle, Vec3d diffPos) {
        double value = this.fov.getDouble();
        if (value >= 180.0) {
            return true;
        }
        Angle diff = VectorUtils.vectorAngle(diffPos);
        double pitch = Math.abs(Utils.normalizeAngle(selfAngle.getPitch() - diff.getPitch()));
        double yaw = Math.abs(Utils.normalizeAngle(selfAngle.getYaw() - diff.getYaw()));
        return pitch <= value && yaw <= value;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Entity findTargetEntity(Vec3d selfPos, Vec3d selfLookVec, Angle viewAngles) {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        Vec3d selfLookVecNormal = selfLookVec.normalize();
        Entity target = null;
        double shortestDistance = -1.0;
        List list = world.loadedEntityList;
        synchronized (list) {
            for (Object entity : Collections.synchronizedList(Lists.newArrayList((Iterable)world.loadedEntityList))) {
                Entity goodEntity = (Entity) entity;
            	Vec3d pos;
                if (goodEntity == null || !this.isValidTarget(goodEntity, pos = EntityUtils.getOBBCenter(goodEntity), selfPos, selfLookVec, viewAngles)) continue;
                double distance = pos.subtract(selfPos).normalize().subtract(selfLookVecNormal).lengthVector();
                if (shortestDistance != -1.0 && distance >= shortestDistance) continue;
                target = goodEntity;
                shortestDistance = distance;
            }
        }
        LocalPlayerUtils.setTargetEntity(target);
        return target;
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.silent = configuration.get(this.getModName(), "aim_silent", true, "Won't lock onto target");
        Property[] arrproperty = new Property[]{this.silent, this.autoAttack = configuration.get(this.getModName(), "aim_autoattack", true, "Automatically attack"), this.visibilityCheck = configuration.get(this.getModName(), "aim_vischeck", true, "If the aimbot will do visibility checks"), this.projectileAutoAttack = configuration.get(this.getModName(), "projectile_autoattack", true, "Automatically attack"), this.holdTarget = configuration.get(this.getModName(), "aim_hold_target", false, "Keeps target until it is no longer a valid attack target"), this.projectileAimbot = configuration.get(this.getModName(), "projectile_aimbot", false, "Aimbot for bows, snowballs, and eggs"), this.projectileTraceCheck = configuration.get(this.getModName(), "projectile_trace_check", true, "Requires beefy computer, will check if targets can be hit by the bows trajectory"), this.fov = configuration.get(this.getModName(), "aim_fov", 40.0, "Aimbot field of view", 0.0, 180.0), this.range = configuration.get(this.getModName(), "aim_range", 4.5, "Attack range"), this.projectileRange = configuration.get(this.getModName(), "projectile_range", 100.0, "Attack range for projectiles"), this.cooldownPercent = configuration.get(this.getModName(), "aim_cooldown_percent", 100.0, "What cooldown % to attack again at", 0.0, 100.0), this.players = configuration.get(this.getModName(), "tar_players", true, "Attack players"), this.hostileMobs = configuration.get(this.getModName(), "tar_hostile_mobs", true, "Attack hostile mobs"), this.friendlyMobs = configuration.get(this.getModName(), "tar_friendly_mobs", true, "Attack friendly mobs"), this.lagCompensation = configuration.get(this.getModName(), "aim_lagcomp", true, "Compensate for tps lag")};
        this.addSettings(arrproperty);
    }

    @Override
    public void onDisabled() {
        LocalPlayerUtils.setTargetEntity(null);
        LocalPlayerUtils.setActiveFakeAngles(false);
        LocalPlayerUtils.setProjectileTargetAcquired(false);
        LocalPlayerUtils.setFakeViewAngles(null);
    }

    @SubscribeEvent
    public void onLocalPlayerUpdate(LocalPlayerUpdateEvent event) {
        EntityPlayerSP localPlayer = AimbotMod.MC.thePlayer;
        Entity target = LocalPlayerUtils.getTargetEntity();
        Vec3d selfPos = EntityUtils.getEyePos((Entity)localPlayer);
        Vec3d selfLookVec = localPlayer.getLookVec();
        Angle viewAngles = VectorUtils.vectorAngle(selfLookVec);
        if (this.holdTarget.getBoolean()) {
            if (target == null || !this.isValidTarget(target, EntityUtils.getOBBCenter(target), selfPos, selfLookVec, viewAngles)) {
                target = this.findTargetEntity(selfPos, selfLookVec, viewAngles);
            }
        } else {
            target = this.findTargetEntity(selfPos, selfLookVec, viewAngles);
        }
        if (target != null) {
            if (!this.isHoldingProjectileItem()) {
                Angle aim = Utils.getLookAtAngles(target);
                if (!this.silent.getBoolean()) {
                    LocalPlayerUtils.setViewAngles(aim);
                }
                if (this.canAttack((EntityPlayer)localPlayer, target)) {
                    AimbotMod.MC.playerController.attackEntity((EntityPlayer)AimbotMod.MC.thePlayer, target);
                    localPlayer.swingArm(EnumHand.MAIN_HAND);
                    if (this.silent.getBoolean()) {
                        LocalPlayerUtils.setActiveFakeAngles(true);
                        LocalPlayerUtils.setFakeViewAngles(aim);
                        LocalPlayerUtils.sendRotatePacket(aim);
                        return;
                    }
                }
            } else {
                ItemStack heldItem = localPlayer.getHeldItemMainhand();
                ProjectileUtils.ProjectileTraceResult result = new ProjectileUtils.ProjectileTraceResult();
                boolean exists = ProjectileUtils.projectileTrajectoryHitsEntity(target, selfPos, this.getAimPos(target), result);
                if (!exists || result.shootAngle == null) {
                    LocalPlayerUtils.setProjectileTargetAcquired(false);
                } else {
                    LocalPlayerUtils.setProjectileTargetAcquired(true);
                    LocalPlayerUtils.setFakeViewAngles(result.shootAngle);
                    if (!this.silent.getBoolean() && Bindings.use.getBinding().isKeyDown()) {
                        LocalPlayerUtils.setViewAngles(result.shootAngle);
                    }
                    LocalPlayerUtils.setActiveFakeAngles(false);
                    if (this.projectileAutoAttack.getBoolean() && Bindings.use.getBinding().isKeyDown() && ProjectileUtils.getForce(heldItem) >= result.maxForce) {
                        Bindings.use.setPressed(false);
                    }
                    return;
                }
            }
        }
        LocalPlayerUtils.setActiveFakeAngles(false);
        LocalPlayerUtils.setProjectileTargetAcquired(false);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPacketSending(PacketEvent.Send.Pre event) {
        EntityPlayerSP localPlayer;
        ItemStack heldItem;
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            //TODO: Comment to remind me to change this for the next update
            boolean rotating = ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, packet, "rotating", "field_149481_i");
            if (rotating && LocalPlayerUtils.isFakeAnglesActive() && LocalPlayerUtils.getFakeViewAngles() != null) {
                Angle viewAngles = LocalPlayerUtils.getFakeViewAngles();
                ObfuscationReflectionHelper.setPrivateValue(CPacketPlayer.class, packet, (float) viewAngles.getPitch(), "pitch", "field_149473_f");
                ObfuscationReflectionHelper.setPrivateValue(CPacketPlayer.class, packet, (float) viewAngles.getYaw(), "yaw", "field_149476_e");
            }
        } else if (event.getPacket() instanceof CPacketPlayerDigging) {
            EntityPlayerSP localPlayer2;
            ItemStack heldItem2;
            if (((CPacketPlayerDigging)event.getPacket()).getAction().equals((Object)CPacketPlayerDigging.Action.RELEASE_USE_ITEM) && LocalPlayerUtils.isProjectileTargetAcquired() && !Utils.OUTGOING_PACKET_IGNORE_LIST.contains((Object)event.getPacket()) && (heldItem2 = (localPlayer2 = AimbotMod.MC.thePlayer).getHeldItemMainhand()) != null && ProjectileUtils.isBow(heldItem2)) {
                LocalPlayerUtils.sendRotatePacket(LocalPlayerUtils.getFakeViewAngles());
                CPacketPlayerDigging usePacket = new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
                Utils.OUTGOING_PACKET_IGNORE_LIST.add((Packet)usePacket);
                AimbotMod.getNetworkManager().sendPacket((Packet)usePacket);
                LocalPlayerUtils.sendRotatePacket(LocalPlayerUtils.getViewAngles());
                event.setCanceled(true);
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && LocalPlayerUtils.isProjectileTargetAcquired() && !Utils.OUTGOING_PACKET_IGNORE_LIST.contains((Object)event.getPacket()) && ((CPacketPlayerTryUseItem)event.getPacket()).getHand().equals((Object)EnumHand.MAIN_HAND) && (heldItem = (localPlayer = AimbotMod.MC.thePlayer).getHeldItemMainhand()) != null && ProjectileUtils.isThrowable(heldItem) && !ProjectileUtils.isBow(heldItem)) {
            LocalPlayerUtils.sendRotatePacket(LocalPlayerUtils.getFakeViewAngles());
            CPacketPlayerTryUseItem usePacket = new CPacketPlayerTryUseItem(((CPacketPlayerTryUseItem)event.getPacket()).getHand());
            Utils.OUTGOING_PACKET_IGNORE_LIST.add((Packet)usePacket);
            AimbotMod.getNetworkManager().sendPacket((Packet)usePacket);
            LocalPlayerUtils.sendRotatePacket(LocalPlayerUtils.getViewAngles());
            event.setCanceled(true);
        }
    }
}

