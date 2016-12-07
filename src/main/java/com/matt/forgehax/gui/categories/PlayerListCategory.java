/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
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
 *  net.minecraftforge.fml.client.config.GuiConfigEntries
 *  net.minecraftforge.fml.client.config.GuiConfigEntries$CategoryEntry
 *  net.minecraftforge.fml.client.config.IConfigElement
 *  org.lwjgl.input.Mouse
 */
package com.matt.forgehax.gui.categories;

import com.google.gson.JsonElement;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.gui.GuiAddPlayer;
import com.matt.forgehax.gui.GuiCategoryFileList;
import com.matt.forgehax.gui.GuiEditPlayer;
import com.matt.forgehax.gui.GuiPlayerList;
import com.matt.forgehax.gui.categories.IGuiCategory;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.container.ContainerList;
import com.matt.forgehax.util.container.ContainerManager;
import com.matt.forgehax.util.container.lists.PlayerList;
import com.matt.forgehax.util.draw.SurfaceUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.lwjgl.input.Mouse;

public class PlayerListCategory
extends GuiConfigEntries.CategoryEntry {
    public PlayerListCategory(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    protected GuiScreen buildChildScreen() {
        return new GuiPlayerManager((GuiScreen)this.owningScreen, this.configElement.getChildElements(), this.owningScreen.modID, this.owningScreen.allRequireWorldRestart || this.configElement.requiresWorldRestart(), this.owningScreen.allRequireMcRestart || this.configElement.requiresMcRestart(), this.owningScreen.title, (this.owningScreen.titleLine2 == null ? "" : this.owningScreen.titleLine2) + " > " + this.name);
    }

    public static class GuiPlayerManager
    extends GuiConfig
    implements IGuiCategory {
        private GuiCategoryFileList guiCategoryFileList;
        private GuiPlayerList guiPlayerList;
        public PlayerList selectedList;
        private int fileListX;
        private int fileListY;
        private int fileListW;
        private int fileListH;
        private GuiButton buttonAdd;
        private GuiButton buttonRemove;
        private GuiTextField textField;
        private GuiButton buttonAddPlayer;
        private GuiButton buttonRemovePlayer;
        private GuiButton buttonEditPlayer;
        private final String title2Original;
        public String statusMessage = "";
        public String errorMessage = "";
        public boolean doRefresh = false;
        public boolean isCurrentlyAddingPlayer = false;

        public GuiPlayerManager(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, String titleLine2) {
            super(parentScreen, configElements, modID, allRequireWorldRestart, allRequireMcRestart, title, titleLine2);
            this.title2Original = titleLine2;
        }

        public void refreshGuiList(@Nullable PlayerList selectedList) {
            int x = this.fileListX + this.fileListW + 10;
            int y = this.fileListY;
            int w = this.width - x - 5;
            int h = this.height - y - 40 - 25;
            this.guiPlayerList = new GuiPlayerList((GuiScreen)this, x, y, w, h, 43, this.width, this.height, selectedList);
            if (selectedList != null) {
                this.titleLine2 = this.title2Original + " > " + selectedList.getName();
            }
            this.updateButtonLocks();
        }

        public void updateButtonLocks() {
            this.buttonRemove.enabled = this.selectedList != null;
            this.buttonAdd.enabled = !this.textField.getText().isEmpty();
            this.buttonAddPlayer.enabled = this.selectedList != null && !this.isCurrentlyAddingPlayer;
            this.buttonEditPlayer.enabled = this.guiPlayerList != null && this.guiPlayerList.getCurrentlySelected() != null;
            this.buttonRemovePlayer.enabled = this.buttonEditPlayer.enabled;
        }

        public void initGui() {
            this.buttonList.clear();
            super.initGui();
            int x = 5;
            int y = 40;
            int w = this.parentScreen.width / 5;
            int h = this.parentScreen.height / 4;
            this.guiCategoryFileList = new GuiCategoryFileList((GuiScreen)this, x, y, w, h, SurfaceUtils.getTextHeight() + 2, this.width, this.height, ContainerManager.getContainerCollection(ContainerManager.Category.PLAYERS));
            this.guiCategoryFileList.setSelected(this.selectedList);
            this.fileListX = x;
            this.fileListY = y;
            this.fileListW = w;
            this.fileListH = h;
            int ID = 3000;
            int buttonWidth = 50;
            this.buttonRemove = new GuiButton(ID++, x, y + h + 5, w, 20, "Remove");
            this.buttonList.add(this.buttonRemove);
            this.buttonAdd = new GuiButton(ID++, x, y + h + 30, buttonWidth, 20, "Add");
            this.buttonList.add(this.buttonAdd);
            this.textField = new GuiTextField(ID++, this.mc.fontRendererObj, x + buttonWidth + 5, y + h + 30, w - (x + buttonWidth + 5), 20);
            this.textField.setMaxStringLength(256);
            this.textField.setEnabled(true);
            this.textField.setFocused(true);
            this.textField.setText("");
            this.buttonAddPlayer = new GuiButton(ID++, this.fileListX + this.fileListW + 10, this.height - y - 20, 100, 20, "Add");
            this.buttonList.add(this.buttonAddPlayer);
            this.buttonRemovePlayer = new GuiButton(ID++, this.fileListX + this.fileListW + 10 + 105, this.height - y - 20, 100, 20, "Remove");
            this.buttonList.add(this.buttonRemovePlayer);
            this.buttonEditPlayer = new GuiButton(ID++, this.fileListX + this.fileListW + 10 + 105 + 105, this.height - y - 20, 100, 20, "Edit");
            this.buttonList.add(this.buttonEditPlayer);
            this.refreshGuiList(this.selectedList);
        }

        protected void actionPerformed(GuiButton button) {
            super.actionPerformed(button);
            if (button.equals((Object)this.buttonAdd) && !this.textField.getText().isEmpty()) {
                ContainerList list = (ContainerList)ContainerManager.createContainerList(ContainerManager.Category.PLAYERS, this.textField.getText());
                if (list != null) {
                    list.save();
                    this.initGui();
                }
            } else if (button.equals((Object)this.buttonRemove) && this.selectedList != null) {
                if (ContainerManager.removeContainerList(ContainerManager.Category.PLAYERS, this.selectedList)) {
                    this.selectedList = null;
                    this.initGui();
                }
            } else if (button.equals((Object)this.buttonAddPlayer) && this.selectedList != null) {
                FMLClientHandler.instance().displayGuiScreen((EntityPlayer)this.mc.thePlayer, (GuiScreen)new GuiAddPlayer((GuiScreen)this));
            } else if (button.equals((Object)this.buttonRemovePlayer) && this.selectedList != null) {
                Map.Entry<String, JsonElement> selectedPlayer = this.guiPlayerList.getCurrentlySelected();
                if (selectedPlayer != null) {
                    this.selectedList.removePlayerByUUID(selectedPlayer.getKey());
                    this.initGui();
                }
            } else if (button.equals((Object)this.buttonEditPlayer) && this.selectedList != null) {
                Map.Entry<String, JsonElement> selectedPlayer = this.guiPlayerList.getCurrentlySelected();
                FMLClientHandler.instance().displayGuiScreen((EntityPlayer)this.mc.thePlayer, (GuiScreen)new GuiEditPlayer((GuiScreen)this, this.selectedList.getPlayerData(selectedPlayer.getKey())));
            }
        }

        public void updateScreen() {
            if (this.doRefresh) {
                this.initGui();
                this.doRefresh = false;
                this.statusMessage = "";
                this.errorMessage = "";
            } else {
                super.updateScreen();
                this.textField.updateCursorCounter();
            }
        }

        public void handleMouseInput() throws IOException {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            super.handleMouseInput();
            if (this.guiCategoryFileList != null) {
                this.guiCategoryFileList.handleMouseInput(mouseX, mouseY);
            }
            if (this.guiPlayerList != null) {
                this.guiPlayerList.handleMouseInput(mouseX, mouseY);
            }
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.guiCategoryFileList.drawScreen(mouseX, mouseY, partialTicks);
            if (this.guiPlayerList != null) {
                this.guiPlayerList.drawScreen(mouseX, mouseY, partialTicks);
            }
            this.textField.drawTextBox();
            int x = 1;
            int y = 1;
            if (!this.statusMessage.isEmpty()) {
                SurfaceUtils.drawTextShadow(this.statusMessage, x, y, Utils.Colors.GREEN);
                y += SurfaceUtils.getTextHeight() + 1;
            }
            if (!this.errorMessage.isEmpty()) {
                SurfaceUtils.drawTextShadow(this.errorMessage, x, y, Utils.Colors.RED);
            }
        }

        protected void func_73869_a(char eventChar, int eventKey) {
            super.keyTyped(eventChar, eventKey);
            this.textField.textboxKeyTyped(eventChar, eventKey);
            this.updateButtonLocks();
        }

        protected void func_73864_a(int x, int y, int mouseEvent) throws IOException {
            super.mouseClicked(x, y, mouseEvent);
            this.textField.mouseClicked(x, y, mouseEvent);
        }

        @Override
        public void onSelectedFileFromList(Object selected) {
            if (selected instanceof PlayerList) {
                this.selectedList = (PlayerList)selected;
                this.refreshGuiList(this.selectedList);
            } else if (selected == null) {
                this.selectedList = null;
                this.refreshGuiList(null);
            }
        }

        @Override
        public void addToSelected(final Object obj) {
            if (obj instanceof String && this.selectedList != null) {
                this.isCurrentlyAddingPlayer = true;
                this.statusMessage = "Getting player data from Mojang servers...";
                new Thread(new Runnable(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    @Override
                    public void run() {
                        Runnable var1_1 = this;
                        synchronized (var1_1) {
                            try {
                                String name = (String)obj;
                                GuiPlayerManager.this.selectedList.addPlayerByName(name);
                            }
                            catch (Exception e) {
                                GuiPlayerManager.this.errorMessage = e.getMessage();
                                ForgeHax.instance().printStackTrace(e);
                            }
                            finally {
                                GuiPlayerManager.this.doRefresh = true;
                                GuiPlayerManager.this.isCurrentlyAddingPlayer = false;
                            }
                        }
                    }
                }).start();
            }
        }

    }

}

