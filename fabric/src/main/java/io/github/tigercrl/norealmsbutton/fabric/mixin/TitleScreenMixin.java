package io.github.tigercrl.norealmsbutton.fabric.mixin;

import io.github.tigercrl.norealmsbutton.mixin.AbstractWidgetAccessor;
import io.github.tigercrl.norealmsbutton.mixin.ScreenInvoker;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Redirect(method = "createNormalMenuOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public GuiEventListener returnEmptyRealmsButton(TitleScreen instance, GuiEventListener guiEventListener) {
        if (guiEventListener instanceof Button b && ((AbstractWidgetAccessor) b).getMessage().equals(Component.translatable("menu.online"))) {
            b.setAlpha(0);
            b.visible = false;
            b.setY(b.getY() - 25);
            b.setFocused(false);
        }
        return ((ScreenInvoker) instance).invokeAddRenderableWidget(guiEventListener);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public GuiEventListener fixPos(TitleScreen instance, GuiEventListener guiEventListener) {
        if (guiEventListener instanceof Button b && !(guiEventListener instanceof PlainTextButton))
            b.setY(b.getY() - 25);
        return ((ScreenInvoker) instance).invokeAddRenderableWidget(guiEventListener);
    }
}
