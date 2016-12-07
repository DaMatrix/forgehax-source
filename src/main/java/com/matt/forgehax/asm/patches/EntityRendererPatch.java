/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package com.matt.forgehax.asm.patches;

import com.matt.forgehax.asm.Names;
import com.matt.forgehax.asm.helper.AsmClass;
import com.matt.forgehax.asm.helper.AsmHelper;
import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.ClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityRendererPatch
extends ClassTransformer {
    public final AsmMethod HURTCAMERAEFFECT;
    private final int[] hurtCameraEffectPreSignature;
    private final int[] hurtCameraEffectPostSignature;

    public EntityRendererPatch() {
        this.HURTCAMERAEFFECT = ((AsmMethod)((AsmMethod)new AsmMethod().setName("hurtCameraEffect")).setObfuscatedName("d")).setArgumentTypes(Float.TYPE).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_HURTCAMEFFECT);
        this.hurtCameraEffectPreSignature = new int[]{25, 180, 182, 193, 153};
        this.hurtCameraEffectPostSignature = new int[]{177};
        this.registerHook(this.HURTCAMERAEFFECT);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.HURTCAMERAEFFECT.getRuntimeName()) && method.desc.equals(this.HURTCAMERAEFFECT.getDescriptor())) {
            this.updatePatchedMethods(this.hurtCameraEffectPatch(method));
            return true;
        }
        return false;
    }

    private boolean hurtCameraEffectPatch(MethodNode method) {
        AbstractInsnNode preNode = null;
        AbstractInsnNode postNode = null;
        try {
            preNode = AsmHelper.findPattern(method.instructions.getFirst(), this.hurtCameraEffectPreSignature, "xxxxx");
        }
        catch (Exception e) {
            this.log("hurtCameraEffect", "preNode error: %s\n", e.getMessage());
        }
        try {
            postNode = AsmHelper.findPattern(method.instructions.getFirst(), this.hurtCameraEffectPostSignature, "x");
        }
        catch (Exception e) {
            this.log("hurtCameraEffect", "postNode error: %s\n", e.getMessage());
        }
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(23, 1));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_HURTCAMEFFECT.getParentClass().getRuntimeName(), this.NAMES.ON_HURTCAMEFFECT.getRuntimeName(), this.NAMES.ON_HURTCAMEFFECT.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insertBefore(postNode, (AbstractInsnNode)endJump);
            return true;
        }
        return false;
    }
}

