/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.util.Session
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.event.FMLInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  net.minecraftforge.fml.relauncher.Side
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax;

import com.google.common.collect.Maps;
import com.matt.forgehax.ForgeHaxConfig;
import com.matt.forgehax.ForgeHaxEventHandler;
import com.matt.forgehax.asm.ForgeHaxHooks;
import com.matt.forgehax.mods.ActiveModListMod;
import com.matt.forgehax.mods.AimbotMod;
import com.matt.forgehax.mods.AntiAfkMod;
import com.matt.forgehax.mods.AntiBatsMod;
import com.matt.forgehax.mods.AntiEffectsMod;
import com.matt.forgehax.mods.AntiFireMod;
import com.matt.forgehax.mods.AntiFogMod;
import com.matt.forgehax.mods.AntiHurtCamMod;
import com.matt.forgehax.mods.AntiKnockbackMod;
import com.matt.forgehax.mods.AntiOverlayMod;
import com.matt.forgehax.mods.AutoBlockCraft;
import com.matt.forgehax.mods.AutoEatMod;
import com.matt.forgehax.mods.AutoFishMod;
import com.matt.forgehax.mods.AutoProjectile;
import com.matt.forgehax.mods.AutoReconnectMod;
import com.matt.forgehax.mods.AutoRespawnMod;
import com.matt.forgehax.mods.AutoSprintMod;
import com.matt.forgehax.mods.AutoWalkMod;
import com.matt.forgehax.mods.BaseMod;
import com.matt.forgehax.mods.BedModeMod;
import com.matt.forgehax.mods.ChamsMod;
import com.matt.forgehax.mods.ChatSpammerMod;
import com.matt.forgehax.mods.DropInvMod;
import com.matt.forgehax.mods.ElytraFlight;
import com.matt.forgehax.mods.EntityEspMod;
import com.matt.forgehax.mods.FastBreak;
import com.matt.forgehax.mods.FastPlaceMod;
import com.matt.forgehax.mods.FlyMod;
import com.matt.forgehax.mods.FreecamMod;
import com.matt.forgehax.mods.FullBrightMod;
import com.matt.forgehax.mods.NoCaveCulling;
import com.matt.forgehax.mods.NoFallMod;
import com.matt.forgehax.mods.NoSlowdown;
import com.matt.forgehax.mods.NoclipMod;
import com.matt.forgehax.mods.ProjectilesMod;
import com.matt.forgehax.mods.SafeWalkMod;
import com.matt.forgehax.mods.SpawnerEspMod;
import com.matt.forgehax.mods.StepMod;
import com.matt.forgehax.mods.StorageESPMod;
import com.matt.forgehax.mods.TeleportMod;
import com.matt.forgehax.mods.XrayMod;
import com.matt.forgehax.mods.YawLockMod;
import com.matt.forgehax.mods.core.ContainersMod;
import com.matt.forgehax.util.LagCompensator;
import com.matt.forgehax.util.container.ContainerManager;
import com.mojang.authlib.GameProfile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Session;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid="forgehax", version="1.0", guiFactory="com.matt.forgehax.ForgeHaxGuiFactory", acceptedMinecraftVersions="[1.11]", name="ForgeHax")
public class ForgeHax {
    public static final String MODID = "forgehax";
    public static final String VERSION = "1.0";
    public static final Minecraft MC = Minecraft.getMinecraft();
    public static final String CONFIG_FILE_NAME = "settings.json";
    private static final boolean isInDevMode = ForgeHaxHooks.isInDebugMode;
    public static ForgeHax INSTANCE;
    private File baseFolder;
    private File configFolder;
    private ForgeHaxConfig config;
    public Logger log;
    public Map<String, BaseMod> mods = Maps.newLinkedHashMap();
    public boolean newProfile = false;

    public static ForgeHax instance() {
        return INSTANCE;
    }

    public ForgeHax() {
        INSTANCE = this;
    }

    public Logger getLog() {
        return this.log;
    }

    public File getBaseDirectory() {
        return this.baseFolder;
    }

    public File getConfigFolder() {
        return this.configFolder;
    }

    public ForgeHaxConfig getConfig() {
        return this.config;
    }

    public void setupConfigFolder() {
        File userDir = new File(this.getBaseDirectory(), "users");
        userDir.mkdirs();
        if (!isInDevMode) {
            this.configFolder = new File(userDir, MC.getSession().getProfile().getId().toString());
            if (!this.configFolder.exists()) {
                this.newProfile = true;
                this.configFolder.mkdirs();
            }
        } else {
            this.configFolder = new File(userDir, "devmode");
        }
    }

    public void registerMod(BaseMod mod) {
        this.mods.put(mod.getModName(), mod);
    }

    private File getLastProfileFile() {
        return new File(this.getBaseDirectory(), "last_profile.txt");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private File getLastUsedProfileFolder() {
        File profile = this.getLastProfileFile();
        if (profile.exists()) {
            FileReader reader = null;
            BufferedReader buffer = null;
            try {
                reader = new FileReader(profile);
                buffer = new BufferedReader(reader);
                String profileName = buffer.readLine();
                for (File file : this.getBaseDirectory().listFiles()) {
                    if (!file.getName().equals(profileName) || !file.isDirectory()) continue;
                    File file2 = file;
                    buffer.close();
                    return file2;
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (buffer != null) {
                        buffer.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void updateLastUsedProfile(File lastUsedProfile) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(this.getLastProfileFile());
            output.write(lastUsedProfile.getName().getBytes());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (output != null) {
                    output.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isNewProfile() {
        return this.newProfile;
    }

    public void printStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        this.getLog().error((Object)sw);
        if (ForgeHax.MC.thePlayer != null) {
            ForgeHax.MC.thePlayer.sendChatMessage("ERROR: " + exception.getMessage());
        }
    }
    @Mod.EventHandler
    public void construct(FMLConstructionEvent e) throws NoSuchFieldException, SecurityException	{
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        switch (event.getSide()) {
            case CLIENT: {
                this.log = event.getModLog();
                this.registerMod(new ContainersMod("Containers", "Mod containers for xray and entity lists"));
                if (isInDevMode) {
                    // empty if block
                }
                this.registerMod(new ActiveModListMod("activemods", true, "Shows list of all active mods", 207));
                this.registerMod(new AimbotMod("aimbot", true, "Auto aim/attack entities", 207));
                this.registerMod(new AntiAfkMod("antiafk", false, "Moves automatically to prevent being kicked", 207));
                this.registerMod(new AntiBatsMod("antibats", true, "666 KILL BATS 666", 207));
                this.registerMod(new AntiEffectsMod("antipotioneffects", true, "Removes potion effects", 207));
                this.registerMod(new AntiFireMod("antifire", true, "Removes fire", 207));
                this.registerMod(new AntiFogMod("antifog", true, "Removes fog", 207));
                this.registerMod(new AntiHurtCamMod("antihurtcam", true, "Removes hurt camera effect", 207));
                this.registerMod(new AntiKnockbackMod("antiknockback", true, "Removes knockback movement", 207));
                this.registerMod(new AntiOverlayMod("antioverlay", true, "Removes screen overlays", 207));
                this.registerMod(new AutoBlockCraft("autoblockcraft", true, "Automatically crafts blocks for you", 207));
                this.registerMod(new AutoEatMod("autoeat", true, "Auto eats when you get hungry", 207));
                this.registerMod(new AutoFishMod("autofish", false, "Auto fish", 207));
                this.registerMod(new AutoProjectile("autoprojectile", true, "Automatically sets pitch to best trajectory", 207));
                this.registerMod(new AutoReconnectMod("autoreconnect", true, "Automatically reconnects to server"));
                this.registerMod(new AutoRespawnMod("autorespawn", true, "Auto respawn on death", 207));
                this.registerMod(new AutoSprintMod("autosprint", false, "Automatically sprints", 207));
                this.registerMod(new AutoWalkMod("autowalk", false, "Automatically walks forward", 207));
                this.registerMod(new BedModeMod("bedmode", true, "Sleep walking", 207));
                this.registerMod(new ChamsMod("chams", true, "Render living models behind walls", 207));
                this.registerMod(new ChatSpammerMod("chatspammer", false, "Add lines of spam into forgehax/spam.txt", 207));
                this.registerMod(new StorageESPMod("storageesp", true, "Shows storage", 207));
                this.registerMod(new EntityEspMod("entityesp", true, "Shows entity locations and info", 207));
                this.registerMod(new FastBreak("fastbreak", true, "Fast break retard", 207));
                this.registerMod(new FastPlaceMod("fastplace", true, "Fast place", 207));
                this.registerMod(new FlyMod("fly", false, "Enables flying", 207));
                this.registerMod(new FreecamMod("freecam", false, "Freecam mode", 207));
                this.registerMod(new FullBrightMod("fullbright", true, "Makes everything render with maximum brightness", 207));
                this.registerMod(new NoCaveCulling("nocaveculling", false, "Disables mojangs dumb cave culling shit", 207));
                this.registerMod(new NoclipMod("noclip", false, "Enables player noclip", 207));
                this.registerMod(new NoFallMod("nofall", false, "Prevents fall damage from being taken", 207));
                this.registerMod(new NoSlowdown("noslowdown", true, "Disables block slowdown", 207));
                this.registerMod(new ProjectilesMod("projectiles", true, "Draws projectile path", 207));
                this.registerMod(new SafeWalkMod("safewalk", false, "Prevents you from falling off blocks", 207));
                this.registerMod(new SpawnerEspMod("spawneresp", true, "Spawner esp", 207));
                this.registerMod(new StepMod("step", true, "Step up blocks", 207));
                this.registerMod(new TeleportMod("teleport", true, "Type '.setpos [x] [y] [z] [onGround]' in chat to use", 207));
                this.registerMod(new XrayMod("xray", true, "See blocks through walls", 207));
                this.registerMod(new YawLockMod("yawlock", false, "Locks yaw to prevent moving into walls", 207));
                this.registerMod(new ElytraFlight("elytraflight", false, "Elytra Flight", 207));
                this.registerMod(new DropInvMod("dropinvmod", true, "hax", 207));
                this.baseFolder = new File(event.getModConfigurationDirectory(), "forgehax");
                this.baseFolder.mkdirs();
                this.setupConfigFolder();
                this.config = new ForgeHaxConfig(new File(this.getConfigFolder(), "settings.json"));
                ContainerManager.initialize();
            }
		default:
			break;
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        switch (event.getSide()) {
            case CLIENT: {
                MinecraftForge.EVENT_BUS.register((Object)this.config);
                MinecraftForge.EVENT_BUS.register((Object)new ForgeHaxEventHandler());
                MinecraftForge.EVENT_BUS.register((Object)LagCompensator.getInstance().getEventHandler());
                for (Map.Entry<String, BaseMod> entry : this.mods.entrySet()) {
                    if (!entry.getValue().isEnabled()) continue;
                    entry.getValue().onEnabled();
                    entry.getValue().register();
                }
            }
		default:
			break;
        }
    }
    public static int getStackSizeFromItemStack(ItemStack stack)	{
    	return ObfuscationReflectionHelper.getPrivateValue(ItemStack.class, stack, "stackSize", "field_77994_a");
    }
    
    public static <E> boolean callPrivateMethod(Class clazz, E obj, Object[] args, String... methodNames)	{
    	for (String name : methodNames)	{
    		try {
				Method m = clazz.getMethod(name);
				m.setAccessible(true);
				m.invoke(obj, args);
				return true;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
    	}
    	return false;
    }
}

