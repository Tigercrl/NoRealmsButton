package io.github.tigercrl.norealmsbutton.fabric;

import io.github.tigercrl.norealmsbutton.NoRealmsButton;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public class NoRealmsButtonFabricClient implements ClientModInitializer {
    public static final Identifier REALMS_REMOVE_PHASE = Identifier.fromNamespaceAndPath(NoRealmsButton.MOD_ID, "realms_remove");

    @Override
    public void onInitializeClient() {
        // Run after other listeners.
        ScreenEvents.AFTER_INIT.addPhaseOrdering(Event.DEFAULT_PHASE, REALMS_REMOVE_PHASE);

        ScreenEvents.AFTER_INIT.register(REALMS_REMOVE_PHASE, (minecraft, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof TitleScreen)) {
                return;
            }

            List<AbstractWidget> buttons = new ArrayList<>();

            for (var child : screen.children()) {
                if (child instanceof AbstractWidget widget) {
                    buttons.add(widget);
                }
            }

            Button realmsButton = null;

            for (AbstractWidget button : buttons) {
                if (realmsButton != null && button.getY() >= realmsButton.getY() && !(button instanceof PlainTextButton) && button.visible) {
                    button.setY(button.getY() - 24);
                }

                if (button.getMessage().equals(Component.translatable("menu.online")) && button instanceof Button matchedButton) {
                    realmsButton = matchedButton;
                }
            }

            if (realmsButton != null) {
                realmsButton.visible = false;
                realmsButton.active = false;
            }
        });
    }
}