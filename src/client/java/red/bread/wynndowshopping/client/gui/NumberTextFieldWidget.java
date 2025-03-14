package red.bread.wynndowshopping.client.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class NumberTextFieldWidget extends TextFieldWidget {
    Consumer<Integer> consumer;
    public NumberTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, int text, Consumer<Integer> consumer) {
        super(textRenderer, x, y, width, height, Text.of(String.valueOf(text)));
        setText(String.valueOf(text));
        this.consumer = consumer;

        setChangedListener(s -> {
            try {
                consumer.accept(Integer.parseInt(s));
            } catch (Exception e) {
                consumer.accept(0);
            }
        });
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(0f, 0f, 200f);
        super.renderWidget(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
    }

    @Override
    public void write(String text) {
        if (text.isEmpty() || Pattern.matches("\\d+", text)) {
            super.write(text);
        }
    }
}
