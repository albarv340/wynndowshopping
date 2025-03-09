package red.bread.wynndowshopping.client.gui;

import java.awt.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

public class ItemButtonWidget extends ButtonWidget {
    ItemStack item;

    public ItemButtonWidget(int x, int y, int slotSize, ItemStack item) {
        super(x, y, slotSize, slotSize, Text.empty(), ButtonWidget::onPress, DEFAULT_NARRATION_SUPPLIER);
        this.item = item;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
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
        context.fill(minX, minY, maxX, maxY, fillOpacity | Color.blue.getRGB());
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