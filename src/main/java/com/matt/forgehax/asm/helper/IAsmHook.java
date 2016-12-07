/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.MethodNode
 */
package com.matt.forgehax.asm.helper;

import org.objectweb.asm.tree.MethodNode;

public interface IAsmHook {
    public boolean onTransform(MethodNode var1);
}

