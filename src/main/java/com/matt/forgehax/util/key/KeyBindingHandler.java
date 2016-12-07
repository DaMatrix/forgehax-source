/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraftforge.client.settings.IKeyConflictContext
 */
package com.matt.forgehax.util.key;

import com.matt.forgehax.ForgeHaxBase;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;

public class KeyBindingHandler
extends ForgeHaxBase {
    private static final IKeyConflictContext OVERRIDE_KEYCONFLICT_CONTEXT = new IKeyConflictContext(){

        public boolean isActive() {
            return true;
        }

        public boolean conflicts(IKeyConflictContext other) {
            return false;
        }
    };
    private final KeyBinding binding;
    private IKeyConflictContext oldConflictContext = null;
    private int bindingCount = 0;

    public KeyBindingHandler(KeyBinding bind) {
        this.binding = bind;
    }

    public KeyBinding getBinding() {
        return this.binding;
    }

    public void setPressed(boolean b) {
        KeyBinding.setKeyBindState((int)this.binding.getKeyCode(), (boolean)b);
    }

    public boolean isBound() {
        return this.binding.getKeyConflictContext() == OVERRIDE_KEYCONFLICT_CONTEXT;
    }

    public void bind() {
        ++this.bindingCount;
        if (this.oldConflictContext == null) {
            this.oldConflictContext = this.binding.getKeyConflictContext();
            this.binding.setKeyConflictContext(OVERRIDE_KEYCONFLICT_CONTEXT);
        }
    }

    public void attemptBind() {
        if (!this.isBound()) {
            this.bind();
        }
    }

    public void unbind() {
        --this.bindingCount;
        if (this.oldConflictContext != null && this.bindingCount <= 0) {
            this.binding.setKeyConflictContext(this.oldConflictContext);
            this.oldConflictContext = null;
        }
        if (this.bindingCount < 0) {
            this.bindingCount = 0;
        }
    }

    public void attemptUnbind() {
        if (this.isBound()) {
            this.unbind();
        }
    }

}

