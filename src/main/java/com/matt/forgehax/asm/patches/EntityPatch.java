/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.FieldInsnNode
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
import com.matt.forgehax.asm.helper.AsmField;
import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.ClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityPatch
extends ClassTransformer {
    public final AsmMethod APPLY_ENTITY_COLLISION;
    public final AsmMethod MOVE_ENTITY;
    public final AsmMethod DO_APPLY_COLLISIONS;
    private final int[] applyPushToThisPreNode;
    private final int[] applyPushToThisPostNode;
    private final int[] applyPushToOtherPreNode;
    private final int[] applyPushToOtherPostNode;
    private final int[] moveEntityMotionPreSig;
    private final int[] moveEntityMotionPostSig;
    private final int[] isPlayerSneakingSig;
    private final int[] doBlockCollisionsPreSig;
    private final int[] doBlockCollisionsPostSig;

    public EntityPatch() {
        this.APPLY_ENTITY_COLLISION = ((AsmMethod)((AsmMethod)new AsmMethod().setName("applyEntityCollision")).setObfuscatedName("i")).setArgumentTypes(this.NAMES.ENTITY).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_APPLY_COLLISION);
        this.MOVE_ENTITY = ((AsmMethod)((AsmMethod)new AsmMethod().setName("moveEntity")).setObfuscatedName("d")).setArgumentTypes(Double.TYPE, Double.TYPE, Double.TYPE).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_WEB_MOTION);
        this.DO_APPLY_COLLISIONS = ((AsmMethod)((AsmMethod)new AsmMethod().setName("doBlockCollisions")).setObfuscatedName("ac")).setArgumentTypes(new Object[0]).setReturnType(Void.TYPE).setHooks(this.NAMES.ON_DO_BLOCK_COLLISIONS);
        this.applyPushToThisPreNode = new int[]{25, 24, 119, 14, 24, 119, 182};
        this.applyPushToThisPostNode = new int[]{182};
        this.applyPushToOtherPreNode = new int[]{25, 24, 14, 24, 182};
        this.applyPushToOtherPostNode = new int[]{182};
        this.moveEntityMotionPreSig = new int[]{24, 18, 107, 57, 0, 0, 24, 18, 107, 57};
        this.moveEntityMotionPostSig = new int[]{181, 0, 0, 0, 24, 57, 0, 0, 24, 57};
        this.isPlayerSneakingSig = new int[]{153, 25, 193, 153, 4, 167, 0, 0, 3};
        this.doBlockCollisionsPreSig = new int[]{25, 185, 25, 180, 25, 25, 25, 182, 0, 0, 167};
        this.doBlockCollisionsPostSig = new int[]{182, 0, 0, 167, 0, 0, 0, 58, 0, 0, 25, 18, 184, 58, 0, 0, 25, 18, 182, 58};
        this.registerHook(this.APPLY_ENTITY_COLLISION);
        this.registerHook(this.MOVE_ENTITY);
        this.registerHook(this.DO_APPLY_COLLISIONS);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        if (method.name.equals(this.APPLY_ENTITY_COLLISION.getRuntimeName()) && method.desc.equals(this.APPLY_ENTITY_COLLISION.getDescriptor())) {
            this.updatePatchedMethods(this.applyEntityCollisionPatch(method));
            return true;
        }
        if (method.name.equals(this.MOVE_ENTITY.getRuntimeName()) && method.desc.equals(this.MOVE_ENTITY.getDescriptor())) {
            this.updatePatchedMethods(this.applyMoveEntityPatch(method));
            return true;
        }
        if (method.name.equals(this.DO_APPLY_COLLISIONS.getRuntimeName()) && method.desc.equals(this.DO_APPLY_COLLISIONS.getDescriptor())) {
            this.updatePatchedMethods(this.doBlockCollisionsPatch(method));
            return true;
        }
        return false;
    }

    private boolean applyEntityCollisionPatch(MethodNode method) {
        AbstractInsnNode thisEntityPreNode = this.findPattern("applyEntityCollision", "thisEntityPreNode", method.instructions.getFirst(), this.applyPushToThisPreNode, "xxxxxxx");
        AbstractInsnNode thisEntityPostNode = this.findPattern("applyEntityCollision", "thisEntityPostNode", thisEntityPreNode, this.applyPushToThisPostNode, "x");
        AbstractInsnNode otherEntityPreNode = this.findPattern("applyEntityCollision", "otherEntityPreNode", thisEntityPostNode, this.applyPushToOtherPreNode, "xxxxx");
        AbstractInsnNode otherEntityPostNode = this.findPattern("applyEntityCollision", "otherEntityPostNode", otherEntityPreNode, this.applyPushToOtherPostNode, "x");
        if (thisEntityPostNode != null && thisEntityPreNode != null && otherEntityPostNode != null && otherEntityPreNode != null) {
            LabelNode endJumpForThis = new LabelNode();
            LabelNode endJumpForOther = new LabelNode();
            InsnList insnThisPre = new InsnList();
            insnThisPre.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnThisPre.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnThisPre.add((AbstractInsnNode)new VarInsnNode(24, 2));
            insnThisPre.add((AbstractInsnNode)new InsnNode(119));
            insnThisPre.add((AbstractInsnNode)new VarInsnNode(24, 4));
            insnThisPre.add((AbstractInsnNode)new InsnNode(119));
            insnThisPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_APPLY_COLLISION.getParentClass().getRuntimeName(), this.NAMES.ON_APPLY_COLLISION.getRuntimeName(), this.NAMES.ON_APPLY_COLLISION.getDescriptor(), false));
            insnThisPre.add((AbstractInsnNode)new JumpInsnNode(154, endJumpForThis));
            InsnList insnOtherPre = new InsnList();
            insnOtherPre.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insnOtherPre.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnOtherPre.add((AbstractInsnNode)new VarInsnNode(24, 2));
            insnOtherPre.add((AbstractInsnNode)new VarInsnNode(24, 4));
            insnOtherPre.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_APPLY_COLLISION.getParentClass().getRuntimeName(), this.NAMES.ON_APPLY_COLLISION.getRuntimeName(), this.NAMES.ON_APPLY_COLLISION.getDescriptor(), false));
            insnOtherPre.add((AbstractInsnNode)new JumpInsnNode(154, endJumpForOther));
            method.instructions.insertBefore(thisEntityPreNode, insnThisPre);
            method.instructions.insert(thisEntityPostNode, (AbstractInsnNode)endJumpForThis);
            method.instructions.insertBefore(otherEntityPreNode, insnOtherPre);
            method.instructions.insert(otherEntityPostNode, (AbstractInsnNode)endJumpForOther);
            return true;
        }
        return false;
    }

    private boolean applyMoveEntityPatch(MethodNode method) {
        AbstractInsnNode sneakFlagNode;
        boolean isPatched = false;
        AbstractInsnNode preNode = this.findPattern("moveEntity", "preNode", method.instructions.getFirst(), this.moveEntityMotionPreSig, "xxxx??xxxx");
        AbstractInsnNode postNode = this.findPattern("moveEntity", "postNode", method.instructions.getFirst(), this.moveEntityMotionPostSig, "x???xx??xx");
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            int identifier = 59;
            InsnList insnList = new InsnList();
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnList.add((AbstractInsnNode)new VarInsnNode(24, 1));
            insnList.add((AbstractInsnNode)new VarInsnNode(24, 3));
            insnList.add((AbstractInsnNode)new VarInsnNode(24, 5));
            insnList.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_WEB_MOTION.getParentClass().getRuntimeName(), this.NAMES.ON_WEB_MOTION.getRuntimeName(), this.NAMES.ON_WEB_MOTION.getDescriptor(), false));
            insnList.add((AbstractInsnNode)new VarInsnNode(58, identifier));
            insnList.add((AbstractInsnNode)new LabelNode());
            insnList.add((AbstractInsnNode)new VarInsnNode(25, identifier));
            insnList.add((AbstractInsnNode)new MethodInsnNode(182, this.NAMES.WEB_MOTION_EVENT.getRuntimeName(), "getX", "()D", false));
            insnList.add((AbstractInsnNode)new VarInsnNode(57, 1));
            insnList.add((AbstractInsnNode)new LabelNode());
            insnList.add((AbstractInsnNode)new VarInsnNode(25, identifier));
            insnList.add((AbstractInsnNode)new MethodInsnNode(182, this.NAMES.WEB_MOTION_EVENT.getRuntimeName(), "getY", "()D", false));
            insnList.add((AbstractInsnNode)new VarInsnNode(57, 3));
            insnList.add((AbstractInsnNode)new LabelNode());
            insnList.add((AbstractInsnNode)new VarInsnNode(25, identifier));
            insnList.add((AbstractInsnNode)new MethodInsnNode(182, this.NAMES.WEB_MOTION_EVENT.getRuntimeName(), "getZ", "()D", false));
            insnList.add((AbstractInsnNode)new VarInsnNode(57, 5));
            insnList.add((AbstractInsnNode)new LabelNode());
            insnList.add((AbstractInsnNode)new VarInsnNode(25, identifier));
            insnList.add((AbstractInsnNode)new MethodInsnNode(182, this.NAMES.WEB_MOTION_EVENT.getRuntimeName(), "isCanceled", "()Z", false));
            insnList.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            InsnList pop = new InsnList();
            pop.insert((AbstractInsnNode)endJump);
            method.instructions.insertBefore(preNode, insnList);
            method.instructions.insert(postNode, pop);
            isPatched = true;
        }
        if ((sneakFlagNode = this.findPattern("moveEntity", "sneakFlagNode", method.instructions.getFirst(), this.isPlayerSneakingSig, "xxxxxx??x")) != null && sneakFlagNode instanceof JumpInsnNode) {
            LabelNode jumpToLabel = ((JumpInsnNode)sneakFlagNode).label;
            LabelNode orJump = new LabelNode();
            InsnList insnList = new InsnList();
            insnList.add((AbstractInsnNode)new JumpInsnNode(154, orJump));
            insnList.add((AbstractInsnNode)new FieldInsnNode(178, this.NAMES.IS_SAFEWALK_ACTIVE.getParentClass().getRuntimeName(), this.NAMES.IS_SAFEWALK_ACTIVE.getRuntimeName(), this.NAMES.IS_SAFEWALK_ACTIVE.getTypeDescriptor()));
            insnList.add((AbstractInsnNode)new JumpInsnNode(153, jumpToLabel));
            insnList.add((AbstractInsnNode)orJump);
            AbstractInsnNode previousNode = sneakFlagNode.getPrevious();
            method.instructions.remove(sneakFlagNode);
            method.instructions.insert(previousNode, insnList);
            isPatched &= true;
        }
        return isPatched;
    }

    private boolean doBlockCollisionsPatch(MethodNode method) {
        AbstractInsnNode preNode = this.findPattern("doBlockCollisions", "preNode", method.instructions.getFirst(), this.doBlockCollisionsPreSig, "xxxxxxxx??x");
        AbstractInsnNode postNode = this.findPattern("doBlockCollisions", "postNode", method.instructions.getFirst(), this.doBlockCollisionsPostSig, "x??x???x??xxxx??xxxx");
        if (preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();
            InsnList insnList = new InsnList();
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 4));
            insnList.add((AbstractInsnNode)new VarInsnNode(25, 8));
            insnList.add((AbstractInsnNode)new MethodInsnNode(184, this.NAMES.ON_DO_BLOCK_COLLISIONS.getParentClass().getRuntimeName(), this.NAMES.ON_DO_BLOCK_COLLISIONS.getRuntimeName(), this.NAMES.ON_DO_BLOCK_COLLISIONS.getDescriptor(), false));
            insnList.add((AbstractInsnNode)new JumpInsnNode(154, endJump));
            method.instructions.insertBefore(preNode, insnList);
            method.instructions.insert(postNode, (AbstractInsnNode)endJump);
            return true;
        }
        return false;
    }
}

