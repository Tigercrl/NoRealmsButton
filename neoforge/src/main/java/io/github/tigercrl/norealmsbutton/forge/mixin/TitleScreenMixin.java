package io.github.tigercrl.norealmsbutton.forge.mixin;

import io.github.tigercrl.norealmsbutton.NoRealmsButtonClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    public void fixModButton(CallbackInfo ci) {
        ScreenAccessor accessor = ((ScreenAccessor) this);
        NoRealmsButtonClient.adjustTitleScreen(
                accessor.getChildren().stream()
                        .filter(b -> b instanceof Button)
                        .map(b -> (Button) b)
                        .toList(),
                b -> {
                    accessor.getRenderables().remove(b);
                    accessor.getNarratables().remove(b);
                    accessor.getChildren().remove(b);
                }
        );
    }
}
