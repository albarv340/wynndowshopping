package red.bread.wynndowshopping.client.item;

public class Identification {
    public int min, max, raw;

    public boolean isRaw() {
        return min == max && max == raw;
    }

    public Identification(int raw) {
        this.raw = raw;
        this.min = raw;
        this.max = raw;
    }
}
