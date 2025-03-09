package red.bread.wynndowshopping.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import red.bread.wynndowshopping.client.WynndowshoppingClient;
import red.bread.wynndowshopping.client.item.WynnItem;
import red.bread.wynndowshopping.client.util.ItemStackBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class InventoryRenderer {
    private static InventoryOverlay inventoryOverlay;

    public static void init(Screen screen, int scaledWidth, int scaledHeight) {
        List<ItemStack> items = new ArrayList<>();
//        for (int i = 1; i < 1000; i++) {
//            items.add(new ItemStack(RegistryEntry.of(Item.byRawId(i))));
//        }
        for (Map.Entry<String, WynnItem> wynnItem : WynndowshoppingClient.items.entrySet()) {
            items.add(ItemStackBuilder.buildItem(wynnItem.getKey(), wynnItem.getValue()));
        }
        inventoryOverlay = new InventoryOverlay(items, screen, scaledWidth, scaledHeight, s -> {
            WynndowshoppingClient.currentSearchText = s;
            updateHighlightedSlots();
        });
        inventoryOverlay.redraw();
    }

    private static final Set<Integer> highlightedSlots = new HashSet<>();

    public static void render(Screen screen1, MinecraftClient client, DrawContext drawContext, int scaledWidth, int scaledHeight) {
        if (client.player == null) {
            return;
        }
        updateHighlightedSlots();
        List<Slot> slots = client.player.currentScreenHandler.slots;
        final int slotSize = 18;

        final int searchMissColor = new Color(0, 0, 0, 150).getRGB();
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(0.0, 0.0, 358F);
        int screenX = ((HandledScreen<?>) screen1).x;
        int screenY = ((HandledScreen<?>) screen1).y;
        highlightMatchingSLots(drawContext, slots, screenX, screenY, slotSize, searchMissColor);
        drawContext.getMatrices().pop();
        if (inventoryOverlay.shouldRenderItems()) {
            inventoryOverlay.renderBackground(drawContext);
            inventoryOverlay.redraw();
        }
    }

    private static void highlightMatchingSLots(DrawContext drawContext, List<Slot> slots, int screenX, int screenY, int slotSize, int searchMissColor) {
        if (!WynndowshoppingClient.highlightSearchedString) {
            return;
        }
        for (Slot slot : slots) {
            int x = slot.x + screenX - 1;
            int y = slot.y + screenY - 1;
            if (!highlightedSlots.contains(slot.id)) {
                drawContext.fill(x, y, x + slotSize, y + slotSize, searchMissColor);
            }
        }
    }

    public static void updateHighlightedSlots() {
        if (MinecraftClient.getInstance().player != null) {
            DefaultedList<Slot> containerSlots = MinecraftClient.getInstance().player.currentScreenHandler.slots;
            highlightedSlots.clear();
            if (WynndowshoppingClient.currentSearchText.isEmpty()) return;
            for (Slot slot : containerSlots) {
                String itemName = slot.getStack().getName().getString();
                if (itemName.equals("Air")) continue;
                if (itemName.toLowerCase().contains(WynndowshoppingClient.currentSearchText.toLowerCase())) {
                    highlightedSlots.add(slot.id);
                }
            }
        }

    }
}
