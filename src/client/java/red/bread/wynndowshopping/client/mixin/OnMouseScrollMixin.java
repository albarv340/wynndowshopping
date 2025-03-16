package red.bread.wynndowshopping.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.bread.wynndowshopping.client.customevents.OnMouseScrollCallback;

@Mixin(Mouse.class)
public class OnMouseScrollMixin {
    @Shadow private double x;

    @Shadow private double y;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        try {
            double scale = (double)this.client.getWindow().getScaledWidth() / (double)this.client.getWindow().getWidth();
            ActionResult result = OnMouseScrollCallback.EVENT.invoker().onMouseScroll(this.x * scale, this.y * scale, vertical);
            if (result == ActionResult.FAIL) {
                ci.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
