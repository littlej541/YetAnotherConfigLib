package dev.isxander.yacl.gui.controllers;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.yacl.api.Controller;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.utils.Dimension;
import dev.isxander.yacl.gui.AbstractWidget;
import dev.isxander.yacl.gui.YACLScreen;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

/**
 * This controller renders a tickbox
 */
public class TickBoxController implements Controller<Boolean> {
    private final Option<Boolean> option;

    /**
     * Constructs a tickbox controller
     *
     * @param option bound option
     */
    public TickBoxController(Option<Boolean> option) {
        this.option = option;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Option<Boolean> option() {
        return option;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component formatValue() {
        return TextComponent.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new TickBoxControllerElement(this, screen, widgetDimension);
    }

    public static class TickBoxControllerElement extends ControllerWidget<TickBoxController> {
        public TickBoxControllerElement(TickBoxController control, YACLScreen screen, Dimension<Integer> dim) {
            super(control, screen, dim);
        }

        @Override
        protected void drawHoveredControl(PoseStack matrices, int mouseX, int mouseY, float delta) {
            int outlineSize = 10;
            int outlineX1 = getDimension().xLimit() - getXPadding() - outlineSize;
            int outlineY1 = getDimension().centerY() - outlineSize / 2;
            int outlineX2 = getDimension().xLimit() - getXPadding();
            int outlineY2 = getDimension().centerY() + outlineSize / 2;

            int color = getValueColor();
            int shadowColor = multiplyColor(color, 0.25f);

            drawOutline(matrices, outlineX1 + 1, outlineY1 + 1, outlineX2 + 1, outlineY2 + 1, 1, shadowColor);
            drawOutline(matrices, outlineX1, outlineY1, outlineX2, outlineY2, 1, color);
            if (control.option().pendingValue()) {
                GuiComponent.fill(matrices, outlineX1 + 3, outlineY1 + 3, outlineX2 - 1, outlineY2 - 1, shadowColor);
                GuiComponent.fill(matrices, outlineX1 + 2, outlineY1 + 2, outlineX2 - 2, outlineY2 - 2, color);
            }
        }

        @Override
        protected void drawValueText(PoseStack matrices, int mouseX, int mouseY, float delta) {
            if (!isHovered())
                drawHoveredControl(matrices, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY) || !isAvailable())
                return false;

            toggleSetting();
            return true;
        }

        @Override
        protected int getHoveredControlWidth() {
            return 10;
        }

        @Override
        protected int getUnhoveredControlWidth() {
            return 10;
        }

        public void toggleSetting() {
            control.option().requestSet(!control.option().pendingValue());
            playDownSound();
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!this.isFocused()) {
                return false;
            }

            if (keyCode == InputConstants.KEY_RETURN || keyCode == InputConstants.KEY_SPACE || keyCode == InputConstants.KEY_NUMPADENTER) {
                toggleSetting();
                return true;
            }

            return false;
        }
    }
}
