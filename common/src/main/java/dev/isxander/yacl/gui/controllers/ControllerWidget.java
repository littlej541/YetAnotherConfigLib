package dev.isxander.yacl.gui.controllers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.yacl.api.Controller;
import dev.isxander.yacl.api.utils.Dimension;
import dev.isxander.yacl.gui.AbstractWidget;
import dev.isxander.yacl.gui.YACLScreen;
import dev.isxander.yacl.gui.utils.GuiUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public abstract class ControllerWidget<T extends Controller<?>> extends AbstractWidget {
    protected final T control;
    protected MultiLineLabel wrappedTooltip;
    protected final YACLScreen screen;

    protected boolean hovered = false;

    protected final Component modifiedOptionName;
    protected final String optionNameString;

    public ControllerWidget(T control, YACLScreen screen, Dimension<Integer> dim) {
        super(dim);
        this.control = control;
        this.screen = screen;
        control.option().addListener((opt, pending) -> updateTooltip());
        updateTooltip();
        this.modifiedOptionName = control.option().name().copy().withStyle(ChatFormatting.ITALIC);
        this.optionNameString = control.option().name().getString().toLowerCase();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        hovered = isMouseOver(mouseX, mouseY);

        Component name = control.option().changed() ? modifiedOptionName : control.option().name();
        Component shortenedName = new TextComponent(GuiUtils.shortenString(name.getString(), textRenderer, getDimension().width() - getControlWidth() - getXPadding() - 7, "...")).setStyle(name.getStyle());

        drawButtonRect(matrices, getDimension().x(), getDimension().y(), getDimension().xLimit(), getDimension().yLimit(), isHovered(), isAvailable());
        matrices.pushPose();
        matrices.translate(getDimension().x() + getXPadding(), getTextY(), 0);
        textRenderer.drawShadow(matrices, shortenedName, 0, 0, getValueColor());
        matrices.popPose();

        drawValueText(matrices, mouseX, mouseY, delta);
        if (isHovered()) {
            drawHoveredControl(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public void postRender(PoseStack matrices, int mouseX, int mouseY, float delta) {
        if (hovered || this.isFocused()) {
            YACLScreen.renderMultilineTooltip(matrices, textRenderer, wrappedTooltip, getDimension().centerX(), getDimension().y() - 5, getDimension().yLimit() + 5, screen.width, screen.height);
        }
    }

    protected void drawHoveredControl(PoseStack matrices, int mouseX, int mouseY, float delta) {

    }

    protected void drawValueText(PoseStack matrices, int mouseX, int mouseY, float delta) {
        Component valueText = getValueText();
        matrices.pushPose();
        matrices.translate(getDimension().xLimit() - textRenderer.width(valueText) - getXPadding(), getTextY(), 0);
        textRenderer.drawShadow(matrices, valueText, 0, 0, getValueColor());
        matrices.popPose();
    }

    private void updateTooltip() {
        this.wrappedTooltip = MultiLineLabel.create(textRenderer, control.option().tooltip(), screen.width / 3 * 2 - 10);
    }

    protected int getControlWidth() {
        return isHovered() ? getHoveredControlWidth() : getUnhoveredControlWidth();
    }

    public boolean isHovered() {
        return isAvailable() && (hovered || this.isFocused());
    }

    protected abstract int getHoveredControlWidth();

    protected int getUnhoveredControlWidth() {
        return textRenderer.width(getValueText());
    }

    protected int getXPadding() {
        return 5;
    }

    protected int getYPadding() {
        return 2;
    }

    protected Component getValueText() {
        return control.formatValue();
    }

    protected boolean isAvailable() {
        return control.option().available();
    }

    protected int getValueColor() {
        return isAvailable() ? -1 : inactiveColor;
    }

    @Override
    public boolean canReset() {
        return true;
    }

    protected void drawOutline(PoseStack matrices, int x1, int y1, int x2, int y2, int width, int color) {
        GuiComponent.fill(matrices, x1, y1, x2, y1 + width, color);
        GuiComponent.fill(matrices, x2, y1, x2 - width, y2, color);
        GuiComponent.fill(matrices, x1, y2, x2, y2 - width, color);
        GuiComponent.fill(matrices, x1, y1, x1 + width, y2, color);
    }

    protected float getTextY() {
        return getDimension().y() + getDimension().height() / 2f - textRenderer.lineHeight / 2f;
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if (this.isAvailable()) {
            this.setFocused(!this.isFocused());
            return this.isFocused();
        }

        return false;
    }

    @Override
    public boolean matchesSearch(String query) {
        return optionNameString.contains(query.toLowerCase());
    }

    @Override
    public NarrationPriority narrationPriority() {
        return this.isFocused() ? NarrationPriority.FOCUSED : isHovered() ? NarrationPriority.HOVERED : NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, control.option().name());
        builder.add(NarratedElementType.HINT, control.option().tooltip());
    }
}
