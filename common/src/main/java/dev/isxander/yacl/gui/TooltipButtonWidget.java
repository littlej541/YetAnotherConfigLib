package dev.isxander.yacl.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.yacl.gui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TooltipButtonWidget extends Button implements StringRenderable {

    protected final Screen screen;
    protected MultiLineLabel wrappedDescription = null;

    public TooltipButtonWidget(Screen screen, int x, int y, int width, int height, Component message, Component tooltip, OnPress onPress) {
        super(x, y, width, height, message, onPress);
        this.screen = screen;
        if (tooltip != null)
            setTooltip(tooltip);
    }

    public void renderHoveredTooltip(PoseStack matrices) {
        if (isHoveredOrFocused() && wrappedDescription != null) {
            YACLScreen.renderMultilineTooltip(matrices, Minecraft.getInstance().font, wrappedDescription, x + width / 2, y - 4, y + height + 4, screen.width, screen.height);
        }
    }

    public void setTooltip(Component tooltip) {
        wrappedDescription = MultiLineLabel.create(Minecraft.getInstance().font, tooltip, screen.width / 3 - 5);
    }

    public void renderString(PoseStack matrices, Font textRenderer, int color) {
        GuiUtils.renderString(matrices, textRenderer, this.getMessage(), this.x, this.y, this.width, this.height, color);
    }
}
