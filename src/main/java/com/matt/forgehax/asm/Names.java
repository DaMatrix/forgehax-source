/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.Type
 */
package com.matt.forgehax.asm;

import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.asm.helper.AsmClass;
import com.matt.forgehax.asm.helper.AsmField;
import com.matt.forgehax.asm.helper.AsmMethod;
import org.objectweb.asm.Type;

public class Names {
    public static final Names INSTANCE = new Names();
    public final AsmClass PACKET = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/network/Packet")).setObfuscatedName("fj");
    public final AsmClass AXISALIGNEDBB = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/util/math/AxisAlignedBB")).setObfuscatedName("bby");
    public final AsmClass MATERIAL = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/block/material/Material")).setObfuscatedName("axx");
    public final AsmClass ENTITY = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/entity/Entity")).setObfuscatedName("rw");
    public final AsmClass LIVING_BASE = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/entity/EntityLivingBase")).setObfuscatedName("sf");
    public final AsmClass VEC3D = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/util/math/Vec3d")).setObfuscatedName("bcb");
    public final AsmClass BLOCK_RENDER_LAYER = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/util/BlockRenderLayer")).setObfuscatedName("ahv");
    public final AsmClass IBLOCKSTATE = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/block/state/IBlockState")).setObfuscatedName("ars");
    public final AsmClass BLOCKPOS = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/util/math/BlockPos")).setObfuscatedName("cm");
    public final AsmClass BLOCK = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/block/Block")).setObfuscatedName("akf");
    public final AsmClass ICAMERA = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/client/renderer/culling/ICamera")).setObfuscatedName("brf");
    public final AsmClass VISGRAPH = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/client/renderer/chunk/VisGraph")).setObfuscatedName("brb");
    public final AsmClass SETVISIBILITY = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/client/renderer/chunk/SetVisibility")).setObfuscatedName("brc");
    public final AsmClass NETWORK_MANAGER$4 = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/network/NetworkManager$4")).setObfuscatedName("eo$4");
    public final AsmClass IBLOCKACCESS = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/world/IBlockAccess")).setObfuscatedName("aih");
    public final AsmClass VERTEXBUFFER = (AsmClass)((AsmClass)new AsmClass().setName("net/minecraft/client/renderer/VertexBuffer")).setObfuscatedName("bnt");
    public final AsmClass WEB_MOTION_EVENT = (AsmClass)new AsmClass().setName("com/matt/forgehax/asm/events/WebMotionEvent");
    public final AsmClass RENDER_BLOCK_IN_LAYER_EVENT = (AsmClass)new AsmClass().setName("com/matt/forgehax/asm/events/RenderBlockInLayerEvent");
    public final AsmField NETMANAGER$4__val$inPacket = ((AsmField)this.NETWORK_MANAGER$4.childField().setName("val$inPacket")).setType(this.PACKET);
    public final AsmClass FORGEHAX_HOOKS = (AsmClass)new AsmClass().setName(Type.getInternalName(ForgeHaxHooks.class));
    public final AsmField IS_SAFEWALK_ACTIVE = ((AsmField)this.FORGEHAX_HOOKS.childField().setName("isSafeWalkActivated")).setType(Boolean.TYPE);
    public final AsmField IS_NOSLOWDOWN_ACTIVE = ((AsmField)this.FORGEHAX_HOOKS.childField().setName("isNoSlowDownActivated")).setType(Boolean.TYPE);
    public final AsmMethod ON_HURTCAMEFFECT = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onHurtcamEffect")).setArgumentTypes(Float.TYPE).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_SENDING_PACKET = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onSendingPacket")).setArgumentTypes(this.PACKET).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_SENT_PACKET = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onSentPacket")).setArgumentTypes(this.PACKET).setReturnType(Void.TYPE);
    public final AsmMethod ON_PRE_RECEIVED = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onPreReceived")).setArgumentTypes(this.PACKET).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_POST_RECEIVED = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onPostReceived")).setArgumentTypes(this.PACKET).setReturnType(Void.TYPE);
    public final AsmMethod ON_WATER_MOVEMENT = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onWaterMovement")).setArgumentTypes(this.ENTITY, this.VEC3D).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_APPLY_COLLISION = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onApplyCollisionMotion")).setArgumentTypes(this.ENTITY, this.ENTITY, Double.TYPE, Double.TYPE).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_WEB_MOTION = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onWebMotion")).setArgumentTypes(this.ENTITY, Double.TYPE, Double.TYPE, Double.TYPE).setReturnType(this.WEB_MOTION_EVENT);
    public final AsmMethod ON_COLOR_MULTIPLIER = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onPutColorMultiplier")).setArgumentTypes(Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, boolean[].class).setReturnType(Integer.TYPE);
    public final AsmMethod ON_PRERENDER_BLOCKLAYER = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onPreRenderBlockLayer")).setArgumentTypes(this.BLOCK_RENDER_LAYER, Double.TYPE).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_POSTRENDER_BLOCKLAYER = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onPostRenderBlockLayer")).setArgumentTypes(this.BLOCK_RENDER_LAYER, Double.TYPE).setReturnType(Void.TYPE);
    public final AsmMethod ON_RENDERBLOCK_INLAYER = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onRenderBlockInLayer")).setArgumentTypes(this.BLOCK, this.IBLOCKSTATE, this.BLOCK_RENDER_LAYER, Boolean.TYPE).setReturnType(this.RENDER_BLOCK_IN_LAYER_EVENT);
    public final AsmMethod ON_SETUP_TERRAIN = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onSetupTerrain")).setArgumentTypes(this.ENTITY, Boolean.TYPE).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_COMPUTE_VISIBILITY = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onComputeVisibility")).setArgumentTypes(this.VISGRAPH, this.SETVISIBILITY).setReturnType(Void.TYPE);
    public final AsmMethod ON_DO_BLOCK_COLLISIONS = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onDoBlockCollisions")).setArgumentTypes(this.ENTITY, this.BLOCKPOS, this.IBLOCKSTATE).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_APPLY_CLIMBABLE_BLOCK_MOVEMENT = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onApplyClimbableBlockMovement")).setArgumentTypes(this.LIVING_BASE).setReturnType(Boolean.TYPE);
    public final AsmMethod ON_RENDER_BLOCK = ((AsmMethod)this.FORGEHAX_HOOKS.childMethod().setName("onBlockRender")).setArgumentTypes(this.BLOCKPOS, this.IBLOCKSTATE, this.IBLOCKACCESS, this.VERTEXBUFFER).setReturnType(Void.TYPE);
}

