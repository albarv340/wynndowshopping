package red.bread.wynndowshopping.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import red.bread.wynndowshopping.client.gui.InventoryRenderer;
import red.bread.wynndowshopping.client.item.*;
import red.bread.wynndowshopping.client.util.ConfigFileUtil;
import red.bread.wynndowshopping.client.util.WebRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class WynndowshoppingClient implements ClientModInitializer {
    public static boolean cancelContainerClose = false;
    public static boolean highlightSearchedString = false;
    public static boolean isInteractedWith = false;
    public static Map<String, WynnItem> items;
    public static Map<String, Set<String>> possibleFilters = new HashMap<>();
    private static boolean isCurrentlyFetchingItemData = false;

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof HandledScreen<?>) {
                InventoryRenderer.init(screen, scaledWidth, scaledHeight);
                ScreenEvents.afterRender(screen).register((screen1, drawContext, mouseX, mouseY, tickDelta) -> InventoryRenderer.render(screen1, client, drawContext, scaledWidth, scaledHeight));
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (MinecraftClient.getInstance().currentScreen == null && isInteractedWith) {
                // Hide the overlay if inventories are exited
                isInteractedWith = false;
            }
        });
        updateItemsFromAPI();
    }

    public static void updateItemsFromAPI() {
        updateItemsFromAPI(stringWynnItemMap -> {
        });
    }

    public static void updateItemsFromAPI(Consumer<Map<String, WynnItem>> callback) {
        if (isCurrentlyFetchingItemData) {
            return;
        }
        isCurrentlyFetchingItemData = true;
        new Thread(() -> {
            try {
                String allItemDataString = WebRequest.getData("https://api.wynncraft.com/v3/item/database?fullResult");
                items = parseAPIStringToItems(allItemDataString);
                callback.accept(items);
                if (items == null) {
                    items = parseAPIStringToItems(ConfigFileUtil.readFile("all-wynncraft-items-backup.json"));
                } else {
                    ConfigFileUtil.writeFile("all-wynncraft-items-backup.json", allItemDataString);
                }
                isCurrentlyFetchingItemData = false;
            } catch (Exception e) {
                items = parseAPIStringToItems(ConfigFileUtil.readFile("all-wynncraft-items-backup.json"));
                e.printStackTrace();
            }
        }).start();
    }

    private static Map<String, WynnItem> parseAPIStringToItems(String apiString) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<Map<String, WynnItem>>() {
                }.getType(), new WynnItemDeserializer())
                .registerTypeAdapter(new TypeToken<Map<String, Identification>>() {
                }.getType(), new IdentificationDeserializer())
                .registerTypeAdapter(DroppedBy.class, new DroppedByDeserializer())
                .create();
        Type type = new TypeToken<Map<String, WynnItem>>() {
        }.getType();
        return gson.fromJson(apiString, type);
    }
}
