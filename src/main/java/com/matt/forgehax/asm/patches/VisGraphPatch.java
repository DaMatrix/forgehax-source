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

public class VisGraphPatch
extends ClassTransformer {
    public final AsmMethod COMPUTE_VISIBILITY;
    private final int[] computeVisSignature;

    public VisGraphPatch() {
        this.COMPUTE_VISIBILITY = ((AsmMethod)((AsmMethod)new AsmMethod().setName("computeVisibility")).setObfuscatedName("a")).setArgumentTypes(new Object[0]).setReturnType(this.NAMES.SETVISIBILITY).setHooks(this.NAMES.ON_COMPUTE_VISIBILITY);
        this.computeVisSignature = new int[]{25, 176};
        this.registerHook(this.COMPUTE_VISIBILITY);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.COMPUTE_VISIBILITY.getRuntimeName()) && method.desc.equals(this.COMPUTE_VISIBILITY.getDescriptor())) {
            this.updatePatchedMethods(this.computeVisibilityPatch(method));
            return true;
        }
        return false;
    }

    private boolean computeVisibilityPatch(MethodNode method) {
        AbstractInsnNode node = this.findPattern("computeVisibility", "node", method.instructions.getFirst(), this.computeVisSignature, "xx");
        if (node != null) {
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_COMPUTE_VISIBILITY.getParentClass().getRuntimeName(), this.NAMES.ON_COMPUTE_VISIBILITY.getRuntimeName(), this.NAMES.ON_COMPUTE_VISIBILITY.getDescriptor(), false));
            method.instructions.insertBefore(node, insnPre);
            return true;
        }
        return false;
    }
}

