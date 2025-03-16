package red.bread.wynndowshopping.client.customevents;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface OnMouseScrollCallback {
    Event<OnMouseScrollCallback> EVENT = EventFactory.createArrayBacked(OnMouseScrollCallback.class,
            (listeners) -> (mouseX, mouseY, verticalAmount) -> {
                for (OnMouseScrollCallback listener : listeners) {
                    ActionResult result = listener.onMouseScroll(mouseX, mouseY, verticalAmount);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult onMouseScroll(double mouseX, double mouseY, double verticalAmount);
}
