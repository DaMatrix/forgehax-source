/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.mods.ToggleMod;

public class SafeWalkMod
extends ToggleMod {
    public SafeWalkMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    @Override
    public void onEnabled() {
        ForgeHaxHooks.isSafeWalkActivated = true;
    }

    @Override
    public void onDisabled() {
        ForgeHaxHooks.isSafeWalkActivated = false;
    }
}

