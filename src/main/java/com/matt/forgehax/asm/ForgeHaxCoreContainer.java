/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.eventbus.EventBus
 *  net.minecraftforge.fml.common.DummyModContainer
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  net.minecraftforge.fml.common.LoadController
 *  net.minecraftforge.fml.common.ModContainer
 *  net.minecraftforge.fml.common.ModMetadata
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.asm;

import com.google.common.eventbus.EventBus;
import com.matt.forgehax.asm.ForgeHaxCoreMod;
import java.util.Arrays;
import java.util.List;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeHaxCoreContainer
extends DummyModContainer {
    public ForgeHaxCoreContainer() {
        super(new ModMetadata());
        ModMetadata metaData = super.getMetadata();
        metaData.authorList = Arrays.asList("fr1kin");
        metaData.description = "CoreMod for ForgeHax.";
        metaData.modId = "forgehaxcore";
        metaData.version = "1.0";
        metaData.name = "ForgeHax Core";
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register((Object)this);
        ForgeHaxCoreMod.logger = LogManager.getLogger((Object)FMLCommonHandler.instance().findContainerFor((Object)this));
        return true;
    }
}

