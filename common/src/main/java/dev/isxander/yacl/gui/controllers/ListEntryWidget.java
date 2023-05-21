package dev.isxander.yacl.gui.controllers;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.yacl.api.ListOption;
import dev.isxander.yacl.api.ListOptionEntry;
import dev.isxander.yacl.api.utils.Dimension;
import dev.isxander.yacl.gui.AbstractWidget;
import dev.isxander.yacl.gui.TooltipButtonWidget;
import dev.isxander.yacl.gui.YACLScreen;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListEntryWidget extends AbstractWidget implements ContainerEventHandler {
    private final TooltipButtonWidget removeButton, moveUpButton, moveDownButton;
    private final AbstractWidget entryWidget;

    private final ListOption<?> listOption;
    private final ListOptionEntry<?> listOptionEntry;

    private final String optionNameString;

    private GuiEventListener focused;
    private boolean dragging;

    public ListEntryWidget(YACLScreen screen, ListOptionEntry<?> listOptionEntry, AbstractWidget entryWidget) {
        super(entryWidget.getDimension().withHeight(Math.max(entryWidget.getDimension().height(), 20) - ((listOptionEntry.parentGroup().indexOf(listOptionEntry) == listOptionEntry.parentGroup().options().size() - 1) ? 0 : 2))); // -2 to remove the padding
        this.listOptionEntry = listOptionEntry;
        this.listOption = listOptionEntry.parentGroup();
        this.optionNameString = listOptionEntry.name().getString().toLowerCase();
        this.entryWidget = entryWidget;

        Dimension<Integer> dim = entryWidget.getDimension();
        entryWidget.setDimension(dim.clone().move(20 * 2, 0).expand(-20 * 3, 0));

        removeButton = new TooltipButtonWidget(screen, dim.xLimit() - 20, dim.y(), 20, 20, new TextComponent("\u274c"), new TranslatableComponent("yacl.list.remove"), btn -> {
            listOption.removeEntry(listOptionEntry);
            updateButtonStates();
        });

        moveUpButton = new TooltipButtonWidget(screen, dim.x(), dim.y(), 20, 20, new TextComponent("\u2191"), new TranslatableComponent("yacl.list.move_up"), btn -> {
            int index = listOption.indexOf(listOptionEntry) - 1;
            if (index >= 0) {
                listOption.removeEntry(listOptionEntry);
                listOption.insertEntry(index, listOptionEntry);
                updateButtonStates();
            }
        });

        moveDownButton = new TooltipButtonWidget(screen, dim.x() + 20, dim.y(), 20, 20, new TextComponent("\u2193"), new TranslatableComponent("yacl.list.move_down"), btn -> {
            int index = listOption.indexOf(listOptionEntry) + 1;
            if (index < listOption.options().size()) {
                listOption.removeEntry(listOptionEntry);
                listOption.insertEntry(index, listOptionEntry);
                updateButtonStates();
            }
        });

        updateButtonStates();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        updateButtonStates(); // update every render in case option becomes available/unavailable

        removeButton.y = getDimension().y();
        moveUpButton.y = getDimension().y();
        moveDownButton.y = getDimension().y();
        entryWidget.setDimension(entryWidget.getDimension().withY(getDimension().y()));

        removeButton.render(matrices, mouseX, mouseY, delta);
        moveUpButton.render(matrices, mouseX, mouseY, delta);
        moveDownButton.render(matrices, mouseX, mouseY, delta);
        entryWidget.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void postRender(PoseStack matrices, int mouseX, int mouseY, float delta) {
        removeButton.renderHoveredTooltip(matrices);
        moveUpButton.renderHoveredTooltip(matrices);
        moveDownButton.renderHoveredTooltip(matrices);
    }

    protected void updateButtonStates() {
        removeButton.active = listOption.available();
        moveUpButton.active = listOption.indexOf(listOptionEntry) > 0 && listOption.available();
        moveDownButton.active = listOption.indexOf(listOptionEntry) < listOption.options().size() - 1 && listOption.available();
    }

    @Override
    public void unfocus() {
        entryWidget.unfocus();
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        entryWidget.updateNarration(builder);
    }

    @Override
    public boolean matchesSearch(String query) {
        return optionNameString.contains(query.toLowerCase());
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return ImmutableList.of(moveUpButton, moveDownButton, entryWidget, removeButton);
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        this.focused = focused;
    }
}
