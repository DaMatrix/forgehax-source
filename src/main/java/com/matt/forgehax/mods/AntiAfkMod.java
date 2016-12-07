/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.common.config.Configuration
 *  net.minecraftforge.common.config.Property
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package com.matt.forgehax.mods;

import com.matt.forgehax.events.LocalPlayerUpdateEvent;
import com.matt.forgehax.mods.ToggleMod;
import com.matt.forgehax.util.key.Bindings;
import com.matt.forgehax.util.key.KeyBindingHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiAfkMod
extends ToggleMod {
    public Property tickInterval;
    public Property moveDistance;
    private Vec3d startPos;
    private int moveDirection = 0;
    private boolean isAutoMoving = false;
    private int tickCounter = 0;

    public AntiAfkMod(String categoryName, boolean defaultValue, String description, int key) {
        super(categoryName, defaultValue, description, key);
    }

    public KeyBindingHandler getMoveBinding() {
        switch (Math.floorMod(this.moveDirection, 4)) {
            case 0: {
                return Bindings.forward;
            }
            case 1: {
                return Bindings.left;
            }
            case 2: {
                return Bindings.back;
            }
        }
        return Bindings.right;
    }

    public void setMoving(boolean b) {
        this.isAutoMoving = b;
    }

    public boolean isMoving() {
        return this.isAutoMoving;
    }

    @Override
    public void loadConfig(Configuration configuration) {
        super.loadConfig(configuration);
        this.tickInterval = configuration.get(this.getModName(), "interval", 200, "Amount of ticks to wait before moving again");
        Property[] arrproperty = new Property[]{this.tickInterval, this.moveDistance = configuration.get(this.getModName(), "distance", 0.25, "Distance to move before stopping")};
        this.addSettings(arrproperty);
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
        if (this.isMoving()) {
            this.getMoveBinding().setPressed(false);
            this.getMoveBinding().unbind();
            Bindings.sneak.setPressed(false);
            Bindings.sneak.unbind();
            this.setMoving(false);
            this.tickCounter = 0;
        }
    }

    @SubscribeEvent
    public void onUpdate(LocalPlayerUpdateEvent event) {
        ++this.tickCounter;
        if (this.isMoving()) {
            Vec3d currentPos = event.getEntityLiving().getPositionVector();
            double distanceMoved = currentPos.distanceTo(this.startPos);
            if (distanceMoved >= this.moveDistance.getDouble()) {
                this.getMoveBinding().setPressed(false);
                this.getMoveBinding().unbind();
                Bindings.sneak.setPressed(false);
                Bindings.sneak.unbind();
                this.setMoving(false);
                this.tickCounter = 0;
            } else {
                if (!this.getMoveBinding().getBinding().isKeyDown()) {
                    this.getMoveBinding().setPressed(true);
                }
                if (!Bindings.sneak.getBinding().isKeyDown()) {
                    Bindings.sneak.setPressed(true);
                }
            }
        } else if (this.tickCounter >= this.tickInterval.getInt()) {
            ++this.moveDirection;
            Bindings.sneak.bind();
            Bindings.sneak.setPressed(true);
            this.getMoveBinding().bind();
            this.getMoveBinding().setPressed(true);
            this.startPos = event.getEntityLiving().getPositionVector();
            this.setMoving(true);
        }
    }
}

