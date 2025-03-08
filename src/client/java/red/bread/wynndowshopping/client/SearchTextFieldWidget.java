package red.bread.wynndowshopping.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class SearchTextFieldWidget extends TextFieldWidget {
    long lastClick = 0;
    public SearchTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, null, text);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        if (System.currentTimeMillis() - lastClick < 200) {
            WynndowshoppingClient.highlightSearchedString = !WynndowshoppingClient.highlightSearchedString;
        }
        lastClick = System.currentTimeMillis();
    }
}
