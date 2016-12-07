/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityPigZombie
 *  net.minecraft.network.Packet
 *  net.minecraft.util.Session
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.IWorldEventListener
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.LivingEvent
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.event.world.WorldEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 */
package com.matt.forgehax;

import com.google.common.collect.Maps;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import com.matt.forgehax.asm.events.PacketEvent;
import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.events.listeners.WorldListener;
import com.matt.forgehax.mods.BaseMod;
import com.matt.forgehax.util.Utils;
import com.mojang.authlib.GameProfile;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.network.Packet;
import net.minecraft.util.Session;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ForgeHaxEventHandler
extends ForgeHaxBase {
    private static final WorldListener WORLD_LISTENER = new WorldListener();
    private boolean isLoaded = false;

    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (ForgeHaxEventHandler.MC.theWorld != null && event.getEntityLiving().equals((Object)ForgeHaxEventHandler.MC.thePlayer)) {
            LocalPlayerUpdateEvent ev = new LocalPlayerUpdateEvent(event.getEntityLiving());
            MinecraftForge.EVENT_BUS.post((Event)ev);
            event.setCanceled(ev.isCanceled());
        } else if (event.getEntityLiving() instanceof EntityPigZombie && ((EntityPigZombie)event.getEntityLiving()).isAngry()) {
        	//TODO: Comment to remind me to change this for the next update
        	ObfuscationReflectionHelper.setPrivateValue(EntityPigZombie.class, (EntityPigZombie) event.getEntityLiving(), (Integer) (
        			ObfuscationReflectionHelper.getPrivateValue(EntityPigZombie.class, (EntityPigZombie) event.getEntityLiving(), "angerLevel", "field_70837_d")
        			), "angerLevel", "field_70837_d");
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        event.getWorld().addEventListener((IWorldEventListener)WORLD_LISTENER);
        this.isLoaded = true;
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        this.isLoaded = false;
    }

    @SubscribeEvent
    public void onKeyboardEvent(InputEvent.KeyInputEvent event) {
        for (Map.Entry<String, BaseMod> entry : ForgeHaxEventHandler.MOD.mods.entrySet()) {
            for (KeyBinding bind : entry.getValue().getKeyBinds()) {
                if (bind.isPressed()) {
                    entry.getValue().onBindPressed(bind);
                }
                if (!bind.isKeyDown()) continue;
                entry.getValue().onBindKeyDown(bind);
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onSentPacket(PacketEvent.Send.Post event) {
        if (Utils.OUTGOING_PACKET_IGNORE_LIST.contains((Object)event.getPacket())) {
            Utils.OUTGOING_PACKET_IGNORE_LIST.remove((Object)event.getPacket());
        }
    }

}

