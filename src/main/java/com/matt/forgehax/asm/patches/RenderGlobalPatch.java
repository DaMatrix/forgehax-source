/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package com.matt.forgehax.asm.patches;

import com.matt.forgehax.asm.Names;
import com.matt.forgehax.asm.helper.AsmClass;
import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.ClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class RenderGlobalPatch
extends ClassTransformer {
    public final AsmMethod RENDER_BLOCK_LAYER;
    public final AsmMethod SETUP_TERRAIN;
    private final int[] renderBlockLayerPreSig;
    private final int[] renderBlockLayerPostSig;
    private final int[] setupTerrainSig;

    public RenderGlobalPatch() {
        this.RENDER_BLOCK_LAYER = ((AsmMethod)((AsmMethod)new AsmMethod().setName("renderBlockLayer")).setObfuscatedName("a")).setArgumentTypes(this.NAMES.BLOCK_RENDER_LAYER, Double.TYPE, Integer.TYPE, this.NAMES.ENTITY).setReturnType(Integer.TYPE).setHooks(this.NAMES.ON_PRERENDER_BLOCKLAYER, this.NAMES.ON_POSTRENDER_BLOCKLAYER);
        this.SETUP_TERRAIN = ((AsmMethod)((AsmMethod)new AsmMethod().setName("setupTerrain")).setObfuscatedName("a")).setArgumentTypes(this.NAMES.ENTITY, Double.TYPE, this.NAMES.ICAMERA, Integer.TYPE, Boolean.TYPE).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_SETUP_TERRAIN);
        this.renderBlockLayerPreSig = new int[]{184, 0, 0, 25, 178, 166, 0, 0, 25, 180, 180};
        this.renderBlockLayerPostSig = new int[]{25, 180, 180, 182, 0, 0, 21, 172};
        this.setupTerrainSig = new int[]{25, 180, 180, 180, 25};
        this.registerHook(this.RENDER_BLOCK_LAYER);
        this.registerHook(this.SETUP_TERRAIN);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.RENDER_BLOCK_LAYER.getRuntimeName()) && method.desc.equals(this.RENDER_BLOCK_LAYER.getDescriptor())) {
            this.updatePatchedMethods(this.renderBlockLayerPatch(method));
            return true;
        }
        if (method.name.equals(this.SETUP_TERRAIN.getRuntimeName()) && method.desc.equals(this.SETUP_TERRAIN.getDescriptor())) {
            this.updatePatchedMethods(this.setupTerrainPatch(method));
            return true;
        }
        return false;
    }

    public boolean renderBlockLayerPatch(MethodNode node) {
        AbstractInsnNode preNode = this.findPattern("renderBlockLayer", "preNode", node.instructions.getFirst(), this.renderBlockLayerPreSig, "x??xxx??xxx");
        AbstractInsnNode postNode = this.findPattern("renderBlockLayer", "postNode", node.instructions.getFirst(), this.renderBlockLayerPostSig, "xxxx??xx");
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new InsnNode(3));
            insnPre.add((AbstractInsnNode)new VarInsnNode(54, 6));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnPre.add((AbstractInsnNode)new VarInsnNode(24, 2));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_PRERENDER_BLOCKLAYER.getParentClass().getRuntimeName(), this.NAMES.ON_PRERENDER_BLOCKLAYER.getRuntimeName(), this.NAMES.ON_PRERENDER_BLOCKLAYER.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            InsnList insnPost = new InsnList();
            insnPost.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnPost.add((AbstractInsnNode)new VarInsnNode(24, 2));
            insnPost.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_POSTRENDER_BLOCKLAYER.getParentClass().getRuntimeName(), this.NAMES.ON_POSTRENDER_BLOCKLAYER.getRuntimeName(), this.NAMES.ON_POSTRENDER_BLOCKLAYER.getDescriptor(), false));
            insnPost.add((AbstractInsnNode)endJump);
            node.instructions.insertBefore(preNode, insnPre);
            node.instructions.insertBefore(postNode, insnPost);
            return true;
        }
        return false;
    }

    public boolean setupTerrainPatch(MethodNode method) {
        AbstractInsnNode node = this.findPattern("setupTerrain", "node", method.instructions.getFirst(), this.setupTerrainSig, "xxxxx");
        if (node != null) {
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnPre.add((AbstractInsnNode)new VarInsnNode(21, 6));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_SETUP_TERRAIN.getParentClass().getRuntimeName(), this.NAMES.ON_SETUP_TERRAIN.getRuntimeName(), this.NAMES.ON_SETUP_TERRAIN.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new VarInsnNode(54, 6));
            method.instructions.insertBefore(node, insnPre);
            return true;
        }
        return false;
    }
}

