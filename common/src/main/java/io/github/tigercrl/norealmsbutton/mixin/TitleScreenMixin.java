package io.github.tigercrl.norealmsbutton.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(method = "realmsNotificationsEnabled", at = @At("RETURN"), cancellable = true)
    public void realmsNotificationsEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void returnEmptyRealmsButton(CallbackInfo ci) {
        ScreenAccessor accessor = (ScreenAccessor) this;
        accessor.getRenderables().forEach(r -> {
            if (r instanceof Button b && ((AbstractWidgetAccessor) b).getMessage().equals(Component.translatable("menu.online"))) {
                b.setAlpha(0);
                b.visible = false;
                b.setFocused(false);
                accessor.getChildren().remove(b);
                accessor.getNarratables().remove(b);
            }
        });
    }
}
