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
import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.ClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class WorldPatch
extends ClassTransformer {
    public final AsmMethod HANDLE_MATERIAL_ACCELERATION;
    private final int[] handleMaterialAccelerationPreSignature;
    private final int[] handleMaterialAccelerationPostSignature;

    public WorldPatch() {
        this.HANDLE_MATERIAL_ACCELERATION = ((AsmMethod)((AsmMethod)new AsmMethod().setName("handleMaterialAcceleration")).setObfuscatedName("a")).setArgumentTypes(this.NAMES.AXISALIGNEDBB, this.NAMES.MATERIAL, this.NAMES.ENTITY).setReturnType(Boolean.TYPE).setHooks(this.NAMES.ON_WATER_MOVEMENT);
        this.handleMaterialAccelerationPreSignature = new int[]{25, 182, 58, 0, 0, 18, 57, 0, 0, 25, 89, 180, 25, 180, 18, 107, 99, 181};
        this.handleMaterialAccelerationPostSignature = new int[]{21, 172};
        this.registerHook(this.HANDLE_MATERIAL_ACCELERATION);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.HANDLE_MATERIAL_ACCELERATION.getRuntimeName()) && method.desc.equals(this.HANDLE_MATERIAL_ACCELERATION.getDescriptor())) {
            this.updatePatchedMethods(this.handleMaterialAccelerationPatch(method));
            return true;
        }
        return false;
    }

    private boolean handleMaterialAccelerationPatch(MethodNode method) {
        AbstractInsnNode preNode = this.findPattern("handleMaterialAcceleration", "preNode", method.instructions.getFirst(), this.handleMaterialAccelerationPreSignature, "xxx??xx??xxxxxxxxx");
        AbstractInsnNode postNode = this.findPattern("handleMaterialAcceleration", "postNode", method.instructions.getFirst(), this.handleMaterialAccelerationPostSignature, "xx");
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 3));
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 11));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_WATER_MOVEMENT.getParentClass().getRuntimeName(), this.NAMES.ON_WATER_MOVEMENT.getRuntimeName(), this.NAMES.ON_WATER_MOVEMENT.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insertBefore(postNode, (AbstractInsnNode)endJump);
            return true;
        }
        return false;
    }
}

