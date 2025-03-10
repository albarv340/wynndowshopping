package red.bread.wynndowshopping.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import red.bread.wynndowshopping.client.item.WynnItem;
import red.bread.wynndowshopping.client.util.Utils;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ItemButtonWidget extends ButtonWidget {
    ItemStack item;
    WynnItem wynnItem;

    public ItemButtonWidget(int x, int y, int slotSize, ItemStack item, WynnItem wynnItem) {
        super(x, y, slotSize, slotSize, Text.empty(), ButtonWidget::onPress, DEFAULT_NARRATION_SUPPLIER);
        this.item = item;
        this.wynnItem = wynnItem;
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible && this.isMouseOver(mouseX, mouseY) && button == 1) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            try {
                String formattedName = Utils.spaceToUpperSnakeCase(item.getName().getString().replaceAll("§.", "").replaceAll("\\[[^]]+]", "").trim());
                String encodedName = URLEncoder.encode(formattedName, StandardCharsets.UTF_8);
                ConfirmLinkScreen.open(MinecraftClient.getInstance().currentScreen, new URI("https://wynncraft.wiki.gg/wiki/" + encodedName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        int minX = getX();
        int minY = getY();
        int maxX = minX + width;
        int maxY = minY + height;

        boolean hovered = (mouseX >= minX) && (mouseX <= maxX) && (mouseY >= minY) && (mouseY <= maxY);

        int outlineColor = hovered ? 0xFFC6C6C6 : 0xFFFFFFFF;
        int fillOpacity = hovered ? 0x6B000000 : 0x88000000;
        context.getMatrices().push();
        context.getMatrices().translate(0.0, 0.0, 160F);
        context.fill(minX, minY, maxX, maxY, fillOpacity | wynnItem.getBackgroundColor());
        context.drawHorizontalLine(minX, maxX, minY, outlineColor);
        context.drawHorizontalLine(minX, maxX, maxY, outlineColor);
        context.drawVerticalLine(minX, minY, maxY, outlineColor);
        context.drawVerticalLine(maxX, minY, maxY, outlineColor);

        context.drawItem(item, minX + 2, minY + 2);
        context.getMatrices().pop();
        if (isHovered() && MinecraftClient.getInstance().currentScreen != null) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, item.getTooltip(Item.TooltipContext.DEFAULT, MinecraftClient.getInstance().player, TooltipType.ADVANCED), mouseX, mouseY);
        }
    }

    @Override
    public void onPress() {
    }
}