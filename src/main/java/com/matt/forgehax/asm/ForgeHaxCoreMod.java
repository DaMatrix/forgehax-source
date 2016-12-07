/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.IFMLLoadingPlugin
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.asm;

import com.matt.forgehax.asm.ForgeHaxAccessTransformer;
import com.matt.forgehax.asm.ForgeHaxTransformer;
import java.util.List;
import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.Logger;

public class ForgeHaxCoreMod
implements IFMLLoadingPlugin {
    public static boolean isObfuscated = false;
    public static Logger logger;

    public String[] getASMTransformerClass() {
        return new String[]{ForgeHaxTransformer.class.getName()};
    }

    public String getModContainerClass() {
        return "com.matt.forgehax.asm.ForgeHaxCoreContainer";
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        if (data.containsKey("runtimeDeobfuscationEnabled")) {
            isObfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
        }
    }

    public String getAccessTransformerClass() {
        return ForgeHaxAccessTransformer.class.getName();
    }

    public static /* varargs */ void print(final String message, final Object ... args) {
        if (logger != null) {
            ForgeHaxCoreMod.printf(message, args);
        } else {
            new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        while (ForgeHaxCoreMod.logger == null) {
                            Thread.sleep(1);
                        }
                        ForgeHaxCoreMod.printf(message, args);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void print(List<String> log) {
        StringBuilder builder = new StringBuilder("\n");
        for (String str : log) {
            if (!str.endsWith("\n")) {
                builder.append("\n");
            }
            builder.append(str);
        }
        ForgeHaxCoreMod.print(builder.toString(), new Object[0]);
    }

    public static /* varargs */ void printf(String str, Object ... args) {
        logger.error(String.format(str, args));
    }

}

