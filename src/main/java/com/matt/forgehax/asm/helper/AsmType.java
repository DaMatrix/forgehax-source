/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.asm.helper;

import com.matt.forgehax.asm.ForgeHaxCoreMod;

public abstract class AsmType<E extends AsmType> {
    protected String realName = "";
    protected String obfuscatedName = "";

    public String getName() {
        return this.realName;
    }

    public E setName(String realName) {
        this.realName = realName;
        return (E)this;
    }

    public String getObfuscatedName() {
        return this.obfuscatedName;
    }

    public E setObfuscatedName(String obfuscatedName) {
        this.obfuscatedName = obfuscatedName;
        return (E)this;
    }

    public String getRuntimeName() {
        return ForgeHaxCoreMod.isObfuscated && !this.obfuscatedName.isEmpty() ? this.obfuscatedName : this.realName;
    }
}

