/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnList
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
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BlockRendererDispatcherPatch
extends ClassTransformer {
    public final AsmMethod ON_RENDER_BLOCK;

    public BlockRendererDispatcherPatch() {
        this.ON_RENDER_BLOCK = ((AsmMethod)((AsmMethod)new AsmMethod().setName("renderBlock")).setObfuscatedName("a")).setArgumentTypes(this.NAMES.IBLOCKSTATE, this.NAMES.BLOCKPOS, this.NAMES.IBLOCKACCESS, this.NAMES.VERTEXBUFFER).setReturnType(Boolean.TYPE).setHooks(this.NAMES.ON_RENDER_BLOCK);
        this.registerHook(this.ON_RENDER_BLOCK);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.ON_RENDER_BLOCK.getRuntimeName()) && method.desc.equals(this.ON_RENDER_BLOCK.getDescriptor())) {
            this.updatePatchedMethods(this.applyBlockRenderPatch(method));
            return true;
        }
        return false;
    }

    private boolean applyBlockRenderPatch(MethodNode method) {
        AbstractInsnNode startNode = this.findPattern("blockRender", "startNode", method.instructions.getFirst(), new int[]{25, 185}, "xx");
        if (startNode != null) {
            InsnList insnList = new InsnList();
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 2));
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 3));
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 4));
            insnList.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_RENDER_BLOCK.getParentClass().getRuntimeName(), this.NAMES.ON_RENDER_BLOCK.getRuntimeName(), this.NAMES.ON_RENDER_BLOCK.getDescriptor(), false));
            method.instructions.insertBefore(startNode, insnList);
            return true;
        }
        return false;
    }
}

