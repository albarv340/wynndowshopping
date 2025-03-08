package red.bread.wynndowshopping.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.awt.*;

public class SearchTextFieldWidget extends TextFieldWidget {
    long lastClick = 0;
    public boolean isInteractedWith = false;
    public SearchTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, null, text);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        isInteractedWith = true;
        if (System.currentTimeMillis() - lastClick < 200) {
            WynndowshoppingClient.highlightSearchedString = !WynndowshoppingClient.highlightSearchedString;
        }
        lastClick = System.currentTimeMillis();
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        if (WynndowshoppingClient.highlightSearchedString) {
            context.fill(getX(), getY(), getX() + width, getY() + height, new Color(255, 255, 0, 50).getRGB());
        }
    }
}
