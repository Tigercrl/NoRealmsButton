package top.tigercrl.norealmsbutton.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
//~ if >=1.21 'screens.' -> 'screens.options.'
import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(OnlineOptionsScreen.class)
public class OnlineOptionsScreenMixin {
    // remove realms settings
    //~ if >=1.21 'createOnlineOptionsScreen' -> 'options'
    @WrapOperation(method = "options", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static <E> boolean removeRealmsOption(List<E> instance, E e, Operation<Boolean> original, @Local(argsOnly = true, name = "options") Options options) {
        if (e instanceof OptionInstance<?> optionInstance && optionInstance.equals(options.realmsNotifications())) {
            return true;
        }
        return original.call(instance, e);
    }
}
