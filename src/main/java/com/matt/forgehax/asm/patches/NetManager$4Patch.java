/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.FieldInsnNode
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
import com.matt.forgehax.asm.helper.AsmField;
import com.matt.forgehax.asm.helper.AsmHelper;
import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.ClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class NetManager$4Patch
extends ClassTransformer {
    public final AsmMethod RUN;
    private final int[] patternPreDispatch;
    private final int[] patternPostDispatch;

    public NetManager$4Patch() {
        this.RUN = ((AsmMethod)((AsmMethod)new AsmMethod().setName("run")).setObfuscatedName("run")).setArgumentTypes(new Object[0]).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_SENDING_PACKET, this.NAMES.ON_SENT_PACKET);
        this.patternPreDispatch = new int[]{25, 180, 25, 180, 165};
        this.patternPostDispatch = new int[]{177};
        this.registerHook(this.RUN);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.RUN.getRuntimeName()) && method.desc.equals(this.RUN.getDescriptor())) {
            this.updatePatchedMethods(this.patchRun(method));
            return true;
        }
        return false;
    }

    private boolean patchRun(MethodNode method) {
        AbstractInsnNode preNode = null;
        AbstractInsnNode postNode = null;
        try {
            preNode = AsmHelper.findPattern(method.instructions.getFirst(), this.patternPreDispatch, "xxxxx");
        }
        catch (Exception e) {
            this.log("dispatchPacket", "preNode error: %s\n", e.getMessage());
        }
        try {
            postNode = AsmHelper.findPattern(method.instructions.getFirst(), this.patternPostDispatch, "x");
        }
        catch (Exception e) {
            this.log("dispatchPacket", "postNode error: %s\n", e.getMessage());
        }
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnPre.add((AbstractInsnNode)new FieldInsnNode(180, this.NAMES.NETMANAGER$4__val$inPacket.getParentClass().getRuntimeName(), this.NAMES.NETMANAGER$4__val$inPacket.getRuntimeName(), this.NAMES.NETMANAGER$4__val$inPacket.getTypeDescriptor()));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_SENDING_PACKET.getParentClass().getRuntimeName(), this.NAMES.ON_SENDING_PACKET.getRuntimeName(), this.NAMES.ON_SENDING_PACKET.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            InsnList insnPost = new InsnList();
            insnPost.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnPost.add((AbstractInsnNode)new FieldInsnNode(180, this.NAMES.NETMANAGER$4__val$inPacket.getParentClass().getRuntimeName(), this.NAMES.NETMANAGER$4__val$inPacket.getRuntimeName(), this.NAMES.NETMANAGER$4__val$inPacket.getTypeDescriptor()));
            insnPost.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_SENT_PACKET.getParentClass().getRuntimeName(), this.NAMES.ON_SENT_PACKET.getRuntimeName(), this.NAMES.ON_SENT_PACKET.getDescriptor(), false));
            insnPost.add((AbstractInsnNode)endJump);
            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insertBefore(postNode, insnPost);
            return true;
        }
        return false;
    }
}

