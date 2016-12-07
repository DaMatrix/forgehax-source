/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.FMLClientHandler
 *  net.minecraftforge.fml.client.config.GuiConfig
 */
package com.matt.forgehax.gui;

import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.container.lists.PlayerList;
import com.matt.forgehax.util.draw.SurfaceUtils;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiEditPlayer
extends GuiScreen {
    private static final String STRING_ADD_NICK = "Enter nick name";
    private final GuiScreen parent;
    private final PlayerList.PlayerData playerData;
    private ResourceLocation playerSkin;
    private GuiButton buttonSave;
    private GuiButton buttonBack;
    private GuiTextField textFieldPlayerNickName;

    public GuiEditPlayer(@Nullable GuiScreen parent, PlayerList.PlayerData playerData) {
        this.parent = parent;
        if (parent != null) {
            this.mc = parent.mc;
        }
        this.playerData = playerData;
        ResourceLocation resourceLocation = AbstractClientPlayer.getLocationSkin((String)playerData.getName());
        AbstractClientPlayer.getDownloadImageSkin((ResourceLocation)resourceLocation, (String)playerData.getName());
        this.playerSkin = resourceLocation;
    }

    public void exitScreen() {
        FMLClientHandler.instance().displayGuiScreen((EntityPlayer)this.mc.thePlayer, this.parent);
        if (this.parent instanceof GuiConfig) {
            ((GuiConfig)this.parent).needsRefresh = true;
        }
    }

    public boolean isValidTextInField() {
        return !this.textFieldPlayerNickName.getText().isEmpty() && !this.textFieldPlayerNickName.getText().equals("Enter nickname");
    }

    public void initGui() {
        this.width = this.parent.width;
        this.height = this.parent.height;
        int x = this.width / 2;
        int y = this.height / 2 + 100;
        int textBoxWidth = 200;
        int textBoxHeight = 20;
        int posX = x - textBoxWidth / 2;
        int posY = y - textBoxHeight;
        this.textFieldPlayerNickName = new GuiTextField(4000, this.mc.fontRendererObj, posX, posY, textBoxWidth, textBoxHeight);
        this.textFieldPlayerNickName.setMaxStringLength(256);
        this.textFieldPlayerNickName.setEnabled(true);
        this.textFieldPlayerNickName.setFocused(true);
        this.textFieldPlayerNickName.setText(!this.playerData.getNickName().isEmpty() ? this.playerData.getNickName() : "Enter nickname");
        posX = x - 52;
        posY = y + 25;
        this.buttonSave = new GuiButton(4001, posX, posY, 50, 20, "Save");
        this.buttonList.add(this.buttonSave);
        this.buttonBack = new GuiButton(4002, posX + 55, posY, 50, 20, "Back");
        this.buttonList.add(this.buttonBack);
    }

    public void func_146281_b() {
        if (this.parent != null && this.parent instanceof GuiConfig) {
            GuiConfig parentGuiConfig = (GuiConfig)this.parent;
            parentGuiConfig.needsRefresh = true;
            parentGuiConfig.initGui();
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.equals((Object)this.buttonSave)) {
            if (this.isValidTextInField()) {
                this.playerData.setNickName(this.textFieldPlayerNickName.getText());
                this.playerData.save();
            }
            this.exitScreen();
        } else if (button.equals((Object)this.buttonBack)) {
            this.exitScreen();
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            FMLClientHandler.instance().displayGuiScreen((EntityPlayer)this.mc.thePlayer, this.parent);
        } else {
            this.textFieldPlayerNickName.textboxKeyTyped(typedChar, keyCode);
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldPlayerNickName.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void updateScreen() {
        super.updateScreen();
        this.textFieldPlayerNickName.updateCursorCounter();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.textFieldPlayerNickName.drawTextBox();
        String nickName = this.playerData.getNickName();
        if (this.isValidTextInField()) {
            nickName = this.textFieldPlayerNickName.getText();
        }
        String renderName = !nickName.isEmpty() ? String.format("%s (%s)", this.playerData.getName(), nickName) : this.playerData.getName();
        String uuid = this.playerData.getUuid().toString();
        int longestWidth = SurfaceUtils.getTextWidth(uuid);
        if (SurfaceUtils.getTextWidth(renderName) > longestWidth) {
            longestWidth = SurfaceUtils.getTextWidth(renderName);
        }
        int faceWidth = SurfaceUtils.getHeadWidth(3.0f);
        int pdWidth = longestWidth + faceWidth + 2;
        int x = this.width / 2;
        int y = this.height / 2;
        int posX = x - pdWidth / 2;
        int posY = y - 200;
        if (this.playerSkin != null) {
            SurfaceUtils.drawHead(this.playerSkin, posX, posY, 3.0f);
        }
        SurfaceUtils.drawText(renderName, posX + SurfaceUtils.getHeadWidth(3.0f) + 2, posY, Utils.Colors.WHITE);
        SurfaceUtils.drawText(uuid, posX + SurfaceUtils.getHeadWidth(3.0f) + 2, posY + SurfaceUtils.getTextHeight() + 1, Utils.Colors.WHITE);
    }
}

