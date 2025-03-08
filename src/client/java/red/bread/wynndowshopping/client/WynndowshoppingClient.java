package red.bread.wynndowshopping.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public class WynndowshoppingClient implements ClientModInitializer {
    public static boolean cancelContainerClose = false;
    public static boolean highlightSearchedString = false;

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof GenericContainerScreen || screen instanceof InventoryScreen) {
                InventoryRenderer.addSearchField(screen, scaledWidth, scaledHeight);
                ScreenEvents.afterRender(screen).register((screen1, drawContext, mouseX, mouseY, tickDelta) -> {
                    InventoryRenderer.render(screen1, client, drawContext, scaledWidth, scaledHeight, mouseX, mouseY, tickDelta);
                });
            }
        });
    }
}
