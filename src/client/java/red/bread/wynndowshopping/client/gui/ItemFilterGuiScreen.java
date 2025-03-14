package red.bread.wynndowshopping.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import red.bread.wynndowshopping.client.WynndowshoppingClient;
import red.bread.wynndowshopping.client.util.Comparator;
import red.bread.wynndowshopping.client.util.Filter;

import java.util.ArrayList;
import java.util.List;

public class ItemFilterGuiScreen extends Screen {
    private final int labelMenuHeight = 30;
    private Screen previousScreen;

    private final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    private ElevatedButtonWidget backButton;
    private ButtonWidget addFilterButton;
    private final ArrayList<DropdownWidget> filterListOption;
    private final ArrayList<DropdownWidget> filterListValue;
    private final ArrayList<ButtonWidget> filterListComparator;
    private final ArrayList<TextFieldWidget> filterListConstant;
    private final ArrayList<ElevatedButtonWidget> filterListDelete;

    private final ArrayList<Filter> itemFilters = new ArrayList<>();

    private int removeIndex = -1;


    public ItemFilterGuiScreen() {
        super(Text.empty());

        filterListOption = new ArrayList<>();
        filterListValue = new ArrayList<>();
        filterListComparator = new ArrayList<>();
        filterListConstant = new ArrayList<>();
        filterListDelete = new ArrayList<>();
    }

    public void open(Screen previousScreen) {
        this.previousScreen = previousScreen;
        MinecraftClient.getInstance().setScreen(this);
        postInit();
    }

    public void postInit() {
        backButton = new ElevatedButtonWidget(5, 5, 30, 20, Text.literal("Back"), Text.of("Go Back"), (button) -> close());

        addFilterButton = ButtonWidget.builder(Text.literal("Add New Filter"), (button) -> {
            int index = filterListOption.size();

            Filter filter = new Filter();
            itemFilters.add(filter);

            ButtonWidget comparator = ButtonWidget.builder(Text.literal("Matches"), b -> {
                filter.incrementComparator();
                b.setMessage(Text.literal(
                        switch (filter.comparator) {
                            case Comparator.EXISTS: yield "Matches";
                            case Comparator.NOT_EXISTS: yield "Excludes";
                            case Comparator.GTE: yield ">=";
                            case Comparator.GT: yield ">";
                            case Comparator.EQUALS: yield "=";
                            case Comparator.LTE: yield "<=";
                            case Comparator.LT: yield "<";
                        }
                ));

            }).position(380, labelMenuHeight + 5 + index * 25).size(60, 20).tooltip(Tooltip.of(Text.literal("Click to cycle"))).build();

            DropdownWidget valueWidget = new DropdownWidget(textRenderer, 125, labelMenuHeight + 7 + index * 25, 250, Text.literal(""), "", List.of(), (v) -> {
                filter.value = v;
            });
            TextFieldWidget constant = new TextFieldWidget(textRenderer, 445, labelMenuHeight + 8 + index * 25, 50, 20, Text.literal(""));
            constant.setText("0");
            constant.setTooltip(Tooltip.of(Text.of("Compared to the raw value")));
            constant.setChangedListener(c -> {
                try {
                    filter.constant = Integer.parseInt(c);
                } catch (Exception ignored) {}
            });
            DropdownWidget options = new DropdownWidget(textRenderer, 30, labelMenuHeight + 7 + index * 25, 90, Text.literal(""), "Select Sort Type", WynndowshoppingClient.possibleFilters.keySet().stream().toList(), (v) -> {
                filter.setOption(v);
                valueWidget.setChoices(WynndowshoppingClient.possibleFilters.get(v).stream().toList());
                valueWidget.setDefaultText("Select " + v);
                comparator.setMessage(Text.literal("Matches"));

            });
            ElevatedButtonWidget delete = new ElevatedButtonWidget(5, labelMenuHeight + 5 + index * 25, 20, 20, Text.literal("D"), Text.of("Delete"), b -> removeIndex = filterListOption.indexOf(options));

            filterListOption.add(options);
            filterListValue.add(valueWidget);
            filterListComparator.add(comparator);
            filterListConstant.add(constant);
            filterListDelete.add(delete);

            updateFilterListPositions();
        }).position(5, labelMenuHeight + 5 + filterListOption.size() * 25).size(80, 20).build();
    }

    public void clearFilters() {
        filterListOption.clear();
        filterListValue.clear();
        filterListComparator.clear();
        filterListConstant.clear();
        filterListDelete.clear();
        itemFilters.clear();

        updateFilterListPositions();
    }

    private void updateFilterListPositions() {
//        if (controller.itemFilterGuiPreviouslyOpened) {
            addFilterButton.setY(labelMenuHeight + 5 + filterListOption.size() * 25);

            filterListOption.forEach(i -> i.setY(labelMenuHeight + 8 + filterListOption.indexOf(i) * 25));
            filterListValue.forEach(i -> i.setY(labelMenuHeight + 8 + filterListValue.indexOf(i) * 25));
            filterListComparator.forEach(i -> i.setY(labelMenuHeight + 5 + filterListComparator.indexOf(i) * 25));
            filterListConstant.forEach(i -> i.setY(labelMenuHeight + 5 + filterListConstant.indexOf(i) * 25));
            filterListDelete.forEach(i -> i.setY(labelMenuHeight + 5 + filterListDelete.indexOf(i) * 25));
            //filterListDuplicate.forEach(i -> i.y = labelMenuHeight + 5 + filterListDuplicate.indexOf(i) * 25);
//        }
    }

    public ArrayList<Filter> getItemFilters() {
        return itemFilters;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        // draw filter buttons and stuff
        boolean anyOpen = false;
        for (DropdownWidget o : filterListOption) if (o.willClick(mouseX, mouseY)) anyOpen = true;
        if (anyOpen) {
            addFilterButton.render(context, 0, 0, delta); // funny band-aid fix for rendering white outline while in dropdown menu
        } else {
            addFilterButton.render(context, mouseX, mouseY, delta);
        }

        for (DropdownWidget o : filterListOption) {
            o.renderMain(context, mouseX, mouseY, delta);
        }
        for (DropdownWidget v : filterListValue) {
            if (!filterListOption.get(filterListValue.indexOf(v)).getLastChoice().isEmpty())
                v.renderMain(context, mouseX, mouseY, delta);
        }
        for (TextFieldWidget c : filterListConstant) {
            if ((filterListOption.get(filterListConstant.indexOf(c)).getLastChoice().equals("identification") || filterListOption.get(filterListConstant.indexOf(c)).getLastChoice().equals("base")) && !(itemFilters.get(filterListConstant.indexOf(c)).comparator.ordinal() <= Comparator.NOT_EXISTS.ordinal()))
                c.render(context, mouseX, mouseY, delta);
        }
        for (ButtonWidget c : filterListComparator) {
            if (!filterListOption.get(filterListComparator.indexOf(c)).getLastChoice().isEmpty())
                c.render(context, mouseX, mouseY, delta);
        }
        filterListDelete.forEach(i -> i.render(context, mouseX, mouseY, delta));
        //filterListDuplicate.forEach(i -> i.render(context, mouseX, mouseY, delta));

        for (DropdownWidget o : filterListOption) {
            o.renderDropdown(context, mouseX, mouseY, delta);
        }
        for (DropdownWidget v : filterListValue) {
            if (!filterListOption.get(filterListValue.indexOf(v)).getLastChoice().isEmpty())
                v.renderDropdown(context, mouseX, mouseY, delta);
        }

        // draw the label at the top
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 110);
        context.fill(0, 0, width, labelMenuHeight, 0xFF555555);
        context.drawHorizontalLine(0, width, labelMenuHeight, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Item Filters").setStyle(Style.EMPTY.withBold(true)), width / 2, (labelMenuHeight - textRenderer.fontHeight) / 2, 0xFFFFAA00);
        context.getMatrices().pop();

        // draw gui elements
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 110);
        backButton.render(context, mouseX, mouseY, delta);
        context.getMatrices().pop();

//        super.render(context, mouseX, mouseY, delta);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        backButton.mouseClicked(mouseX, mouseY, button);

        for (DropdownWidget o : filterListOption) {
            if (o.willClick(mouseX, mouseY)) {
                o.mouseClicked(mouseX, mouseY, button);
                return false;
            }
            o.mouseClicked(mouseX, mouseY, button);
        }
        for (DropdownWidget v : filterListValue) {
            if (v.willClick(mouseX, mouseY)) {
                if (!filterListOption.get(filterListValue.indexOf(v)).getLastChoice().isEmpty())
                    v.mouseClicked(mouseX, mouseY, button);
                return false;
            }
            if (!filterListOption.get(filterListValue.indexOf(v)).getLastChoice().isEmpty())
                v.mouseClicked(mouseX, mouseY, button);
        }
        for (ButtonWidget c : filterListComparator) {
            if (!filterListOption.get(filterListComparator.indexOf(c)).getLastChoice().isEmpty())
                c.mouseClicked(mouseX, mouseY, button);
        }
        for (TextFieldWidget c : filterListConstant) {
            if ((filterListOption.get(filterListConstant.indexOf(c)).getLastChoice().equals("identification") || filterListOption.get(filterListConstant.indexOf(c)).getLastChoice().equals("base")) && !(itemFilters.get(filterListConstant.indexOf(c)).comparator.ordinal() <= Comparator.NOT_EXISTS.ordinal())) {
                c.setFocused(c.isMouseOver(mouseX, mouseY));
//                c.mouseClicked(mouseX, mouseY, button);
            }
        }
        filterListDelete.forEach(i -> i.mouseClicked(mouseX, mouseY, button));
        //filterListDuplicate.forEach(i -> i.mouseClicked(mouseX, mouseY, button));

        addFilterButton.mouseClicked(mouseX, mouseY, button);

        if (removeIndex != -1) {
            filterListOption.remove(removeIndex);
            filterListValue.remove(removeIndex);
            filterListComparator.remove(removeIndex);
            filterListConstant.remove(removeIndex);
            filterListDelete.remove(removeIndex);
            //filterListDuplicate.remove(removeIndex);
            itemFilters.remove(removeIndex);
            removeIndex = -1;

            updateFilterListPositions();
        }

        return true;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        super.charTyped(chr, modifiers);

        filterListOption.forEach(i -> i.charTyped(chr, modifiers));
        filterListValue.forEach(i -> i.charTyped(chr, modifiers));
        filterListConstant.forEach(i -> i.charTyped(chr, modifiers));

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        filterListOption.forEach(i -> i.keyPressed(keyCode, scanCode, modifiers));
        filterListValue.forEach(i -> i.keyPressed(keyCode, scanCode, modifiers));
        filterListConstant.forEach(i -> i.keyPressed(keyCode, scanCode, modifiers));

        return true;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);


    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        filterListOption.forEach(i -> i.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
        filterListValue.forEach(i -> i.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));

        return true;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(previousScreen);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
