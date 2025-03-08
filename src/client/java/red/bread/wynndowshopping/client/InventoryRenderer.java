package red.bread.wynndowshopping.client;

import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class InventoryRenderer {
    private static SearchTextFieldWidget textFieldWidget;
    private static String currentText = "";

    public static void addSearchField(Screen screen, int scaledWidth, int scaledHeight) {
        init(screen, scaledWidth, scaledHeight);
        Screens.getButtons(screen).add(textFieldWidget);
        ScreenKeyboardEvents.beforeKeyPress(screen).register((screen1, key, scancode, modifiers) -> {
            if (textFieldWidget.isActive() && MinecraftClient.getInstance().options.inventoryKey.matchesKey(key, scancode)) {
                WynndowshoppingClient.cancelContainerClose = true;
            }
        });
        ScreenMouseEvents.beforeMouseClick(screen).register((screen1, mouseX, mouseY, button) -> {
            if (textFieldWidget.isActive() && !textFieldWidget.isMouseOver(mouseX, mouseY)) {
                textFieldWidget.setFocused(false);
            }
        });
    }

    private static final Set<Integer> highlightedSlots = new HashSet<>();

    public static void render(Screen screen1, MinecraftClient client, DrawContext drawContext) {
        if (client.player == null || !WynndowshoppingClient.highlightSearchedString) {
            return;
        }
        updateHighlightedSlots();
        List<Slot> slots = client.player.currentScreenHandler.slots;
        final int slotSize = 16;

        final int searchMissColor = new Color(0, 0, 0, 150).getRGB();
        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(0.0, 0.0, 358F);
        int screenX = ((HandledScreen<?>) screen1).x;
        int screenY = ((HandledScreen<?>) screen1).y;
        for (Slot slot : slots) {
            int x = slot.x + screenX;
            int y = slot.y + screenY;
            if (!highlightedSlots.contains(slot.id)) {
                drawContext.fill(x, y, x + slotSize, y + slotSize, searchMissColor);
            }
        }
        drawContext.getMatrices().pop();
    }

    private static void init(Screen screen, int scaledWidth, int scaledHeight) {
        final int HEIGHT = 20;
        final int WIDTH = 174;
        textFieldWidget = new SearchTextFieldWidget(screen.getTextRenderer(),
                (scaledWidth / 2) - WIDTH / 2,
                scaledHeight - HEIGHT - 1,
                WIDTH,
                HEIGHT,
                Text.of(currentText)
        );
        textFieldWidget.setMaxLength(1000);
        textFieldWidget.setText(currentText);
        textFieldWidget.setChangedListener(value -> {
            currentText = value;
            updateHighlightedSlots();
        });
    }

    public static void reset() {
        textFieldWidget.setText("");
        currentText = "";
        highlightedSlots.clear();
    }

    public static void updateHighlightedSlots() {
        if (MinecraftClient.getInstance().player != null) {
            DefaultedList<Slot> containerSlots = MinecraftClient.getInstance().player.currentScreenHandler.slots;
            highlightedSlots.clear();
            if (currentText.isEmpty()) return;
            for (Slot slot : containerSlots) {
                String itemName = slot.getStack().getName().getString();
                if (itemName.equals("Air")) continue;
                Pattern pattern = Pattern.compile(currentText, Pattern.CASE_INSENSITIVE);
                if (pattern.matcher(itemName).find()) {
                    highlightedSlots.add(slot.id);
                }
            }
        }

    }
}
