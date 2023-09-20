package io.github.tigercrl.norealmsbutton.fabric;

import io.github.tigercrl.norealmsbutton.NoRealmsButton;
import net.fabricmc.api.ModInitializer;

public class NoRealmsButtonFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NoRealmsButton.init();
    }
}