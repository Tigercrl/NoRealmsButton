package top.tigercrl.norealmsbutton.mixin;

import net.minecraft.client.gui.screens.TitleScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? >=1.19 {
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
//? } else
//import net.minecraft.client.gui.screens.Screen;

//? forge || neoforge {
/*import top.tigercrl.norealmsbutton.NoRealmsButton;
//? <=1.21 {
import net.minecraft.client.gui.components.Button;
//~ if >=1.19 'TranslatableComponent' -> 'Component'
import net.minecraft.network.chat.Component;
//? }
*///? }


@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Shadow
    @Nullable
            //~ if >=1.19 'Screen ' -> 'RealmsNotificationsScreen '
    private RealmsNotificationsScreen realmsNotificationsScreen;

    // remove realms screen
    @Inject(method = "init", at = @At("TAIL"))
    public void removeRealmsScreen(CallbackInfo ci) {
        realmsNotificationsScreen = null;
    }

    // remove realms notifications
    @Inject(method = "realmsNotificationsEnabled", at = @At("RETURN"), cancellable = true)
    public void realmsNotificationsEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    //? forge || neoforge {
    /*@Inject(method = "init", at = @At("TAIL"))
    public void fixModButton(CallbackInfo ci) {
        ScreenAccessor accessor = ((ScreenAccessor) this);
        NoRealmsButton.adjustTitleScreen(
                accessor.getChildren(),
                b -> {
                    accessor.getRenderables().remove(b);
                    accessor.getNarratables().remove(b);
                    accessor.getChildren().remove(b);
                },
                //$ if <=1.21 'false' else 'true'
                false
        );

        //? <=1.21 {
        accessor.getChildren().forEach(w -> {
            //~ if >=1.19 'new TranslatableComponent' -> 'Component.translatable'
            if (w instanceof Button b && b.getMessage().equals(Component.translatable("fml.menu.mods"))) {
                b.setWidth(200); // widen mods button
            }
        });
        //? }
    }
    *///? }
}
