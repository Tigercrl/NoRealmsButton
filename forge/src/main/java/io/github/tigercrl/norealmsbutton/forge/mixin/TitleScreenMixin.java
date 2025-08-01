package io.github.tigercrl.norealmsbutton.forge.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    public void fixModButton(CallbackInfo ci) {
        ((ScreenAccessor) this).getRenderables().forEach(r -> {
            if (r instanceof Button b) {
                if (b.getMessage().equals(Component.translatable("fml.menu.mods")))
                    b.setWidth(200); // widen mods button
                if (b.getMessage().equals(Component.translatable("menu.online")))
                    b.setY(-99999); // move realms button outside the screen
            }
        });
    }
}
