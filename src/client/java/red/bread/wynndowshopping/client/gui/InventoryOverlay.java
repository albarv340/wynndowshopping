package red.bread.wynndowshopping.client.gui;

import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;
import red.bread.wynndowshopping.client.WynndowshoppingClient;
import red.bread.wynndowshopping.client.item.WynnItem;
import red.bread.wynndowshopping.client.util.Filter;
import red.bread.wynndowshopping.client.util.ItemStackBuilder;
import red.bread.wynndowshopping.client.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class InventoryOverlay {
    private static int currentPage = 1;
    private static int totalPages = 1;
    private static int sortingIndex = 0;
    public static boolean searchLore = false;
    public static String currentSearchText = "";
    @Nullable
    private List<Pair<ItemStack, WynnItem>> items;
    private final Screen screen;
    private final int scaledWidth;
    private final int scaledHeight;
    private final int startX;
    private final Consumer<String> onSearchFieldChange;
    private final SearchTextFieldWidget searchTextFieldWidget;
    private final CheckboxWidget searchLoreCheckbox;
    private boolean hasChanged = true;
    private final int slotSize = 20;
    private final int pageControlHeight = 40;
    private final int filterHeight = 40;
    private final int overlayWidth;
    private final int overlayHeight;
    private final List<ClickableWidget> preExistingButtons;
    private final List<Pair<String, String>> sortings;
    private static final ItemFilterGuiScreen itemFilterGuiScreen = new ItemFilterGuiScreen();


    InventoryOverlay(@Nullable List<Pair<ItemStack, WynnItem>> items, Screen screen, int scaledWidth, int scaledHeight, Consumer<String> onSearchFieldChange) {
        this.items = items;
        this.screen = screen;
        this.onSearchFieldChange = onSearchFieldChange;
        this.scaledWidth = scaledWidth;
        this.scaledHeight = scaledHeight;
        this.startX = Math.max(scaledWidth / 2 + 119, 3 * scaledWidth / 4);
        this.overlayWidth = (scaledWidth - startX);
        this.overlayHeight = scaledHeight - pageControlHeight - filterHeight;
        this.searchTextFieldWidget = getSearchField(screen, scaledWidth, scaledHeight);
        this.searchLoreCheckbox = CheckboxWidget.builder(Text.of(""), MinecraftClient.getInstance().textRenderer).pos((scaledWidth / 2) + 90, scaledHeight - 19).checked(searchLore).callback((checkbox, checked) -> {
            searchLore = checked;
            hasChanged = true;
        }).build();
        this.searchLoreCheckbox.setTooltip(Tooltip.of(Text.of("Search Lore")));
        preExistingButtons = new ArrayList<>(Screens.getButtons(screen));
        sortings = List.of(new Pair<>("Rarity", "By Rarity"), new Pair<>("A-Z", "Alphabetically"), new Pair<>("Lvl↑", "By Level Ascending"), new Pair<>("Lvl↓", "By Level Descending"), new Pair<>("Type", "By Type"), new Pair<>("Filter", "By Active Filter"));
        if (items != null) {
            updateSort();
            updatePageCounts(items.size());
        }
    }

    public void redraw() {
        if (!hasChanged) {
            return;
        }
        hasChanged = false;
        Screens.getButtons(screen).clear();
        Screens.getButtons(screen).addAll(preExistingButtons);
        Screens.getButtons(screen).add(searchTextFieldWidget);
        Screens.getButtons(screen).add(searchLoreCheckbox);
        if (shouldRenderItems()) {
            if (items == null) {
                Screens.getButtons(screen).add(new ElevatedButtonWidget(startX, 0, overlayWidth, 20, Text.of("Error fetching items"), Text.of("Click here to retry"), button -> {
                    button.setMessage(Text.of("Retrying..."));
                    WynndowshoppingClient.updateItemsFromAPI(stringWynnItemMap -> {
                        this.items = ItemStackBuilder.getAllItems(stringWynnItemMap);
                        hasChanged = true;
                    });
                }));
                return;
            }
            final int itemsPerRow = overlayWidth / slotSize;
            List<Pair<ItemStack, WynnItem>> filteredItems = getFilteredItems();
            updatePageCounts(filteredItems.size());
            Screens.getButtons(screen).add(getPrevButton());
            Screens.getButtons(screen).add(getPageDisplayButton());
            Screens.getButtons(screen).add(getNextButton());
            Screens.getButtons(screen).addAll(getFilterButtons());
            final int itemsPerPage = getItemsPerPage();
            List<Pair<ItemStack, WynnItem>> currentPageItems = filteredItems.subList((currentPage - 1) * itemsPerPage, Math.min(currentPage * itemsPerPage, filteredItems.size()));
            for (int i = 0; i < currentPageItems.size(); i++) {
                ItemStack itemStack = currentPageItems.get(i).getA();
                WynnItem wynnItem = currentPageItems.get(i).getB();
                int x = startX + slotSize * (i % itemsPerRow);
                int y = pageControlHeight + (i / itemsPerRow) * slotSize;
                Screens.getButtons(screen).add(new ItemButtonWidget(x, y, slotSize, itemStack, wynnItem));
            }
        } else if (items != null) {
            updatePageCounts(items.size());
        }
    }

    public void renderBackground(DrawContext drawContext) {
        final int backgroundColor = new Color(0, 0, 0, 150).getRGB();
        drawContext.fill(startX, 0, scaledWidth, scaledHeight, backgroundColor);
    }

    public boolean shouldRenderItems() {
        return WynndowshoppingClient.isInteractedWith;
    }

    private int getItemsPerPage() {
        final int itemsPerRow = overlayWidth / slotSize;
        final int itemsPerColumn = overlayHeight / slotSize;
        return itemsPerRow * itemsPerColumn;
    }

    private void switchSorting() {
        sortingIndex = (sortingIndex + 1) % sortings.size();
        updateSort();
    }

    private List<Pair<ItemStack, WynnItem>> getFilteredItems() {
        List<Pair<ItemStack, WynnItem>> filteredItems = new ArrayList<>();
        if (items == null) {
            return filteredItems;
        }
        filteredItems = items.stream().filter(itemStack -> itemStack.getB().requirements.level >= itemFilterGuiScreen.levelMin && itemStack.getB().requirements.level <= itemFilterGuiScreen.levelMax).toList();
        filteredItems = filteredItems.stream().filter(itemStack -> Utils.itemMatches(itemStack.getA(), InventoryOverlay.currentSearchText, InventoryOverlay.searchLore)).toList();
        for (Filter filter : itemFilterGuiScreen.getItemFilters()) {
            if (filter.value.isEmpty()) {
                continue;
            }
            switch (filter.getOption()) {
                case "type" -> {
                    switch (filter.comparator) {
                        case EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().isOfType(filter.value)).toList();
                        case NOT_EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> !itemStackWynnItemPair.getB().isOfType(filter.value)).toList();
                    }
                }
                case "rarity" -> {
                    switch (filter.comparator) {
                        case EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().rarity != null && itemStackWynnItemPair.getB().rarity.equals(filter.value)).toList();
                        case NOT_EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().rarity != null && !itemStackWynnItemPair.getB().rarity.equals(filter.value)).toList();
                    }
                }
                case "tier" -> {
                    switch (filter.comparator) {
                        case EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().tier != null && itemStackWynnItemPair.getB().tier.equals(filter.value)).toList();
                        case NOT_EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().tier != null && !itemStackWynnItemPair.getB().tier.equals(filter.value)).toList();
                    }
                }
                case "restriction" -> {
                    switch (filter.comparator) {
                        case EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().restrictions != null && itemStackWynnItemPair.getB().restrictions.equals(filter.value)).toList();
                        case NOT_EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().restrictions != null && !itemStackWynnItemPair.getB().restrictions.equals(filter.value)).toList();
                    }
                }
                case "majorId" -> {
                    switch (filter.comparator) {
                        case EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().hasMajorId(filter.value)).toList();
                        case NOT_EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> !itemStackWynnItemPair.getB().hasMajorId(filter.value)).toList();
                    }
                }
                case "profession" -> {
                    switch (filter.comparator) {
                        case EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().getProfessions().contains(filter.value)).toList();
                        case NOT_EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> !itemStackWynnItemPair.getB().getProfessions().contains(filter.value)).toList();
                    }
                }
                case "identification", "base" -> {
                    switch (filter.comparator) {
                        case EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().hasIdentification(filter.value)).toList();
                        case NOT_EXISTS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> !itemStackWynnItemPair.getB().hasIdentification(filter.value)).toList();
                        case LT ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().hasIdentification(filter.value) && itemStackWynnItemPair.getB().getRawIdentificationValue(filter.value) < filter.constant).toList();
                        case LTE ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().hasIdentification(filter.value) && itemStackWynnItemPair.getB().getRawIdentificationValue(filter.value) <= filter.constant).toList();
                        case GT ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().hasIdentification(filter.value) && itemStackWynnItemPair.getB().getRawIdentificationValue(filter.value) > filter.constant).toList();
                        case GTE ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().hasIdentification(filter.value) && itemStackWynnItemPair.getB().getRawIdentificationValue(filter.value) >= filter.constant).toList();
                        case EQUALS ->
                                filteredItems = filteredItems.stream().filter(itemStackWynnItemPair -> itemStackWynnItemPair.getB().hasIdentification(filter.value) && itemStackWynnItemPair.getB().getRawIdentificationValue(filter.value) == filter.constant).toList();
                    }
                    if (filter.comparator != red.bread.wynndowshopping.client.util.Comparator.NOT_EXISTS) {
                        filteredItems = filteredItems.stream().sorted((o1, o2) -> o2.getB().getRawIdentificationValue(filter.value) - o1.getB().getRawIdentificationValue(filter.value)).toList();
                        sortingIndex = 5;
                    }
                }
            }
        }
        return filteredItems;
    }

    private void updateSort() {
        // Always make sure items are secondarily sorted by level
        if (items == null) {
            return;
        }
        items.sort(Comparator.comparing(o -> -o.getB().requirements.level));
        switch (sortingIndex) {
            case 0 -> {
                items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().getItemTypeValue()));
                items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().getRarityValue()));
            }
            case 1 ->
                    items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getA().getName().getString()));
            case 2 ->
                    items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().requirements.level));
            case 3 ->
                    items.sort(Comparator.comparing(itemStackWynnItemPair -> -itemStackWynnItemPair.getB().requirements.level));
            case 4 ->
                    items.sort(Comparator.comparing(itemStackWynnItemPair -> itemStackWynnItemPair.getB().getItemTypeValue()));
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
        return new ElevatedButtonWidget(startX + overlayWidth / 4, pageControlHeight / 4, overlayWidth / 2, pageControlHeight / 2, Text.of(String.format("Page %d/%d", currentPage, totalPages)), button -> {
        });
    }

    private List<ElevatedButtonWidget> getFilterButtons() {
        List<ElevatedButtonWidget> filterButtons = new ArrayList<>();
        final int y = scaledHeight - 3 * filterHeight / 4;
        filterButtons.add(new ElevatedButtonWidget(startX, y, overlayWidth / 4, 20, Text.of(sortings.get(sortingIndex).getA()), Text.of("Sort " + sortings.get(sortingIndex).getB()), button -> {
            switchSorting();
        }));
        filterButtons.add(new ElevatedButtonWidget(startX + overlayWidth / 4, y, overlayWidth / 2, 20, Text.of("Clear Filters"), Text.of("Clear Filters"), button -> {
            itemFilterGuiScreen.clearFilters();
            hasChanged = true;
        }));
        filterButtons.add(new ElevatedButtonWidget(scaledWidth - overlayWidth / 4, y, overlayWidth / 4, 20, Text.of("Filters"), Text.of("Open Filter Menu"), button -> {
            itemFilterGuiScreen.open(screen);
        }));
        return filterButtons;
    }

    private SearchTextFieldWidget getSearchField(Screen screen, int scaledWidth, int scaledHeight) {
        final int HEIGHT = 20;
        final int WIDTH = 180;
        SearchTextFieldWidget searchTextFieldWidget;
        searchTextFieldWidget = new SearchTextFieldWidget(screen.getTextRenderer(), (scaledWidth / 2) - WIDTH / 2, scaledHeight - HEIGHT - 1, WIDTH, HEIGHT, Text.of(currentSearchText));
        searchTextFieldWidget.setMaxLength(1000);
        searchTextFieldWidget.setText(currentSearchText);
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
