package top.tigercrl.norealmsbutton.loaders.fabric;

//? fabric {

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.TitleScreen;
import top.tigercrl.norealmsbutton.NoRealmsButton;

import java.util.List;

//~ if >=1.21.11 'ResourceLocation' -> 'Identifier'
import net.minecraft.resources.Identifier;

public class FabricClientEntrypoint implements ClientModInitializer {
    //~ if >=1.21 '(' -> '.fromNamespaceAndPath('
    //~ if >=1.21 '= new ' -> '= '
    //~ if >=26.1 'ResourceLocation' -> 'Identifier'
    public static final Identifier REALMS_REMOVE_PHASE = Identifier.fromNamespaceAndPath(NoRealmsButton.MOD_ID, "realms_remove");

    @Override
    public void onInitializeClient() {
        // run after other listeners
        ScreenEvents.AFTER_INIT.addPhaseOrdering(Event.DEFAULT_PHASE, REALMS_REMOVE_PHASE);

        ScreenEvents.AFTER_INIT.register(REALMS_REMOVE_PHASE, (minecraft, s, scaledWidth, scaledHeight) -> {
            if (!(s instanceof TitleScreen)) return;
            //~ if >=26.1 'getButtons' -> 'getWidgets'
            List<AbstractWidget> buttons = Screens.getWidgets(s);
            NoRealmsButton.adjustTitleScreen(buttons, buttons::remove, true);
        });
    }
}
//? }
