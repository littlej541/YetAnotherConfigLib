package dev.isxander.yacl.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class RequireRestartScreen extends ConfirmScreen {
    public RequireRestartScreen(Screen parent) {
        super(option -> {
            if (option) Minecraft.getInstance().stop();
            else Minecraft.getInstance().setScreen(parent);
        },
                new TranslatableComponent("yacl.restart.title").withStyle(ChatFormatting.RED, ChatFormatting.BOLD),
                new TranslatableComponent("yacl.restart.message"),
                new TranslatableComponent("yacl.restart.yes"),
                new TranslatableComponent("yacl.restart.no")
        );
    }
}
