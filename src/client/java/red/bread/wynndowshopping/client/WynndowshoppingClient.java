package red.bread.wynndowshopping.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import red.bread.wynndowshopping.client.gui.InventoryRenderer;
import red.bread.wynndowshopping.client.item.*;
import red.bread.wynndowshopping.client.util.WebRequest;

import java.lang.reflect.Type;
import java.util.Map;

public class WynndowshoppingClient implements ClientModInitializer {
    public static boolean cancelContainerClose = false;
    public static boolean highlightSearchedString = false;
    public static String currentSearchText = "";
    public static Map<String, WynnItem> items;

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof HandledScreen<?>) {
                InventoryRenderer.init(screen, scaledWidth, scaledHeight);
                ScreenEvents.afterRender(screen).register((screen1, drawContext, mouseX, mouseY, tickDelta) -> InventoryRenderer.render(screen1, client, drawContext, scaledWidth, scaledHeight));
            }
        });
        updateItemsFromAPI();
    }

    private static void updateItemsFromAPI() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<Map<String, Identification>>() {
                }.getType(), new IdentificationDeserializer())
                .create();
        new Thread(() -> {
            try {
                String allItemDataString = WebRequest.getData("https://api.wynncraft.com/v3/item/database?fullResult");
                Type type = new TypeToken<Map<String, WynnItem>>() {
                }.getType();
                items = gson.fromJson(allItemDataString, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
