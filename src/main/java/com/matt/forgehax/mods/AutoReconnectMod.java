/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiDisconnected
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.GuiConnecting
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.common.config.ConfigCategory
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.event.world.WorldEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.client.FMLClientHandler
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.relauncher.ReflectionHelper
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.mods.ToggleMod;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Logger;

public class AutoReconnectMod
extends ToggleMod {
    private static ServerData lastConnectedServer;
    public Property delayTime;
    public Property delayConnectTime;

    public void updateLastConnectedServer() {
        ServerData data = MC.getCurrentServerData();
        if (data != null) {
            lastConnectedServer = data;
        }
    }

    public AutoReconnectMod(String modName, boolean defaultValue, String description) {
        super(modName, defaultValue, description, -1);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        super.loadConfig(configuration);
        this.delayTime = configuration.get(this.getModCategory().getName(), "delay", 5, "Delay between each connect");
        Property[] arrproperty = new Property[]{this.delayTime, this.delayConnectTime = configuration.get(this.getModCategory().getName(), "delay_connect", 5, "Login delay")};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiDisconnected && !(event.getGui() instanceof GuiDisconnectedOverride)) {
            this.updateLastConnectedServer();
            GuiDisconnected disconnected = (GuiDisconnected)event.getGui();
            //TODO: Comment to remind me to change this for the next update
            GuiScreen parent = (GuiScreen) ObfuscationReflectionHelper.getPrivateValue(GuiDisconnected.class, disconnected, "parentScreen", "field_146307_h");
            ITextComponent message = (ITextComponent) ObfuscationReflectionHelper.getPrivateValue(GuiDisconnected.class, disconnected, "message", "field_146304_f");
            String reason = (String) ObfuscationReflectionHelper.getPrivateValue(GuiDisconnected.class, disconnected, "reason", "field_146306_a");
            event.setGui((GuiScreen)new GuiDisconnectedOverride(parent, "connect.failed", message, reason, this.delayTime.getDouble()));
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.updateLastConnectedServer();
    }

    public static class GuiDisconnectedOverride
    extends GuiDisconnected {
        private GuiScreen parent;
        private ITextComponent message;
        private long reconnectTime;
        private GuiButton reconnectButton = null;

        public GuiDisconnectedOverride(GuiScreen screen, String reasonLocalizationKey, ITextComponent chatComp, String reason, double delay) {
            super(screen, reasonLocalizationKey, chatComp);
            this.parent = screen;
            this.message = chatComp;
            this.reconnectTime = System.currentTimeMillis() + (long)(delay * 1000.0);
            try {
                ReflectionHelper.setPrivateValue(GuiDisconnected.class, this, reason, new String[]{"reason", "field_146306_a", "a"});
            }
            catch (Exception e) {
                ForgeHaxBase.MOD.getLog().error(e.getMessage());
            }
        }

        public long getTimeUntilReconnect() {
            return this.reconnectTime - System.currentTimeMillis();
        }

        public double getTimeUntilReconnectInSeconds() {
            return (double)this.getTimeUntilReconnect() / 1000.0;
        }

        public String getFormattedReconnectText() {
            return String.format("Reconnecting (%.1f)...", this.getTimeUntilReconnectInSeconds());
        }

        public ServerData getLastConnectedServerData() {
            return lastConnectedServer != null ? lastConnectedServer : ForgeHaxBase.MC.getCurrentServerData();
        }

        private void reconnect() {
            ServerData data = this.getLastConnectedServerData();
            if (data != null) {
                FMLClientHandler.instance().showGuiScreen((Object)new GuiConnecting(this.parent, ForgeHaxBase.MC, data));
            }
        }

        public void initGui() {
            super.initGui();
            List multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
            int textHeight = multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
            if (this.getLastConnectedServerData() != null) {
                this.reconnectButton = new GuiButton(this.buttonList.size(), this.width / 2 - 100, this.height / 2 + textHeight / 2 + this.fontRendererObj.FONT_HEIGHT + 23, this.getFormattedReconnectText());
                this.buttonList.add(this.reconnectButton);
            }
        }

        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);
            if (button.equals((Object)this.reconnectButton)) {
                this.reconnect();
            }
        }

        public void updateScreen() {
            super.updateScreen();
            if (this.reconnectButton != null) {
                this.reconnectButton.displayString = this.getFormattedReconnectText();
            }
            if (System.currentTimeMillis() >= this.reconnectTime) {
                this.reconnect();
            }
        }
    }

}

