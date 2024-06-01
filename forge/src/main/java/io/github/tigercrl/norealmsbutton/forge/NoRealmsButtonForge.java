package io.github.tigercrl.norealmsbutton.forge;

import io.github.tigercrl.norealmsbutton.NoRealmsButton;
import net.minecraftforge.fml.common.Mod;

@Mod(NoRealmsButton.MOD_ID)
public class NoRealmsButtonForge {
    public NoRealmsButtonForge() {
        NoRealmsButton.init();
    }
}