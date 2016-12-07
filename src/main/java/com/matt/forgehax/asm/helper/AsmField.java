/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.asm.helper;

import com.matt.forgehax.asm.helper.AsmClass;
import com.matt.forgehax.asm.helper.AsmHelper;
import com.matt.forgehax.asm.helper.AsmType;

public class AsmField
extends AsmType<AsmField> {
    private AsmClass parentClass;
    private Object type;

    public String getTypeDescriptor() {
        return AsmHelper.objectToDescriptor(this.type);
    }

    public AsmField setType(Object o) {
        this.type = o;
        return this;
    }

    public AsmClass getParentClass() {
        return this.parentClass;
    }

    public AsmField setParentClass(AsmClass parentClass) {
        this.parentClass = parentClass;
        return this;
    }

    public String toString() {
        return String.format("%s %s", this.getTypeDescriptor(), this.getName());
    }
}

