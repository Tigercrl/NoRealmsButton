package top.tigercrl.norealmsbutton.mixin;

//~ if >=1.19 'Option' -> 'OptionInstance'
import net.minecraft.client.OptionInstance;
//~ if >=1.21 'screens.' -> 'screens.options.'
import net.minecraft.client.gui.screens.OnlineOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Arrays;

@Mixin(OnlineOptionsScreen.class)
public class OnlineOptionsScreenMixin {
    //~ if >=1.19 'Option;' -> 'OptionInstance;'
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/SimpleOptionsSubScreen;<init>(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/Options;Lnet/minecraft/network/chat/Component;[Lnet/minecraft/client/OptionInstance;)V"), index = 3)
    //~ if >=1.19 'Option[]' -> 'OptionInstance<?>[]'
    private static OptionInstance<?>[] removeRealmsOption(OptionInstance<?>[] original) {
        return Arrays.copyOfRange(original, 1, original.length);
    }
}
