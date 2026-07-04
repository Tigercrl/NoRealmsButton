package top.tigercrl.norealmsbutton;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

public class NoRealmsButton {
    public static final String MOD_ID = "norealmsbutton";

    public static void adjustTitleScreen(List<? extends GuiEventListener> widgets, Consumer<Button> removeButton, boolean moveUp) {
        Button realmsButton = null;
        for (GuiEventListener w : widgets) {
            if (w instanceof Button button) {
                if (moveUp && realmsButton != null && button.getY() >= realmsButton.getY() && !(button instanceof PlainTextButton) && button.visible) {
                    button.setY(button.getY() - 24);
                }
                if (button.getMessage().equals(Component.translatable("menu.online"))) {
                    realmsButton = button;
                }
            }
        }
        if (realmsButton != null) removeButton.accept(realmsButton);
    }
}
