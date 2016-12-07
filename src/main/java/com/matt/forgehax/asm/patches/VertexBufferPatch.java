/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.IntInsnNode
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
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class VertexBufferPatch
extends ClassTransformer {
    public final AsmMethod PUT_COLOR_MULTIPLIER;
    private final int[] putColorMultiplierPreNode;
    private final int[] putColorMultiplierPostNode;

    public VertexBufferPatch() {
        this.PUT_COLOR_MULTIPLIER = ((AsmMethod)((AsmMethod)new AsmMethod().setName("putColorMultiplier")).setObfuscatedName("a")).setArgumentTypes(Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_COLOR_MULTIPLIER);
        this.putColorMultiplierPreNode = new int[]{184, 178, 166, 0, 0, 21, 17, 126, 134, 23, 106, 139, 54};
        this.putColorMultiplierPostNode = new int[]{25, 180, 21, 21, 182, 87};
        this.registerHook(this.PUT_COLOR_MULTIPLIER);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.PUT_COLOR_MULTIPLIER.getRuntimeName()) && method.desc.equals(this.PUT_COLOR_MULTIPLIER.getDescriptor())) {
            this.updatePatchedMethods(this.putColorMultiplierPatch(method));
            return true;
        }
        return false;
    }

    private boolean putColorMultiplierPatch(MethodNode method) {
        AbstractInsnNode preNode = this.findPattern("putColorMultiplier", "preNode", method.instructions.getFirst(), this.putColorMultiplierPreNode, "xxx??xxxxxxxx");
        AbstractInsnNode postNode = this.findPattern("putColorMultiplier", "postNode", method.instructions.getFirst(), this.putColorMultiplierPostNode, "xxxxxx");
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new InsnNode(4));
            insnPre.add((AbstractInsnNode)new IntInsnNode(188, 4));
            insnPre.add((AbstractInsnNode)new InsnNode(89));
            insnPre.add((AbstractInsnNode)new InsnNode(3));
            insnPre.add((AbstractInsnNode)new InsnNode(3));
            insnPre.add((AbstractInsnNode)new InsnNode(84));
            insnPre.add((AbstractInsnNode)new VarInsnNode(58, 10));
            insnPre.add((AbstractInsnNode)new VarInsnNode(23, 1));
            insnPre.add((AbstractInsnNode)new VarInsnNode(23, 2));
            insnPre.add((AbstractInsnNode)new VarInsnNode(23, 3));
            insnPre.add((AbstractInsnNode)new VarInsnNode(21, 6));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 10));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_COLOR_MULTIPLIER.getParentClass().getRuntimeName(), this.NAMES.ON_COLOR_MULTIPLIER.getRuntimeName(), this.NAMES.ON_COLOR_MULTIPLIER.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new VarInsnNode(54, 6));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 10));
            insnPre.add((AbstractInsnNode)new InsnNode(3));
            insnPre.add((AbstractInsnNode)new InsnNode(51));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insertBefore(postNode, (AbstractInsnNode)endJump);
            return true;
        }
        return false;
    }
}

