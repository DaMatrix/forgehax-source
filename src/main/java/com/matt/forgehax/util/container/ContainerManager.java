/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package com.matt.forgehax.util.container;

import com.google.common.collect.Maps;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.container.ContainerFactories;
import com.matt.forgehax.util.container.ContainerList;
import com.matt.forgehax.util.container.IContainerFactory;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ContainerManager
extends ForgeHaxBase {
    public static final File FILTER_DIR = new File(MOD.getBaseDirectory(), "fitlers");
    public static final Map<Category, CategoryValue> CATEGORIES = Maps.newHashMap();

    public static void initialize() {
        FILTER_DIR.mkdirs();
        for (Map.Entry<Category, CategoryValue> entry : CATEGORIES.entrySet()) {
            entry.getValue().folder.mkdirs();
            ContainerManager.lookupFiles(entry.getKey());
        }
    }

    public static void lookupFiles(Category category) {
        if (CATEGORIES.containsKey((Object)category)) {
            CategoryValue data = CATEGORIES.get((Object)category);
            File[] files = data.folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isFile() || !file.getName().endsWith(".json")) continue;
                    String displayName = file.getName().replace(".json", "");
                    data.files.putIfAbsent(displayName, data.factory.newInstance(displayName, file));
                }
            }
        }
    }

    public static Map<String, Object> getContainer(Category category) {
        if (CATEGORIES.containsKey((Object)category)) {
            CategoryValue value = CATEGORIES.get((Object)category);
            return Maps.newLinkedHashMap(value.files);
        }
        return null;
    }

    public static Collection<?> getContainerCollection(Category category) {
        return ContainerManager.getContainer(category).values();
    }

    public static Object createContainerList(Category category, String displayName) {
        if (CATEGORIES.containsKey((Object)category)) {
            Object newInstance;
            CategoryValue data = CATEGORIES.get((Object)category);
            String fileName = displayName;
            if (!fileName.endsWith(".json")) {
                fileName = fileName + ".json";
            } else {
                displayName = displayName.replace(".json", "");
            }
            if (data.files.containsKey(displayName)) {
                newInstance = data.files.get(displayName);
            } else {
                newInstance = data.factory.newInstance(displayName, new File(data.folder, fileName));
                data.files.put(displayName, newInstance);
            }
            return newInstance;
        }
        return null;
    }

    public static boolean removeContainerList(Category category, String fileName) {
        if (CATEGORIES.containsKey((Object)category)) {
            CategoryValue data = CATEGORIES.get((Object)category);
            if (data.files.containsKey(fileName)) {
                ContainerList list = (ContainerList)data.files.get(fileName);
                data.files.remove(fileName);
                list.delete();
                return true;
            }
        }
        return false;
    }

    public static boolean removeContainerList(Category category, ContainerList list) {
        return ContainerManager.removeContainerList(category, list.getName());
    }

    static {
        CATEGORIES.put(Category.ITEMS, new CategoryValue(new File(FILTER_DIR, Category.ITEMS.name().toLowerCase()), new ContainerFactories.ItemListFactory()));
        CATEGORIES.put(Category.PLAYERS, new CategoryValue(new File(FILTER_DIR, Category.PLAYERS.name().toLowerCase()), new ContainerFactories.PlayerListFactory()));
    }

    private static class CategoryValue {
        public File folder;
        public IContainerFactory factory;
        public Map<String, Object> files;

        public CategoryValue(File file, IContainerFactory factory) {
            this.folder = file;
            this.factory = factory;
            
            Map<Object, Object> map = Collections.synchronizedMap(Maps.newLinkedHashMap());
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                this.files.put((String) key, value);
            }
        }
    }

    public static enum Category {
        ITEMS,
        PLAYERS;
        

        private Category() {
        }
    }

}

