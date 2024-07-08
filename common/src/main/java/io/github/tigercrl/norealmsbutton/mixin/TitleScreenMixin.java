package io.github.tigercrl.norealmsbutton.mixin;

import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Shadow
    @Nullable
    private RealmsNotificationsScreen realmsNotificationsScreen;

    @Unique
    private final List<Button> noRealmsButton$fixPosButtons = new ArrayList<>();

    @Inject(method = "tick", at = @At("HEAD"))
    public void realmsNotificationsEnabled(CallbackInfo ci) {
        realmsNotificationsScreen = null;
    }

    @Redirect(method = "createNormalMenuOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public GuiEventListener returnEmptyRealmsButton(TitleScreen instance, GuiEventListener guiEventListener) {
        if (guiEventListener instanceof Button b && ((AbstractWidgetAccessor) b).getMessage().equals(Component.translatable("menu.online"))) {
            b.setAlpha(0);
            b.visible = false;
            b.setFocused(false);
        }
        return ((ScreenInvoker) instance).invokeAddRenderableWidget(guiEventListener);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    public GuiEventListener checkFixPos(TitleScreen instance, GuiEventListener guiEventListener) {
        if (guiEventListener instanceof Button b && !(guiEventListener instanceof PlainTextButton))
            noRealmsButton$fixPosButtons.add(b);
        return ((ScreenInvoker) instance).invokeAddRenderableWidget(guiEventListener);
    }

    @Inject(method = "init", at = @At(value = "RETURN"))
    public void doFixPos(CallbackInfo ci) {
        noRealmsButton$fixPosButtons.forEach(b -> b.setY(b.getY() - 25));
    }
}
