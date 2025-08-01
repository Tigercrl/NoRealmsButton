package io.github.tigercrl.norealmsbutton;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class NoRealmsButtonClient {
    public static void adjustTitleScreen(List<Button> buttons, Consumer<Button> removeButton) {
        Button realmsButton = null;
        for (Button b : buttons) {
            if (realmsButton != null && b.getY() >= realmsButton.getY() && !(b instanceof PlainTextButton) && b.visible) {
                b.setY(b.getY() - 24);
            }
            if (b.getMessage().equals(Component.translatable("menu.online"))) {
                realmsButton = b;
            }
        }
        if (realmsButton != null) removeButton.accept(realmsButton);
    }
}
