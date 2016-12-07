/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.asm.helper;

import com.matt.forgehax.asm.helper.AsmField;
import com.matt.forgehax.asm.helper.AsmMethod;
import com.matt.forgehax.asm.helper.AsmType;

public class AsmClass
extends AsmType<AsmClass> {
    public AsmMethod childMethod() {
        return new AsmMethod().setParentClass(this);
    }

    public AsmField childField() {
        return new AsmField().setParentClass(this);
    }

    public String toString() {
        return this.getName();
    }
}

