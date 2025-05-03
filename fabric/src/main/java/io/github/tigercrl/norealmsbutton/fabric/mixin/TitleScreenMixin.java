package io.github.tigercrl.norealmsbutton.fabric.mixin;

import io.github.tigercrl.norealmsbutton.mixin.AbstractWidgetAccessor;
import io.github.tigercrl.norealmsbutton.mixin.ScreenAccessor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    public void fixButton(CallbackInfo ci) {
        final boolean[] shouldFixPos = {false};
        ((ScreenAccessor) this).getRenderables().forEach(r -> {
            if (r instanceof Button b && !(b instanceof PlainTextButton)) {
                if (((AbstractWidgetAccessor) b).getMessage().equals(Component.translatable("menu.online")))
                    shouldFixPos[0] = true;
                if (shouldFixPos[0]) b.setY(b.getY() - 25);
            }
        });
    }
}
