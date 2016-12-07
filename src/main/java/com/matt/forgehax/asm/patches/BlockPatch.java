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

public class BlockPatch
extends ClassTransformer {
    public final AsmMethod CAN_RENDER_IN_LAYER;
    private final int[] canRenderBlockLayerPreSig;
    private final int[] canRenderBlockLayerPostSig;

    public BlockPatch() {
        this.CAN_RENDER_IN_LAYER = ((AsmMethod)((AsmMethod)new AsmMethod().setName("canRenderInLayer")).setObfuscatedName("canRenderInLayer")).setArgumentTypes(this.NAMES.IBLOCKSTATE, this.NAMES.BLOCK_RENDER_LAYER).setReturnType(Boolean.TYPE).setHooks(this.NAMES.ON_RENDERBLOCK_INLAYER);
        this.canRenderBlockLayerPreSig = new int[]{182};
        this.canRenderBlockLayerPostSig = new int[]{172};
        this.registerHook(this.CAN_RENDER_IN_LAYER);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.CAN_RENDER_IN_LAYER.getRuntimeName()) && method.desc.equals(this.CAN_RENDER_IN_LAYER.getDescriptor())) {
            this.updatePatchedMethods(this.canRenderinLayerPatch(method));
            return true;
        }
        return false;
    }

    private boolean canRenderinLayerPatch(MethodNode node) {
        AbstractInsnNode preNode = this.findPattern("canRenderInLayer", "preNode", node.instructions.getFirst(), this.canRenderBlockLayerPreSig, "x");
        AbstractInsnNode postNode = this.findPattern("canRenderInLayer", "postNode", node.instructions.getFirst(), this.canRenderBlockLayerPostSig, "x");
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(54, 3));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 2));
            insnPre.add((AbstractInsnNode)new VarInsnNode(21, 3));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_RENDERBLOCK_INLAYER.getParentClass().getRuntimeName(), this.NAMES.ON_RENDERBLOCK_INLAYER.getRuntimeName(), this.NAMES.ON_RENDERBLOCK_INLAYER.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new VarInsnNode(58, 4));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 4));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(182, this.NAMES.RENDER_BLOCK_IN_LAYER_EVENT.getRuntimeName(), "isCanceled", "()Z", false));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(153, endJump));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 4));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(182, this.NAMES.RENDER_BLOCK_IN_LAYER_EVENT.getRuntimeName(), "getReturnValue", "()Z", false));
            insnPre.add((AbstractInsnNode)new InsnNode(172));
            insnPre.add((AbstractInsnNode)endJump);
            insnPre.add((AbstractInsnNode)new VarInsnNode(21, 3));
            node.instructions.insert(preNode, insnPre);
            return true;
        }
        return false;
    }
}

