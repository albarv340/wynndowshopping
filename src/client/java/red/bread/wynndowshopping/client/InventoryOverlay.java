package red.bread.wynndowshopping.client;

import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class InventoryOverlay {
    private int currentPage = 1;
    private int totalPages = 1;
    private final List<ItemStack> items;
    private final Screen screen;
    private final int scaledWidth;
    private final int scaledHeight;
    private final int startX;
    private final Consumer<String> onSearchFieldChange;
    private final SearchTextFieldWidget searchTextFieldWidget;
    private boolean hasChanged = true;
    private final int slotSize = 20;
    private final int pageControlHeight = 40;
    private final int overlayWidth;
    private final int overlayHeight;


    InventoryOverlay(List<ItemStack> items, Screen screen, int scaledWidth, int scaledHeight, Consumer<String> onSearchFieldChange) {
        this.items = items;
        this.screen = screen;
        this.onSearchFieldChange = onSearchFieldChange;
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
        this.startX = scaledWidth / 2 + 119;
        this.overlayWidth = (scaledWidth - startX);
        this.overlayHeight = scaledHeight - pageControlHeight;
        this.searchTextFieldWidget = getSearchField(screen, scaledWidth, scaledHeight);
        updatePageCounts(items.size());
    }

    public void redraw() {
        if (!hasChanged) {
            return;
        }
        hasChanged = false;
        Screens.getButtons(screen).clear();
        Screens.getButtons(screen).add(searchTextFieldWidget);
        if (shouldRenderItems()) {
            final int itemsPerRow = overlayWidth / slotSize;
            List<ItemStack> filteredItems = items.stream().filter(itemStack -> itemStack.getName().getString().toLowerCase().contains(WynndowshoppingClient.currentSearchText.toLowerCase())).toList();
            updatePageCounts(filteredItems.size());
            Screens.getButtons(screen).add(getPrevButton());
            Screens.getButtons(screen).add(getPageDisplayButton());
            Screens.getButtons(screen).add(getNextButton());
            final int itemsPerPage = getItemsPerPage();
            List<ItemStack> currentPageItems = filteredItems.subList((currentPage -1) * itemsPerPage, Math.min(currentPage * itemsPerPage, filteredItems.size()));
            for (int i = 0; i < currentPageItems.size(); i++) {
                ItemStack itemStack = currentPageItems.get(i);
                int x = startX + slotSize * (i % itemsPerRow);
                int y = pageControlHeight + (i / itemsPerRow) * slotSize;
                Screens.getButtons(screen).add(new ItemButtonWidget(x, y, slotSize, itemStack));
            }
        } else {
            updatePageCounts(items.size());
        }
    }

    public void renderBackground(DrawContext drawContext) {
        final int backgroundColor = new Color(0, 0, 0, 150).getRGB();
        drawContext.fill(startX, 0, scaledWidth, scaledHeight, backgroundColor);
    }

    public boolean shouldRenderItems() {
        return searchTextFieldWidget.isInteractedWith;
    }

    private int getItemsPerPage() {
        final int itemsPerRow = overlayWidth / slotSize;
        final int itemsPerColumn = overlayHeight / slotSize;
        return itemsPerRow * itemsPerColumn;
    }

    private void updatePageCounts(int itemAmount) {
        totalPages = itemAmount / getItemsPerPage() + 1;
        if (currentPage > totalPages) {
            currentPage = 1;
        }
    }

    private ElevatedButtonWidget getPrevButton() {
        return new ElevatedButtonWidget(startX, pageControlHeight / 4, overlayWidth / 4, pageControlHeight / 2, Text.of("< Prev"), button -> {
            currentPage--;
            if (currentPage < 1) {
                currentPage = totalPages;
            }
            hasChanged = true;
        });
    }

    private ElevatedButtonWidget getNextButton() {
        return new ElevatedButtonWidget(startX + 3 * overlayWidth / 4, pageControlHeight / 4, overlayWidth / 4, pageControlHeight / 2, Text.of("Next >"), button -> {
            currentPage++;
            if (currentPage > totalPages) {
                currentPage = 1;
            }
            hasChanged = true;
        });
    }

    private ElevatedButtonWidget getPageDisplayButton() {
        return new ElevatedButtonWidget(startX + overlayWidth / 4, pageControlHeight / 4, overlayWidth / 2, pageControlHeight / 2, Text.of(String.format("Page %d/%d", currentPage, totalPages)), button -> {});
    }

    private SearchTextFieldWidget getSearchField(Screen screen, int scaledWidth, int scaledHeight) {
        final int HEIGHT = 20;
        final int WIDTH = 180;
        SearchTextFieldWidget searchTextFieldWidget;
        searchTextFieldWidget = new SearchTextFieldWidget(screen.getTextRenderer(),
                (scaledWidth / 2) - WIDTH / 2,
                scaledHeight - HEIGHT - 1,
                WIDTH,
                HEIGHT,
                Text.of(WynndowshoppingClient.currentSearchText)
        );
        searchTextFieldWidget.setMaxLength(1000);
        searchTextFieldWidget.setText(WynndowshoppingClient.currentSearchText);
        searchTextFieldWidget.setChangedListener(s -> {
            hasChanged = true;
            onSearchFieldChange.accept(s);
        });
        ScreenKeyboardEvents.beforeKeyPress(screen).register((screen1, key, scancode, modifiers) -> {
            if (searchTextFieldWidget.isActive() && MinecraftClient.getInstance().options.inventoryKey.matchesKey(key, scancode)) {
                WynndowshoppingClient.cancelContainerClose = true;
            }
        });
        ScreenMouseEvents.beforeMouseClick(screen).register((screen1, mouseX, mouseY, button) -> {
            if (searchTextFieldWidget.isActive() && !searchTextFieldWidget.isMouseOver(mouseX, mouseY)) {
                searchTextFieldWidget.setFocused(false);
            }
        });
        return searchTextFieldWidget;
    }
}
