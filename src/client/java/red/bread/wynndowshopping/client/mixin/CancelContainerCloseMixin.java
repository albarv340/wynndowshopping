package red.bread.wynndowshopping.client.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.bread.wynndowshopping.client.WynndowshoppingClient;

@Mixin(HandledScreen.class)
public class CancelContainerCloseMixin {

    @Inject(method = "close()V", at = @At("HEAD"), cancellable = true)
    private void injected(CallbackInfo ci) {
        if (WynndowshoppingClient.cancelContainerClose) {
            ci.cancel();
            WynndowshoppingClient.cancelContainerClose = false;
        }
    }
}
