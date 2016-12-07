/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.registry.RegistryNamespaced
 */
package com.matt.forgehax.util.container.lists;

import com.google.common.collect.Lists;
import com.matt.forgehax.util.container.ContainerList;
import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.RegistryNamespaced;

public class ItemList
extends ContainerList {
    public ItemList(String name, File file) {
        super(name, file);
    }

    public static List<ItemStack> getRegisteredItems() {
        NonNullList<ItemStack> itemList = new BetterNonNullList<ItemStack>();
        for (Item item : Item.REGISTRY) {
            if (item == null) continue;
            item.getSubItems(item, null, itemList);
        }
        return itemList;
    }
}

