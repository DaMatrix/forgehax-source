/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiTextField
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.client.config.GuiConfig
 *  net.minecraftforge.fml.client.config.GuiConfigEntries
 *  net.minecraftforge.fml.client.config.GuiConfigEntries$CategoryEntry
 *  net.minecraftforge.fml.client.config.IConfigElement
 *  org.lwjgl.input.Mouse
 */
package com.matt.forgehax.gui.categories;

import com.matt.forgehax.gui.GuiCategoryFileList;
import com.matt.forgehax.gui.GuiItemList;
import com.matt.forgehax.gui.categories.IGuiCategory;
import com.matt.forgehax.util.container.ContainerList;
import com.matt.forgehax.util.container.ContainerManager;
import com.matt.forgehax.util.container.lists.ItemList;
import com.matt.forgehax.util.draw.SurfaceUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.lwjgl.input.Mouse;

public class ItemListCategory
extends GuiConfigEntries.CategoryEntry {
    public ItemListCategory(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    protected GuiScreen buildChildScreen() {
        return new GuiItemManager((GuiScreen)this.owningScreen, this.configElement.getChildElements(), this.owningScreen.modID, this.owningScreen.allRequireWorldRestart || this.configElement.requiresWorldRestart(), this.owningScreen.allRequireMcRestart || this.configElement.requiresMcRestart(), this.owningScreen.title, (this.owningScreen.titleLine2 == null ? "" : this.owningScreen.titleLine2) + " > " + this.name);
    }

    public static class GuiItemManager
    extends GuiConfig
    implements IGuiCategory {
        private final List<ItemStack> ALL_ITEMS = ItemList.getRegisteredItems();
        private GuiCategoryFileList guiCategoryFileList;
        private GuiItemList guiItemList;
        public ItemList currentlySelectedList;
        private int fileListX;
        private int fileListY;
        private int fileListW;
        private int fileListH;
        private GuiButton buttonAdd;
        private GuiButton buttonRemove;
        private GuiTextField textField;
        private final String title2Original;

        public GuiItemManager(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, String titleLine2) {
            super(parentScreen, configElements, modID, allRequireWorldRestart, allRequireMcRestart, title, titleLine2);
            this.title2Original = titleLine2;
        }

        public void refreshGuiList(@Nullable ItemList selectedList) {
            int x = this.fileListX + this.fileListW + 10;
            int y = this.fileListY;
            int w = this.width - x - 5;
            int h = this.height - y - 40 - 25;
            this.guiItemList = new GuiItemList((GuiScreen)this, x, y, w, h, 43, this.width, this.height, this.ALL_ITEMS, selectedList);
            if (selectedList != null) {
                this.titleLine2 = this.title2Original + " > " + selectedList.getName();
            }
        }

        public void updateButtonLocks() {
            this.buttonRemove.enabled = this.currentlySelectedList != null;
            this.buttonAdd.enabled = !this.textField.getText().isEmpty();
        }

        public void initGui() {
            this.buttonList.clear();
            super.initGui();
            int x = 5;
            int y = 40;
            int w = this.parentScreen.width / 5;
            int h = this.parentScreen.height / 4;
            this.guiCategoryFileList = new GuiCategoryFileList((GuiScreen)this, x, y, w, h, SurfaceUtils.getTextHeight() + 2, this.width, this.height, ContainerManager.getContainerCollection(ContainerManager.Category.ITEMS));
            this.guiCategoryFileList.setSelected(this.currentlySelectedList);
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
            this.refreshGuiList(this.currentlySelectedList);
        }

        protected void actionPerformed(GuiButton button) {
            super.actionPerformed(button);
            if (button.equals((Object)this.buttonAdd) && !this.textField.getText().isEmpty()) {
                ContainerList list = (ContainerList)ContainerManager.createContainerList(ContainerManager.Category.PLAYERS, this.textField.getText());
                if (list != null) {
                    list.save();
                    this.initGui();
                }
            } else if (button.equals((Object)this.buttonRemove) && this.currentlySelectedList != null && ContainerManager.removeContainerList(ContainerManager.Category.PLAYERS, this.currentlySelectedList)) {
                this.currentlySelectedList = null;
                this.initGui();
            }
        }

        public void updateScreen() {
            super.updateScreen();
            this.textField.updateCursorCounter();
        }

        public void handleMouseInput() throws IOException {
            int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            super.handleMouseInput();
            if (this.guiCategoryFileList != null) {
                this.guiCategoryFileList.handleMouseInput(mouseX, mouseY);
            }
            if (this.guiItemList != null) {
                this.guiItemList.handleMouseInput(mouseX, mouseY);
            }
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.guiCategoryFileList.drawScreen(mouseX, mouseY, partialTicks);
            if (this.guiItemList != null) {
                this.guiItemList.drawScreen(mouseX, mouseY, partialTicks);
            }
            this.textField.drawTextBox();
        }

        protected void func_73869_a(char eventChar, int eventKey) {
            super.keyTyped(eventChar, eventKey);
            this.textField.textboxKeyTyped(eventChar, eventKey);
        }

        protected void func_73864_a(int x, int y, int mouseEvent) throws IOException {
            super.mouseClicked(x, y, mouseEvent);
            this.textField.mouseClicked(x, y, mouseEvent);
        }

        @Override
        public void onSelectedFileFromList(Object selected) {
        }

        @Override
        public void addToSelected(Object obj) {
        }
    }

}

