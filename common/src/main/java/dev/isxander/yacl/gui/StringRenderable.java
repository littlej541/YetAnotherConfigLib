package dev.isxander.yacl.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

public interface StringRenderable {
    void renderString(PoseStack matrices, Font textRenderer, int color);
}
