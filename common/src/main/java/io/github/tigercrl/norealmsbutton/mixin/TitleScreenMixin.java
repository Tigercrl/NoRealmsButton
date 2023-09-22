package io.github.tigercrl.norealmsbutton.mixin;

import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(method = "realmsNotificationsEnabled", at = @At("RETURN"), cancellable = true)
    public void realmsNotificationsEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Redirect(method = "createNormalMenuOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"))
    public Button.Builder returnEmptyRealmsButton(Button.Builder instance, int i, int j, int k, int l) {
        if (instance.build().getMessage().equals(Component.translatable("menu.online")))
            return instance.bounds(i, j - 24, 2, 0);
        else return instance.bounds(i, j, k, l);
    }

    @Redirect(method = "added", at = @At(value = "INVOKE", target = "Lcom/mojang/realmsclient/gui/screens/RealmsNotificationsScreen;added()V"))
    public void doNothing(RealmsNotificationsScreen instance) {
    }
}
