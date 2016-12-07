/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package com.matt.forgehax.mods.debug;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.matt.forgehax.asm.ForgeHaxCoreMod;
import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.mods.ToggleMod;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DebugOutputMod
extends ToggleMod {
    public DebugOutputMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onEnabled() {
        if (ForgeHaxHooks.isInDebugMode) {
            LinkedHashMap report = Maps.newLinkedHashMap();
            for (Map.Entry<String, ForgeHaxHooks.DebugData> entry : ForgeHaxHooks.responding.entrySet()) {
                for (String className : entry.getValue().parentClassNames) {
                    if (entry.getValue().log == null || report.containsKey(className)) continue;
                    report.put(className, Lists.newArrayList(entry.getValue().log));
                }
            }
            ArrayList printReport = Lists.newArrayList();
            for (Object reports : report.values()) {
                printReport.addAll((List) reports);
            }
            printReport.add(0, "##############################\n");
            printReport.add(1, "HOOK REPORT\n");
            printReport.add(2, "______________________________\n");
            printReport.add("##############################\n");
            ForgeHaxCoreMod.print(printReport);
        }
    }

    @Override
    public void onDisabled() {
    }
}

