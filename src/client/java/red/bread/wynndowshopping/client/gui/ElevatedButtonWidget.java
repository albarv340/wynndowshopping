package red.bread.wynndowshopping.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ElevatedButtonWidget extends ButtonWidget {
    public ElevatedButtonWidget(int x, int y, int width, int height, Text text, PressAction onPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(0.0, 0.0, 360F);
        super.renderWidget(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
    }
}
