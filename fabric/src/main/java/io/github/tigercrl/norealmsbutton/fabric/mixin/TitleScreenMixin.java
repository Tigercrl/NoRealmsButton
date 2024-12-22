package io.github.tigercrl.norealmsbutton.fabric.mixin;

import io.github.tigercrl.norealmsbutton.mixin.ScreenInvoker;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Redirect(method = "createNormalMenuOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addButton(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;"))
    public AbstractWidget returnEmptyRealmsButton(TitleScreen instance, AbstractWidget abstractWidget) {
        if (abstractWidget instanceof Button) {
            Button b = (Button) abstractWidget;
            if (b.getMessage().equals(new TranslatableComponent("menu.online"))) {
                b.setAlpha(0);
                b.visible = false;
                b.y = b.y - 25;
            }
        }
        return ((ScreenInvoker) instance).invokeAddButton(abstractWidget);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addButton(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;"))
    public AbstractWidget fixPos(TitleScreen instance, AbstractWidget abstractWidget) {
        if (abstractWidget instanceof Button) {
            Button b = (Button) abstractWidget;
            b.y = b.y - 25;
        }
        return ((ScreenInvoker) instance).invokeAddButton(abstractWidget);
    }
}
