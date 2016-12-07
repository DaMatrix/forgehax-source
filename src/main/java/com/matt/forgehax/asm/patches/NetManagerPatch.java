/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.util.concurrent.GenericFutureListener
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
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class NetManagerPatch
extends ClassTransformer {
    public final AsmMethod DISPATCH_PACKET;
    public final AsmMethod CHANNEL_READ0;
    private final int[] patternPreDispatch;
    private final int[] patternPostDispatch;
    private final int[] patternPreSend;
    private final int[] patternPostSend;

    public NetManagerPatch() {
        this.DISPATCH_PACKET = ((AsmMethod)((AsmMethod)new AsmMethod().setName("dispatchPacket")).setObfuscatedName("a")).setArgumentTypes(this.NAMES.PACKET, GenericFutureListener[].class).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_SENT_PACKET, this.NAMES.ON_SENDING_PACKET);
        this.CHANNEL_READ0 = ((AsmMethod)((AsmMethod)new AsmMethod().setName("channelRead0")).setObfuscatedName("a")).setArgumentTypes(ChannelHandlerContext.class, this.NAMES.PACKET).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_POST_RECEIVED, this.NAMES.ON_PRE_RECEIVED);
        this.patternPreDispatch = new int[]{25, 25, 165, 25, 193, 154, 0, 0, 25, 25, 182};
        this.patternPostDispatch = new int[]{87, 0, 0, 167, 0, 0, 0, 25, 180, 185, 187, 89};
        this.patternPreSend = new int[]{25, 25, 180, 185};
        this.patternPostSend = new int[]{185, 0, 0, 167};
        this.registerHook(this.DISPATCH_PACKET);
        this.registerHook(this.CHANNEL_READ0);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.DISPATCH_PACKET.getRuntimeName()) && method.desc.equals(this.DISPATCH_PACKET.getDescriptor())) {
            this.updatePatchedMethods(this.patchDispatchPacket(method));
            return true;
        }
        if (method.name.equals(this.CHANNEL_READ0.getRuntimeName()) && method.desc.equals(this.CHANNEL_READ0.getDescriptor())) {
            this.updatePatchedMethods(this.patchChannelRead0(method));
            return true;
        }
        return false;
    }

    private boolean patchDispatchPacket(MethodNode method) {
        AbstractInsnNode preNode = null;
        AbstractInsnNode postNode = null;
        try {
            preNode = AsmHelper.findPattern(method.instructions.getFirst(), this.patternPreDispatch, "xxxxxx??xxx");
        }
        catch (Exception e) {
            this.log("dispatchPacket", "preNode error: %s\n", e.getMessage());
        }
        try {
            postNode = AsmHelper.findPattern(method.instructions.getFirst(), this.patternPostDispatch, "x??x???xxxxx");
        }
        catch (Exception e) {
            this.log("dispatchPacket", "postNode error: %s\n", e.getMessage());
        }
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_SENDING_PACKET.getParentClass().getRuntimeName(), this.NAMES.ON_SENDING_PACKET.getRuntimeName(), this.NAMES.ON_SENDING_PACKET.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            InsnList insnPost = new InsnList();
            insnPost.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnPost.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_SENT_PACKET.getParentClass().getRuntimeName(), this.NAMES.ON_SENT_PACKET.getRuntimeName(), this.NAMES.ON_SENT_PACKET.getDescriptor(), false));
            insnPost.add((AbstractInsnNode)endJump);
            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insert(postNode, insnPost);
            return true;
        }
        return false;
    }

    private boolean patchChannelRead0(MethodNode method) {
        AbstractInsnNode preNode = null;
        AbstractInsnNode postNode = null;
        try {
            preNode = AsmHelper.findPattern(method.instructions.getFirst(), this.patternPreSend, "xxxx");
        }
        catch (Exception e) {
            this.log("channelRead0", "preNode error: %s\n", e.getMessage());
        }
        try {
            postNode = AsmHelper.findPattern(method.instructions.getFirst(), this.patternPostSend, "x??x");
        }
        catch (Exception e) {
            this.log("channelRead0", "postNode error: %s\n", e.getMessage());
        }
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnPre = new InsnList();
            insnPre.add((AbstractInsnNode)new VarInsnNode(25, 2));
            insnPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_PRE_RECEIVED.getParentClass().getRuntimeName(), this.NAMES.ON_PRE_RECEIVED.getRuntimeName(), this.NAMES.ON_PRE_RECEIVED.getDescriptor(), false));
            insnPre.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            InsnList insnPost = new InsnList();
            insnPost.add((AbstractInsnNode)new VarInsnNode(25, 2));
            insnPost.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_POST_RECEIVED.getParentClass().getRuntimeName(), this.NAMES.ON_POST_RECEIVED.getRuntimeName(), this.NAMES.ON_POST_RECEIVED.getDescriptor(), false));
            insnPost.add((AbstractInsnNode)endJump);
            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insert(postNode, insnPost);
            return true;
        }
        return false;
    }
}

