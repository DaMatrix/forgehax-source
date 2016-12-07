/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.login.client.CPacketEncryptionResponse
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.event.world.WorldEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.mods.net.ClientToServer;
import com.matt.forgehax.mods.net.IServerCallback;
import com.matt.forgehax.mods.net.Server;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.Logger;

public class DropInvMod
extends ToggleMod
implements IServerCallback {
    private static final int STARTING_PORT = 4044;
    private Server server;
    private ClientToServer clientToServer;
    private boolean isConnectedToServer = false;
    private boolean hasCheckedPos = false;
    private boolean isInQueueServer = true;
    private boolean connectToServer = false;
    public Property time;
    public Property connectDelay;
    private ServerData lastConnectedServer = null;

    public DropInvMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
        this.initializeServer();
    }

    @Override
    public void onEnabled() {
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.time = configuration.get(this.getModName(), "time", 50, "Thread sleep in ms");
        Property[] arrproperty = new Property[]{this.time, this.connectDelay = configuration.get(this.getModName(), "connect_delay", 100, "Connect delay in ms")};
        this.addSettings(arrproperty);
    }

    public void initializeServer() {
        int port = Server.findOpenPort(4044, 4054);
        if (port == -1) {
            MOD.getLog().warn("Failed to find open port");
            return;
        }
        MOD.getLog().info(String.format("Found open port at '%d'", port));
        this.server = new Server(port, this);
        this.server.startServerThreaded();
        int talkingPort = Server.getTalkPort(port);
        MOD.getLog().info(String.format("Talking to port '%d'", talkingPort));
        this.clientToServer = new ClientToServer(talkingPort);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.clientToServer.sendConnectedMessage();
        this.lastConnectedServer = MC.getCurrentServerData();
        this.hasCheckedPos = false;
        this.connectToServer = false;
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.isConnectedToServer = false;
        this.hasCheckedPos = false;
        this.isInQueueServer = true;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send.Pre event) {
        if (event.getPacket() instanceof CPacketEncryptionResponse) {
            this.clientToServer.sendDisconnectMessage();
            MOD.getLog().info("Sent disconnect msg");
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (DropInvMod.MC.gameSettings.keyBindDrop.isPressed()) {
            for (int i = 0; i < 50; ++i) {
                DropInvMod.MC.thePlayer.connection.sendPacket((Packet)new CPacketChatMessage("\u00ef\u00bf\u00bd"));
            }
            new Thread(new Runnable(){

                @Override
                public void run() {
                    try {
                        Thread.sleep(DropInvMod.this.connectDelay.getLong());
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 45; i >= 9; --i) {
                        ForgeHaxBase.MC.playerController.windowClick(0, i, 1, ClickType.THROW, (EntityPlayer)ForgeHaxBase.MC.thePlayer);
                    }
                }
            }).start();
        }
    }

    @Override
    public void onConnecting() {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(DropInvMod.this.time.getLong());
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ForgeHaxBase.MC.thePlayer != null) {
                    for (int i = 36; i < 45; ++i) {
                        ForgeHaxBase.MC.playerController.windowClick(0, i, 1, ClickType.THROW, (EntityPlayer)ForgeHaxBase.MC.thePlayer);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClientConnected() {
    }

}

