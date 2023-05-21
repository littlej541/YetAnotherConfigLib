package dev.isxander.yacl.mixin.mixins;

import dev.isxander.yacl.mixin.MultiLineLabelWidthInterface;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.MultiLineLabel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net.minecraft.client.gui.components.MultiLineLabel$2")
public class MultiLineLabelMixin implements MultiLineLabelWidthInterface {
    @Unique private int width;

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void captureWidth(Font font, List<MultiLineLabel.TextWithWidth> list, CallbackInfo ci) {
        List<TextWithWidthAccessor> lines = (List<TextWithWidthAccessor>) (Object) list;

        this.setWidth(lines.stream().map(TextWithWidthAccessor::getWidth).max(Integer::compare).orElse(0));
    }


}
