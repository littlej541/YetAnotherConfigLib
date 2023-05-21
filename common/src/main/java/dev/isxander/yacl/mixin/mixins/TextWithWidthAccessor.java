package dev.isxander.yacl.mixin.mixins;

import net.minecraft.client.gui.components.MultiLineLabel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiLineLabel.TextWithWidth.class)
public interface TextWithWidthAccessor {
    @Accessor int getWidth();
}
