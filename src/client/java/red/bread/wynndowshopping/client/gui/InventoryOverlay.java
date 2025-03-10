package red.bread.wynndowshopping.client.gui;

import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import oshi.util.tuples.Pair;
import red.bread.wynndowshopping.client.WynndowshoppingClient;
import red.bread.wynndowshopping.client.item.WynnItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class InventoryOverlay {
    private int currentPage = 1;
    private int totalPages = 1;
    private int sortingIndex = -1;
    private final List<Pair<ItemStack, WynnItem>> items;
    private final Screen screen;
    private final int scaledWidth;
    private final int scaledHeight;
    private final int startX;
    private final Consumer<String> onSearchFieldChange;
    private final SearchTextFieldWidget searchTextFieldWidget;
    private boolean hasChanged = true;
    private final int slotSize = 20;
    private final int pageControlHeight = 40;
    private final int filterHeight = 40;
    private final int overlayWidth;
    private final int overlayHeight;
    private final List<ClickableWidget> preExistingButtons;
    private final List<Pair<String, String>> sortings;


    InventoryOverlay(List<Pair<ItemStack, WynnItem>> items, Screen screen, int scaledWidth, int scaledHeight, Consumer<String> onSearchFieldChange) {
        this.items = items;
        this.screen = screen;
        this.onSearchFieldChange = onSearchFieldChange;
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
        this.startX = Math.max(scaledWidth / 2 + 119, 3 * scaledWidth / 4);
        this.overlayWidth = (scaledWidth - startX);
        this.overlayHeight = scaledHeight - pageControlHeight - filterHeight;
        this.searchTextFieldWidget = getSearchField(screen, scaledWidth, scaledHeight);
        preExistingButtons = new ArrayList<>(Screens.getButtons(screen));
        sortings = List.of(new Pair<>("Rarity", "By Rarity"), new Pair<>("A-Z", "Alphabetically"), new Pair<>("Lvl↑", "By Level Ascending"), new Pair<>("Lvl↓", "By Level Descending"), new Pair<>("Type", "By Type"));
        switchSorting();
        updatePageCounts(items.size());
    }

    public void redraw() {
        if (!hasChanged) {
            return;
        }
        hasChanged = false;
        Screens.getButtons(screen).clear();
        Screens.getButtons(screen).addAll(preExistingButtons);
        Screens.getButtons(screen).add(searchTextFieldWidget);
        if (shouldRenderItems()) {
            final int itemsPerRow = overlayWidth / slotSize;
            List<Pair<ItemStack, WynnItem>> filteredItems = items.stream().filter(itemStack -> itemStack.getA().getName().getString().toLowerCase().contains(WynndowshoppingClient.currentSearchText.toLowerCase())).toList();
            updatePageCounts(filteredItems.size());
            Screens.getButtons(screen).add(getPrevButton());
            Screens.getButtons(screen).add(getPageDisplayButton());
            Screens.getButtons(screen).add(getNextButton());
            Screens.getButtons(screen).addAll(getFilterButtons());
            final int itemsPerPage = getItemsPerPage();
            List<Pair<ItemStack, WynnItem>> currentPageItems = filteredItems.subList((currentPage -1) * itemsPerPage, Math.min(currentPage * itemsPerPage, filteredItems.size()));
            for (int i = 0; i < currentPageItems.size(); i++) {
                ItemStack itemStack = currentPageItems.get(i).getA();
                WynnItem wynnItem = currentPageItems.get(i).getB();
                int x = startX + slotSize * (i % itemsPerRow);
                int y = pageControlHeight + (i / itemsPerRow) * slotSize;
                Screens.getButtons(screen).add(new ItemButtonWidget(x, y, slotSize, itemStack, wynnItem));
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

    private void switchSorting() {
        this.sortingIndex = (sortingIndex + 1) % sortings.size();
        // Always make sure items are secondarily sorted by level
        items.sort(Comparator.comparing(o -> -o.getB().requirements.level));
        switch (sortingIndex) {
            case 0 -> {
                items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().getItemTypeValue()));
                items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().getRarityValue()));
            }
            case 1 -> items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getA().getName().getString()));
            case 2 -> items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().requirements.level));
            case 3 -> items.sort(Comparator.comparing(itemStackWynnItemPair -> -itemStackWynnItemPair.getB().requirements.level));
            case 4 -> items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().getItemTypeValue()));
        }
        hasChanged = true;
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

    private List<ElevatedButtonWidget> getFilterButtons() {
        List<ElevatedButtonWidget> filterButtons = new ArrayList<>();
        final int y = scaledHeight - 3 * filterHeight / 4;
        filterButtons.add(new ElevatedButtonWidget(startX, y, 40, 20, Text.of(sortings.get(sortingIndex).getA()), Text.of("Sort " + sortings.get(sortingIndex).getB()), button -> {
            switchSorting();
        }));
        return filterButtons;
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
