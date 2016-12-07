/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.util.container;

import com.matt.forgehax.util.container.IContainerFactory;
import com.matt.forgehax.util.container.lists.ItemList;
import com.matt.forgehax.util.container.lists.PlayerList;
import java.io.File;

public class ContainerFactories {

    public static class ItemListFactory
    implements IContainerFactory {
        @Override
        public Object newInstance(String name, File file) {
            return new ItemList(name, file);
        }
    }

    public static class PlayerListFactory
    implements IContainerFactory {
        @Override
        public Object newInstance(String name, File file) {
            return new PlayerList(name, file);
        }
    }

}

