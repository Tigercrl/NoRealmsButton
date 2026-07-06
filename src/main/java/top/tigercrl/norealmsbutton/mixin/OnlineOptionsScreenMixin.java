package top.tigercrl.norealmsbutton.mixin;

//~ if >=1.19 'Option' -> 'OptionInstance'
import net.minecraft.client.OptionInstance;
//~ if >=1.21 'screens.' -> 'screens.options.'
import net.minecraft.client.gui.screens.OnlineOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=26.2 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.OptionsList;
*///? } elif >=1.20 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Options;
import java.util.List;
//? } else {
/*import org.spongepowered.asm.mixin.injection.ModifyArg;
import java.util.Arrays;
*///? }

@Mixin(OnlineOptionsScreen.class)
public class OnlineOptionsScreenMixin {
    //? if >=26.2 {
    /*@WrapOperation(method = "addOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addHeader(Lnet/minecraft/network/chat/Component;)V"))
    private static void removeRealmsHeader(OptionsList instance, Component component, Operation<Void> original) {
        if (component.equals(Component.translatable("options.online.realms.header"))) {
            return;
        }
        original.call(instance, component);
    }

    @WrapOperation(method = "addOptions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addBig(Lnet/minecraft/client/OptionInstance;)V"))
    private static void removeRealmsOption(OptionsList instance, OptionInstance<?> option, Operation<Void> original) {
        if (option.equals(Minecraft.getInstance().options.realmsNotifications())) {
            return;
        }
        original.call(instance, option);
    }
    *///? } elif >=1.20 {
    //~ if >=1.21 'createOnlineOptionsScreen' -> 'options'
    @WrapOperation(method = "createOnlineOptionsScreen", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static <E> boolean removeRealmsOption(List<E> instance, E e, Operation<Boolean> original, @Local(argsOnly = true) Options options) {
        if (e instanceof OptionInstance<?> optionInstance && optionInstance.equals(options.realmsNotifications())) {
            return true;
        }
        return original.call(instance, e);
    }
    //? } else {
    /*//~ if >=1.19 'Option;' -> 'OptionInstance;'
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/SimpleOptionsSubScreen;<init>(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/Options;Lnet/minecraft/network/chat/Component;[Lnet/minecraft/client/OptionInstance;)V"), index = 3)
    //~ if >=1.19 'Option[]' -> 'OptionInstance<?>[]'
    private static OptionInstance<?>[] removeRealmsOption(OptionInstance<?>[] original) {
        return Arrays.copyOfRange(original, 1, original.length);
    }
    *///? }
}
