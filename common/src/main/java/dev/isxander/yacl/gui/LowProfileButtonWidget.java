package dev.isxander.yacl.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.yacl.gui.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class LowProfileButtonWidget extends Button implements StringRenderable {
    public LowProfileButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, NO_TOOLTIP);
    }

    public LowProfileButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, width, height, message, onPress, onTooltip);
    }

    public void renderWidget(PoseStack matrices, int mouseX, int mouseY, float deltaTicks) {
        this.renderButton(matrices, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float deltaTicks) {
        if (!isHoveredOrFocused() || !active) {
            int j = this.active ? 0xFFFFFF : 0xA0A0A0;
            this.renderString(matrices, Minecraft.getInstance().font, j);
        } else {
            super.renderButton(matrices, mouseX, mouseY, deltaTicks);
        }
    }

    @Override
    public void renderString(PoseStack matrices, Font textRenderer, int color) {
        GuiUtils.renderString(matrices, Minecraft.getInstance().font, this.getMessage(), this.x, this.y, this.width, this.height, color);
    }
}
