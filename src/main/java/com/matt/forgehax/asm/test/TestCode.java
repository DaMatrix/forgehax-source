/*
 * Decompiled with CFR 0_118.
 */
package com.matt.forgehax.asm.test;

public class TestCode {
    private static boolean isNoSlowOn = false;
    private boolean movementInput = false;
    private boolean onGround = false;

    public boolean isSneaking() {
        return true;
    }

    public boolean isHandActive() {
        return true;
    }

    public boolean isRiding() {
        return true;
    }

    public void moveEntity() {
        this.isSneaking();
        if (this.isHandActive() && !this.isRiding() && !isNoSlowOn) {
            this.movementInput = false;
        }
        boolean flag3 = false;
    }
}

