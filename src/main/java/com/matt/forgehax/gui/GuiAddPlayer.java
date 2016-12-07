/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.client.FMLClientHandler
 *  net.minecraftforge.fml.client.config.GuiConfig
 */
package com.matt.forgehax.gui;

import com.matt.forgehax.gui.categories.IGuiCategory;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiAddPlayer
extends GuiScreen {
    private static final String STRING_ADD_FRIEND = "Enter player name to add";
    private final GuiScreen parent;
    private GuiButton buttonAddPlayer;
    private GuiButton buttonBack;
    private GuiTextField textFieldPlayerName;

    public GuiAddPlayer(@Nullable GuiScreen parent) {
        this.parent = parent;
        if (parent != null) {
            this.mc = parent.mc;
        }
    }

    public void exitScreen() {
        FMLClientHandler.instance().displayGuiScreen((EntityPlayer)this.mc.thePlayer, this.parent);
        if (this.parent instanceof GuiConfig) {
            ((GuiConfig)this.parent).needsRefresh = true;
        }
    }

    public String getTextFieldContents() {
        if (this.textFieldPlayerName != null && !this.textFieldPlayerName.getText().isEmpty() && !this.textFieldPlayerName.getText().equals("Enter player name to add")) {
            return this.textFieldPlayerName.getText();
        }
        return null;
    }

    public void initGui() {
        this.width = this.parent.width;
        this.height = this.parent.height;
        int x = this.width / 2;
        int y = this.height / 2;
        int textBoxHeight = 20;
        int textBoxWidth = 200;
        int buttonHeight = 20;
        int buttonWidth = 200;
        this.textFieldPlayerName = new GuiTextField(4000, this.mc.fontRendererObj, x - textBoxWidth / 2, y - textBoxHeight, textBoxWidth, textBoxHeight);
        this.textFieldPlayerName.setMaxStringLength(256);
        this.textFieldPlayerName.setEnabled(true);
        this.textFieldPlayerName.setFocused(true);
        this.textFieldPlayerName.setText("Enter player name to add");
        this.buttonAddPlayer = new GuiButton(4001, x - buttonWidth / 2, y + 5, buttonWidth, buttonHeight, "Add");
        this.buttonList.add(this.buttonAddPlayer);
        this.buttonBack = new GuiButton(4002, x - buttonWidth / 2, y + buttonHeight + 10, buttonWidth, buttonHeight, "Back");
        this.buttonList.add(this.buttonBack);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.equals((Object)this.buttonAddPlayer)) {
            if (this.parent instanceof IGuiCategory) {
                IGuiCategory guiCategory = (IGuiCategory)this.parent;
                guiCategory.addToSelected(this.getTextFieldContents());
            }
            this.exitScreen();
        } else if (button.equals((Object)this.buttonBack)) {
            this.exitScreen();
        }
    }

    public void func_146281_b() {
        if (this.parent != null && this.parent instanceof GuiConfig) {
            GuiConfig parentGuiConfig = (GuiConfig)this.parent;
            parentGuiConfig.needsRefresh = true;
            parentGuiConfig.initGui();
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            FMLClientHandler.instance().displayGuiScreen((EntityPlayer)this.mc.thePlayer, this.parent);
        } else {
            this.textFieldPlayerName.textboxKeyTyped(typedChar, keyCode);
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldPlayerName.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void updateScreen() {
        super.updateScreen();
        this.textFieldPlayerName.updateCursorCounter();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.textFieldPlayerName.drawTextBox();
    }
}

