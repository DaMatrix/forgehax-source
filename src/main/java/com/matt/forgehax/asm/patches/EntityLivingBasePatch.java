/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.MethodNode
 */
package com.matt.forgehax.asm.patches;

import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.ClassTransformer;
import org.objectweb.asm.tree.MethodNode;

public class EntityLivingBasePatch
extends ClassTransformer {
    private final AsmMethod MOVE_ENTITY_WITH_HEADING = ((AsmMethod)((AsmMethod)new AsmMethod().setName("moveEntityWithHeading")).setObfuscatedName("g")).setArgumentTypes(Float.TYPE, Float.TYPE).setReturnType(Void.TYPE).setHooks(new AsmMethod[0]);

    public EntityLivingBasePatch() {
        this.registerHook(this.MOVE_ENTITY_WITH_HEADING);
    }

    @Override
    public boolean onTransformMethod(MethodNode method) {
        return false;
    }
}

