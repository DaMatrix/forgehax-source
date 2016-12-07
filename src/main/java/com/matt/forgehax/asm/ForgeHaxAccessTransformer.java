/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.asm.transformers.AccessTransformer
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin$SortingIndex
 */
package com.matt.forgehax.asm;

import java.io.IOException;
import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(value=1000)
public class ForgeHaxAccessTransformer
extends AccessTransformer {
    public ForgeHaxAccessTransformer() throws IOException {
        super("forgehax_at.cfg");
    }
}

