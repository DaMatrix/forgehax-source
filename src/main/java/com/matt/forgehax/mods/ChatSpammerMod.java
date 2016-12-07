/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 */
package com.matt.forgehax.mods;

import com.google.common.collect.Lists;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.mods.ToggleMod;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ChatSpammerMod
extends ToggleMod {
    public Property delay;
    private int index = 0;
    private long timeLastMessageSent = -1;
    private final List<String> spamList = Lists.newCopyOnWriteArrayList();

    public ChatSpammerMod(String modName, boolean defaultValue, String description, int key) {
        super(modName, defaultValue, description, key);
    }

    private void parseSpamFile(File file) {
        if (!file.exists() || !file.isFile()) {
            return;
        }
        this.spamList.clear();
        try {
            Scanner scanner = new Scanner(new FileReader(file));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String msg = scanner.next();
                if (msg.length() > 240) {
                    msg = msg.substring(0, 239);
                }
                this.spamList.add(msg);
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnabled() {
        this.index = 0;
        this.timeLastMessageSent = -1;
        File spamFile = new File(MOD.getBaseDirectory(), "spam.txt");
        this.parseSpamFile(spamFile);
    }

    @Override
    public void loadConfig(Configuration configuration) {
        this.delay = configuration.get(this.getModName(), "delay", 5000, "Delay between messages in ms");
        Property[] arrproperty = new Property[]{this.delay};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (ChatSpammerMod.MC.thePlayer == null) {
            return;
        }
        switch (event.phase) {
            case START: {
                break;
            }
            case END: {
                if (System.currentTimeMillis() < this.timeLastMessageSent + (long)this.delay.getInt()) break;
                try {
                    ChatSpammerMod.MC.thePlayer.sendChatMessage(this.spamList.get(this.index % this.spamList.size()));
                    this.timeLastMessageSent = System.currentTimeMillis();
                } catch (ArithmeticException e)	{
                	ChatSpammerMod.MC.thePlayer.addChatMessage(new TextComponentString("You need to edit config/spam.txt"));
                }
                catch (Exception e) {
                    MOD.printStackTrace(e);
                }
                finally {
                    ++this.index;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send.Pre event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            this.timeLastMessageSent = System.currentTimeMillis();
        }
    }

}

