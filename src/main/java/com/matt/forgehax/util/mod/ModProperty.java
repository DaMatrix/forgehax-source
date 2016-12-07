/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.config.Property
 */
package com.matt.forgehax.util.mod;

import net.minecraftforge.common.config.Property;

public class ModProperty {
    public Property property;
    private String lastValue = "";

    public ModProperty(Property property) {
        this.property = property;
        this.update();
    }

    public void update() {
        this.lastValue = this.property.getString();
    }

    public boolean hasChanged() {
        return !this.property.getString().equals(this.lastValue);
    }
}

