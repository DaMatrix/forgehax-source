/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.common.config.Property$Type
 */
package com.matt.forgehax.util.mod;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraftforge.common.config.Property;

public class ListProperties {
    private static final Property NULL_PROPERTY = new Property("<null>", Boolean.toString(false), Property.Type.BOOLEAN);
    private final List<Property> properties = Lists.newArrayList();

    public Property getProperty(String name) {
        for (Property property : this.properties) {
            if (!property.getName().toLowerCase().equals(name.toLowerCase())) continue;
            return property;
        }
        return NULL_PROPERTY;
    }
}

