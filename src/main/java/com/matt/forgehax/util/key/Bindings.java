/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 */
package com.matt.forgehax.util.key;

import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.util.key.KeyBindingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Bindings
extends ForgeHaxBase {
    public static KeyBindingHandler forward = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindForward);
    public static KeyBindingHandler back = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindBack);
    public static KeyBindingHandler left = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindLeft);
    public static KeyBindingHandler right = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindRight);
    public static KeyBindingHandler jump = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindJump);
    public static KeyBindingHandler sprint = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindSprint);
    public static KeyBindingHandler sneak = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindSneak);
    public static KeyBindingHandler attack = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindAttack);
    public static KeyBindingHandler use = new KeyBindingHandler(Bindings.MC.gameSettings.keyBindUseItem);
}

