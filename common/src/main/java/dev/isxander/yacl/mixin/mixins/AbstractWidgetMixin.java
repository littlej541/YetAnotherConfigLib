package dev.isxander.yacl.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.isxander.yacl.gui.StringRenderable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(AbstractWidget.class)
public class AbstractWidgetMixin {
    @Redirect(method = "renderButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/AbstractWidget;drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private void renderStringFix(PoseStack poseStack, Font font, Component component, int centerX, int y, int color) {
        if (this instanceof StringRenderable stringRenderable) {
            stringRenderable.renderString(poseStack, font, color);
        } else {
            GuiComponent.drawCenteredString(poseStack, font, component, centerX, y, color);
        }
    }
}
