package dev.isxander.yacl.gui.utils;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class GuiUtils {
    public static MutableComponent translatableFallback(String key, Component fallback) {
        if (Language.getInstance().has(key))
            return new TranslatableComponent(key);
        return fallback.copy();
    }

    public static void enableScissor(int x, int y, int width, int height) {
        Window window = Minecraft.getInstance().getWindow();
        double d = window.getGuiScale();
        RenderSystem.enableScissor((int)(x * d), (int)((window.getGuiScaledHeight() - y - height) * d), (int)(width * d), (int)(height * d));
    }

    public static String shortenString(String string, Font font, int maxWidth, String suffix) {
        if (string.isEmpty())
            return string;

        boolean firstIter = true;
        while (font.width(string) > maxWidth) {
            string = string.substring(0, Math.max(string.length() - 1 - (firstIter ? 1 : suffix.length() + 1), 0)).trim();
            string += suffix;

            if (string.equals(suffix))
                break;

            firstIter = false;
        }

        return string;
    }

    public static void renderString(PoseStack matrices, Font font, Component message, int x, int y, int width, int height, int color) {
        int centerX = x + width / 2;
        int centerY = y + (height - 9) / 2 + 1;

        GuiComponent.drawCenteredString(matrices, font, message, centerX, centerY, color);
    }

    public static void renderTooltipBackground(BoxDrawer drawer, Matrix4f pose, BufferBuilder bufferBuilder, int x, int y, int width, int height, int z) {
        int x0 = x - 3;
        int y0 = y - 3;
        int x1 = x0 + width + 3 + 3;
        int y1 = y0 + height + 3 + 3;

        drawer.draw(pose, bufferBuilder, x0, y0 - 1, x1, y0, z, 0xF0100010, 0xF0100010); // top outer
        drawer.draw(pose, bufferBuilder, x0, y1, x1, y1 + 1, z, 0xF0100010, 0xF0100010); // bottom outer
        drawer.draw(pose, bufferBuilder, x0, y0, x1, y1, z, 0xF0100010, 0xF0100010); // background
        drawer.draw(pose, bufferBuilder, x0 - 1, y0, x0, y1, z, 0xF0100010, 0xF0100010); // left outer
        drawer.draw(pose, bufferBuilder, x1, y0, x1 + 1, y1, z, 0xF0100010, 0xF0100010); // right outer
        drawer.draw(pose, bufferBuilder, x0, y0 + 1, x0 + 1, y1 - 1, z, 0x505000FF, 0x5028007F); // left inner
        drawer.draw(pose, bufferBuilder, x1 - 1, y0 + 1, x1, y1 - 1, z, 0x505000FF, 0x5028007F); // right inner
        drawer.draw(pose, bufferBuilder, x0, y0, x1, y0 + 1, z, 0x505000FF, 0x505000FF); // top inner
        drawer.draw(pose, bufferBuilder, x0, y1 - 1, x1, y1, z, 0x5028007F, 0x5028007F); // bottom inner
    }

    @FunctionalInterface
    public interface BoxDrawer {
        void draw(Matrix4f pose, BufferBuilder bufferBuilder, int x0, int y0, int x1, int y1, int z, int color0, int color1);
    }
}
