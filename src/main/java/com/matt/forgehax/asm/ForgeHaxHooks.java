/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.renderer.VertexBuffer
 *  net.minecraft.client.renderer.chunk.SetVisibility
 *  net.minecraft.client.renderer.chunk.VisGraph
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.util.BlockRenderLayer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.IBlockAccess
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 */
package com.matt.forgehax.asm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.matt.forgehax.asm.events.ApplyClimbableBlockMovement;
import com.matt.forgehax.asm.events.ApplyCollisionMotionEvent;
import com.matt.forgehax.asm.events.ComputeVisibilityEvent;
import com.matt.forgehax.asm.events.DoBlockCollisionsEvent;
import com.matt.forgehax.asm.events.HurtCamEffectEvent;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.asm.events.RenderBlockInLayerEvent;
import com.matt.forgehax.asm.events.RenderBlockLayerEvent;
import com.matt.forgehax.asm.events.SetupTerrainEvent;
import com.matt.forgehax.asm.events.WaterMovementEvent;
import com.matt.forgehax.asm.events.WebMotionEvent;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class ForgeHaxHooks {
    public static boolean isInDebugMode = true;
    public static final Map<String, DebugData> responding = Maps.newLinkedHashMap();
    public static boolean isSafeWalkActivated;
    public static boolean isNoSlowDownActivated;
    public static boolean SHOULD_UPDATE_ALPHA;
    public static float COLOR_MULTIPLIER_ALPHA;

    private static void reportHook(String name) {
        DebugData debug;
        if (isInDebugMode && (debug = responding.get(name)) != null) {
            debug.hasResponded = true;
            debug.isResponding = true;
        }
    }

    public static void setHooksLog(String className, List<String> log, int methodCount) {
        for (DebugData data : responding.values()) {
            if (!data.parentClassNames.contains(className)) continue;
            data.log = log;
            data.targetCount = methodCount;
        }
    }

    public static boolean onHurtcamEffect(float partialTicks) {
        ForgeHaxHooks.reportHook("onHurtcamEffect");
        return MinecraftForge.EVENT_BUS.post((Event)new HurtCamEffectEvent(partialTicks));
    }

    public static boolean onSendingPacket(Packet<?> packet) {
        ForgeHaxHooks.reportHook("onSendingPacket");
        return MinecraftForge.EVENT_BUS.post((Event)new PacketEvent.Send.Pre(packet));
    }

    public static void onSentPacket(Packet<?> packet) {
        ForgeHaxHooks.reportHook("onSentPacket");
        MinecraftForge.EVENT_BUS.post((Event)new PacketEvent.Send.Post(packet));
    }

    public static boolean onPreReceived(Packet<?> packet) {
        ForgeHaxHooks.reportHook("onPreReceived");
        return MinecraftForge.EVENT_BUS.post((Event)new PacketEvent.Received.Pre(packet));
    }

    public static void onPostReceived(Packet<?> packet) {
        ForgeHaxHooks.reportHook("onPostReceived");
        MinecraftForge.EVENT_BUS.post((Event)new PacketEvent.Received.Post(packet));
    }

    public static boolean onWaterMovement(Entity entity, Vec3d moveDir) {
        ForgeHaxHooks.reportHook("onWaterMovement");
        return MinecraftForge.EVENT_BUS.post((Event)new WaterMovementEvent(entity, moveDir));
    }

    public static boolean onApplyCollisionMotion(Entity entity, Entity collidedWithEntity, double x, double z) {
        ForgeHaxHooks.reportHook("onApplyCollisionMotion");
        return MinecraftForge.EVENT_BUS.post((Event)new ApplyCollisionMotionEvent(entity, collidedWithEntity, x, 0.0, z));
    }

    public static WebMotionEvent onWebMotion(Entity entity, double x, double y, double z) {
        ForgeHaxHooks.reportHook("onWebMotion");
        WebMotionEvent event = new WebMotionEvent(entity, x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
        return event;
    }

    public static int onPutColorMultiplier(float r, float g, float b, int buffer, boolean[] flag) {
        ForgeHaxHooks.reportHook("onPutColorMultiplier");
        flag[0] = SHOULD_UPDATE_ALPHA;
        if (SHOULD_UPDATE_ALPHA) {
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                int red = (int)((float)(buffer & 255) * r);
                int green = (int)((float)(buffer >> 8 & 255) * g);
                int blue = (int)((float)(buffer >> 16 & 255) * b);
                int alpha = (int)((float)(buffer >> 24 & 255) * COLOR_MULTIPLIER_ALPHA);
                buffer = alpha << 24 | blue << 16 | green << 8 | red;
            } else {
                int red = (int)((float)(buffer >> 24 & 255) * r);
                int green = (int)((float)(buffer >> 16 & 255) * g);
                int blue = (int)((float)(buffer >> 8 & 255) * b);
                int alpha = (int)((float)(buffer & 255) * COLOR_MULTIPLIER_ALPHA);
                buffer = red << 24 | green << 16 | blue << 8 | alpha;
            }
        }
        return buffer;
    }

    public static boolean onPreRenderBlockLayer(BlockRenderLayer layer, double partialTicks) {
        ForgeHaxHooks.reportHook("onPreRenderBlockLayer");
        return MinecraftForge.EVENT_BUS.post((Event)new RenderBlockLayerEvent.Pre(layer, partialTicks));
    }

    public static void onPostRenderBlockLayer(BlockRenderLayer layer, double partialTicks) {
        ForgeHaxHooks.reportHook("onPostRenderBlockLayer");
        MinecraftForge.EVENT_BUS.post((Event)new RenderBlockLayerEvent.Post(layer, partialTicks));
    }

    public static RenderBlockInLayerEvent onRenderBlockInLayer(Block block, IBlockState state, BlockRenderLayer layer, boolean result) {
        ForgeHaxHooks.reportHook("onRenderBlockInLayer");
        RenderBlockInLayerEvent event = new RenderBlockInLayerEvent(block, state, layer, result);
        MinecraftForge.EVENT_BUS.post((Event)event);
        return event;
    }

    public static boolean onSetupTerrain(Entity renderEntity, boolean playerSpectator) {
        ForgeHaxHooks.reportHook("onSetupTerrain");
        SetupTerrainEvent event = new SetupTerrainEvent(renderEntity, playerSpectator);
        MinecraftForge.EVENT_BUS.post((Event)event);
        return event.isCulling();
    }

    public static void onComputeVisibility(VisGraph visGraph, SetVisibility setVisibility) {
        ForgeHaxHooks.reportHook("onComputeVisibility");
        MinecraftForge.EVENT_BUS.post((Event)new ComputeVisibilityEvent(visGraph, setVisibility));
    }

    public static boolean onDoBlockCollisions(Entity entity, BlockPos pos, IBlockState state) {
        ForgeHaxHooks.reportHook("onDoBlockCollisions");
        return MinecraftForge.EVENT_BUS.post((Event)new DoBlockCollisionsEvent(entity, pos, state));
    }

    public static boolean onApplyClimbableBlockMovement(EntityLivingBase livingBase) {
        return MinecraftForge.EVENT_BUS.post((Event)new ApplyClimbableBlockMovement(livingBase));
    }

    public static void onBlockRender(BlockPos pos, IBlockState state, IBlockAccess access, VertexBuffer buffer) {
    }

    static {
        responding.put("onHurtcamEffect", new DebugData("net.minecraft.client.renderer.EntityRenderer"));
        responding.put("onSendingPacket", new DebugData("net.minecraft.network.NetworkManager", "net.minecraft.network.NetworkManager$4"));
        responding.put("onSentPacket", new DebugData("net.minecraft.network.NetworkManager", "net.minecraft.network.NetworkManager$4"));
        responding.put("onPreReceived", new DebugData("net.minecraft.network.NetworkManager"));
        responding.put("onPostReceived", new DebugData("net.minecraft.network.NetworkManager"));
        responding.put("onWaterMovement", new DebugData("net.minecraft.world.World"));
        responding.put("onApplyCollisionMotion", new DebugData("net.minecraft.entity.Entity"));
        responding.put("onWebMotion", new DebugData("net.minecraft.entity.Entity"));
        responding.put("onDoBlockCollisions", new DebugData("net.minecraft.entity.Entity"));
        responding.put("onPutColorMultiplier", new DebugData("net.minecraft.client.renderer.VertexBuffer"));
        responding.put("onPreRenderBlockLayer", new DebugData("net.minecraft.client.renderer.RenderGlobal"));
        responding.put("onPostRenderBlockLayer", new DebugData("net.minecraft.client.renderer.RenderGlobal"));
        responding.put("onRenderBlockInLayer", new DebugData("net.minecraft.block.Block"));
        responding.put("onSetupTerrain", new DebugData("net.minecraft.client.renderer.RenderGlobal"));
        responding.put("onComputeVisibility", new DebugData("net.minecraft.client.renderer.chunk.VisGraph"));
        isSafeWalkActivated = false;
        isNoSlowDownActivated = false;
        SHOULD_UPDATE_ALPHA = false;
        COLOR_MULTIPLIER_ALPHA = 0.5882353f;
    }

    public static class DebugData {
        public final List<String> parentClassNames = Lists.newArrayList();
        public boolean hasResponded = false;
        public boolean isResponding = false;
        public List<String> log;
        public int targetCount = 0;

        public /* varargs */ DebugData(String ... parentClasses) {
            Collections.addAll(this.parentClassNames, parentClasses);
        }
    }

}

