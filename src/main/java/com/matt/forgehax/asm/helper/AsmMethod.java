/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.asm.helper;

import com.matt.forgehax.asm.helper.AsmClass;
import com.matt.forgehax.asm.helper.AsmHelper;
import com.matt.forgehax.asm.helper.AsmType;

public class AsmMethod
extends AsmType<AsmMethod> {
    private AsmMethod[] hooks = new AsmMethod[0];
    private AsmClass parentClass;
    private Object[] argumentTypes;
    private Object returnType = Void.TYPE;

    public /* varargs */ AsmMethod setHooks(AsmMethod ... hooks) {
        this.hooks = hooks;
        return this;
    }

    public AsmMethod[] getHooks() {
        return this.hooks;
    }

    public /* varargs */ AsmMethod setArgumentTypes(Object ... types) {
        this.argumentTypes = types;
        return this;
    }

    public AsmMethod setReturnType(Object type) {
        this.returnType = type;
        return this;
    }

    public String getDescriptor() {
        StringBuilder builder = new StringBuilder("");
        if (this.argumentTypes != null) {
            for (Object var : this.argumentTypes) {
                builder.append(AsmHelper.objectToDescriptor(var));
            }
        }
        return String.format("(%s)%s", builder.toString(), AsmHelper.objectToDescriptor(this.returnType));
    }

    public AsmMethod setParentClass(AsmClass clazz) {
        this.parentClass = clazz;
        return this;
    }

    public AsmClass getParentClass() {
        return this.parentClass;
    }

    public String toString() {
        return String.format("%s%s", this.getName(), this.getDescriptor());
    }
}

