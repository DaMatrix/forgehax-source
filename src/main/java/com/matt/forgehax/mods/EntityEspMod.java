/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderLivingEvent
 *  net.minecraftforge.client.event.RenderLivingEvent$Specials
 *  net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.google.common.collect.Lists;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.draw.SurfaceUtils;
import com.matt.forgehax.util.entity.EnchantmentUtils;
import com.matt.forgehax.util.entity.EntityUtils;
import com.matt.forgehax.util.entity.LocalPlayerUtils;
import com.matt.forgehax.util.math.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEspMod
extends ToggleMod {
    private static final int HEALTHBAR_WIDTH = 15;
    private static final int HEALTHBAR_HEIGHT = 3;
    public Property players;
    public Property hostileMobs;
    public Property friendlyMobs;
    public Property armorEsp;
    public Property distanceEsp;

    public EntityEspMod(String categoryName, boolean defaultValue, String description, int key) {
        super(categoryName, defaultValue, description, key);
    }

    public DrawOptions getDrawOptionValue(Property prop) {
        for (DrawOptions option : DrawOptions.values()) {
            if (!prop.getString().equals(option.name())) continue;
            return option;
        }
        return null;
    }

    public DrawOptions getDrawOptionValue(EntityLivingBase living) {
        if (EntityUtils.isPlayer((Entity)living)) {
            return this.getDrawOptionValue(this.players);
        }
        if (EntityUtils.isHostileMob((Entity)living)) {
            return this.getDrawOptionValue(this.hostileMobs);
        }
        if (EntityUtils.isFriendlyMob((Entity)living)) {
            return this.getDrawOptionValue(this.friendlyMobs);
        }
        return null;
    }

    public boolean isDrawOptionPropertyEnabled(Property prop) {
        return this.getDrawOptionValue(prop) != null && !this.getDrawOptionValue(prop).equals((Object)DrawOptions.DISABLED);
    }

    public ArmorOptions getArmorOptionValue(Property prop) {
        for (ArmorOptions option : ArmorOptions.values()) {
            if (!prop.getString().equals(option.name())) continue;
            return option;
        }
        return null;
    }

    public boolean shouldDraw(EntityLivingBase entity) {
        return LocalPlayerUtils.isTargetEntity((Entity)entity) || !entity.equals((Object)EntityEspMod.MC.thePlayer) && EntityUtils.isAlive((Entity)entity) && EntityUtils.isValidEntity((Entity)entity) && (this.isDrawOptionPropertyEnabled(this.hostileMobs) && EntityUtils.isHostileMob((Entity)entity) || this.isDrawOptionPropertyEnabled(this.players) && EntityUtils.isPlayer((Entity)entity) || this.isDrawOptionPropertyEnabled(this.friendlyMobs) && EntityUtils.isFriendlyMob((Entity)entity));
    }

    @Override
    public void loadConfig(Configuration configuration) {
        super.loadConfig(configuration);
        String[] DRAW_OPTIONS = Utils.toArray((Enum[])DrawOptions.values());
        String[] ARMOR_OPTIONS = Utils.toArray((Enum[])ArmorOptions.values());
        this.players = configuration.get(this.getModName(), "players", DrawOptions.ADVANCED.name(), "Enables player esp", DRAW_OPTIONS);
        Property[] arrproperty = new Property[]{this.players, this.hostileMobs = configuration.get(this.getModName(), "hostile mobs", DrawOptions.ADVANCED.name(), "Enables hostile mob esp", DRAW_OPTIONS), this.friendlyMobs = configuration.get(this.getModName(), "friendly mobs", DrawOptions.ADVANCED.name(), "Enables friendly mob esp", DRAW_OPTIONS), this.armorEsp = configuration.get(this.getModName(), "armor esp", ArmorOptions.ENCHANTMENTS.name(), "Shows info about entities armor if set to advanced esp mode", ARMOR_OPTIONS), this.distanceEsp = configuration.get(this.getModName(), "distance esp", false, "Shows distance in name tags if selected settings >name")};
        this.addSettings(arrproperty);
    }

    @SubscribeEvent
    public void onRenderPlayerNameTag(RenderLivingEvent.Specials.Pre event) {
        if (EntityUtils.isPlayer((Entity)event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            ArmorOptions armorMode = this.getArmorOptionValue(this.armorEsp);
            for (Entity entity : EntityEspMod.MC.theWorld.loadedEntityList) {
                int height;
                int posX;
                if (!EntityUtils.isLiving(entity) || !this.shouldDraw((EntityLivingBase)entity)) continue;
                EntityLivingBase living = (EntityLivingBase)entity;
                Vec3d bottomVec = EntityUtils.getInterpolatedPos((Entity)living, event.getPartialTicks());
                Vec3d topVec = bottomVec.add(new Vec3d(0.0, entity.getRenderBoundingBox().maxY - entity.posY, 0.0));
                VectorUtils.ScreenPos top = VectorUtils.toScreen(topVec.xCoord, topVec.yCoord, topVec.zCoord);
                VectorUtils.ScreenPos bot = VectorUtils.toScreen(bottomVec.xCoord, bottomVec.yCoord, bottomVec.zCoord);
                if (!top.isVisible && !bot.isVisible) continue;
                DrawOptions drawMode = this.getDrawOptionValue(living);
                int topX = top.x;
                int topY = top.y + 1;
                int botX = bot.x;
                int botY = bot.y + 1;
                int width = height = bot.y - top.y;
                if (LocalPlayerUtils.isTargetEntity(entity) || drawMode != null && drawMode.equals((Object)DrawOptions.ADVANCED)) {
                    int x = top.x - width / 2;
                    int y = top.y;
                    int w = width;
                    int h = height;
                    SurfaceUtils.drawOutlinedRect(x - 1, y - 1, w + 2, h + 2, Utils.Colors.BLACK, 2.0f);
                    SurfaceUtils.drawOutlinedRect(x, y, w, h, EntityUtils.getDrawColor(living), 2.0f);
                    SurfaceUtils.drawOutlinedRect(x + 1, y + 1, w - 2, h - 2, Utils.Colors.BLACK, 2.0f);
                }
                if (drawMode == null) continue;
                if (drawMode.equals((Object)DrawOptions.ADVANCED) || drawMode.equals((Object)DrawOptions.SIMPLE)) {
                    double hp = living.getHealth() / living.getMaxHealth();
                    posX = topX - 7;
                    int posY = topY - 3 - 2;
                    SurfaceUtils.drawRect(posX, posY, 15, 3, Utils.toRGBA(0, 0, 0, 255));
                    SurfaceUtils.drawRect(posX + 1, posY + 1, (int)(13.0 * hp), 1, Utils.toRGBA((int)((255.0 - hp) * 255.0), (int)(255.0 * hp), 0, 255));
                    topY -= 4;
                }
                if (drawMode.equals((Object)DrawOptions.ADVANCED) || drawMode.equals((Object)DrawOptions.SIMPLE) || drawMode.equals((Object)DrawOptions.NAME)) {
                    String text = living.getDisplayName().getFormattedText();
                    if (this.distanceEsp.getBoolean() && (drawMode.equals((Object)DrawOptions.SIMPLE) || drawMode.equals((Object)DrawOptions.ADVANCED))) {
                        text = text + String.format(" (%.1f)", living.getPositionVector().distanceTo(EntityEspMod.MC.thePlayer.getPositionVector()));
                    }
                    SurfaceUtils.drawTextShadow(text, topX - SurfaceUtils.getTextWidth(text) / 2, topY - SurfaceUtils.getTextHeight() - 1, Utils.toRGBA(255, 255, 255, 255));
                    topY -= SurfaceUtils.getTextHeight() + 1;
                }
                if (!drawMode.equals((Object)DrawOptions.ADVANCED) || armorMode.equals((Object)ArmorOptions.DISABLED)) continue;
                ArrayList armor = Lists.newArrayList();
                for (ItemStack stack : living.getEquipmentAndArmor()) {
                    if (stack == null) continue;
                    armor.add(0, stack);
                }
                if (armor.size() <= 0) continue;
                int endY = botY + 16;
                posX = topX - 16 * armor.size() / 2;
                for (int i = 0; i < armor.size(); ++i) {
                    List<EnchantmentUtils.EntityEnchantment> enchantments;
                    ItemStack stack2 = (ItemStack)armor.get(i);
                    int startX = posX + i * 16;
                    int startY = botY;
                    SurfaceUtils.drawItemWithOverlay(stack2, startX, startY);
                    if (!armorMode.equals((Object)ArmorOptions.ENCHANTMENTS) || (enchantments = EnchantmentUtils.getEnchantmentsSorted(stack2.getEnchantmentTagList())) == null) continue;
                    for (EnchantmentUtils.EntityEnchantment enchant : enchantments) {
                        SurfaceUtils.drawTextShadow(enchant.getShortName(), startX, startY, Utils.toRGBA(255, 255, 255, 255), 0.5);
                        if ((startY += SurfaceUtils.getTextHeight(0.5)) <= endY) continue;
                        endY = startY;
                    }
                }
                botY = endY + 1;
            }
        }
    }

    public static enum ArmorOptions {
        DISABLED,
        SIMPLE,
        ENCHANTMENTS;
        

        private ArmorOptions() {
        }
    }

    public static enum DrawOptions {
        DISABLED,
        NAME,
        SIMPLE,
        ADVANCED;
        

        private DrawOptions() {
        }
    }

}

