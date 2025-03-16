package red.bread.wynndowshopping.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ElevatedButtonWidget extends ButtonWidget {
    private float elevation = 360f;
    private PressAction onRightClickPress = null;
    public ElevatedButtonWidget(int x, int y, int width, int height, Text text, PressAction onPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
    }
    public ElevatedButtonWidget(int x, int y, int width, int height, Text text, Text tooltip, PressAction onPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.setTooltip(Tooltip.of(tooltip));
    }
    public ElevatedButtonWidget(int x, int y, int width, int height, Text text, Text tooltip, PressAction onPress, PressAction onRightClickPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.setTooltip(Tooltip.of(tooltip));
        this.onRightClickPress = onRightClickPress;
    }
    public ElevatedButtonWidget(int x, int y, int width, int height, Text text, Text tooltip, PressAction onPress, float elevation) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.elevation = elevation;
        this.setTooltip(Tooltip.of(tooltip));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.visible && onRightClickPress != null && button == 1) {
            playClickSound(MinecraftClient.getInstance().getSoundManager());
            onRightClickPress.onPress(this);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(0.0, 0.0, elevation);
        super.renderWidget(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
    }
}
