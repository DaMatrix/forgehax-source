/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.MethodNode
 */
package com.matt.forgehax.asm.patches;

import com.matt.forgehax.asm.Names;
import com.matt.forgehax.asm.helper.AsmClass;
import com.matt.forgehax.asm.helper.AsmField;
import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.ClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

public class EntityPlayerSPPatch
extends ClassTransformer {
    public final AsmMethod ON_LIVING_UPDATE = ((AsmMethod)((AsmMethod)new AsmMethod().setName("onLivingUpdate")).setObfuscatedName("n")).setArgumentTypes(new Object[0]).setReturnType(Void.TYPE).setHooks(new AsmMethod[0]);
    private final int[] applySlowdownSpeedSig = new int[]{154, 0, 0, 25, 180, 89, 180, 18, 106, 181};

    public EntityPlayerSPPatch() {
        this.registerHook(this.ON_LIVING_UPDATE);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.ON_LIVING_UPDATE.getRuntimeName()) && method.desc.equals(this.ON_LIVING_UPDATE.getDescriptor())) {
            this.updatePatchedMethods(this.applyLivingUpdatePatch(method));
            return true;
        }
        return false;
    }

    private boolean applyLivingUpdatePatch(MethodNode method) {
        AbstractInsnNode applySlowdownSpeedNode = this.findPattern("onLivingUpdate", "applySlowdownSpeedNode", method.instructions.getFirst(), this.applySlowdownSpeedSig, "x??xxxxxxx");
        if (applySlowdownSpeedNode != null && applySlowdownSpeedNode instanceof JumpInsnNode) {
            LabelNode jumpTo = ((JumpInsnNode)applySlowdownSpeedNode).label;
            InsnList insnList = new InsnList();
            insnList.add((AbstractInsnNode)new FieldInsnNode(178, this.NAMES.IS_NOSLOWDOWN_ACTIVE.getParentClass().getRuntimeName(), this.NAMES.IS_NOSLOWDOWN_ACTIVE.getRuntimeName(), this.NAMES.IS_NOSLOWDOWN_ACTIVE.getTypeDescriptor()));
            insnList.add((AbstractInsnNode)new JumpInsnNode(154, jumpTo));
            method.instructions.insert(applySlowdownSpeedNode, insnList);
            return true;
        }
        return false;
    }
}

