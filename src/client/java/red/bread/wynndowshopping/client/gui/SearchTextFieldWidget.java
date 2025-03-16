package red.bread.wynndowshopping.client.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import red.bread.wynndowshopping.client.util.Utils;

import java.awt.*;
import java.text.DecimalFormat;

public class SearchTextFieldWidget extends TextFieldWidget {
    long lastClick = 0;
    public SearchTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, null, text);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        InventoryOverlay.isInteractedWith = true;
        if (System.currentTimeMillis() - lastClick < 200) {
            InventoryOverlay.highlightSearchedString = !InventoryOverlay.highlightSearchedString;
        }
        lastClick = System.currentTimeMillis();
    }

    private String evaluateExpression() {
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(Utils.eval(getText()));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        String evaluatedExpression = evaluateExpression();
        if (evaluatedExpression != null) {
            context.drawText(textRenderer, Text.of("=" + evaluatedExpression), getCharacterX(getText().length() - 1) + 10, getY() + 6, 0x00ff00, false);
        }
        if (InventoryOverlay.highlightSearchedString) {
            context.fill(getX(), getY(), getX() + width, getY() + height, new Color(255, 255, 0, 50).getRGB());
        }
    }
}
