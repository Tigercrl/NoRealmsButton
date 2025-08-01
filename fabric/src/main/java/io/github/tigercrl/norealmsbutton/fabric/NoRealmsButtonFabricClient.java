package io.github.tigercrl.norealmsbutton.fabric;

import io.github.tigercrl.norealmsbutton.NoRealmsButton;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class NoRealmsButtonFabricClient implements ClientModInitializer {
    public static final ResourceLocation REALMS_REMOVE_PHACE = new ResourceLocation(NoRealmsButton.MOD_ID, "realms_remove");

    @Override
    public void onInitializeClient() {
        // run after other listeners
        ScreenEvents.AFTER_INIT.addPhaseOrdering(Event.DEFAULT_PHASE, REALMS_REMOVE_PHACE);

        ScreenEvents.AFTER_INIT.register(REALMS_REMOVE_PHACE, (minecraft, s, scaledWidth, scaledHeight) -> {
            if (!(s instanceof TitleScreen)) return;
            List<AbstractWidget> buttons = Screens.getButtons(s);

            Button realmsButton = null;
            for (AbstractWidget b : buttons) {
                if (realmsButton != null && b.getY() >= realmsButton.getY() && !(b instanceof PlainTextButton) && b.visible) {
                    b.setY(b.getY() - 24);
                }
                if (b.getMessage().equals(Component.translatable("menu.online"))) {
                    realmsButton = (Button) b;
                }
            }
            if (realmsButton != null) buttons.remove(realmsButton);
        });
    }
}