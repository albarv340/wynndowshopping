package red.bread.wynndowshopping.client.util;

public class Filter {
    private String option;
    public String value;
    public Comparator comparator;
    public int constant;

    // comparator info:
    // 0 -> Exists
    // 1 -> Not Exists
    // 2 -> >=
    // 3 -> >
    // 4 -> =
    // 5 -> <=
    // 6 -> <

    public Filter() {
        this.option = "";
        this.value = "";
        this.comparator = Comparator.EXISTS;
        this.constant = 0;
    }

    public void setOption(String option) {
        this.option = option;
        if (option.equals("Charm Power") && comparator.ordinal() < Comparator.GTE.ordinal()) comparator = Comparator.NOT_EXISTS;
        else comparator = Comparator.EXISTS;
    }

    public String getOption() {
        return option;
    }

    public void incrementComparator() {
        comparator = comparator.ordinal() < Comparator.values().length - 1 ? Comparator.values()[comparator.ordinal() + 1] : Comparator.EXISTS;
        if (option.equals("identification") || option.equals("base")) {
            if (comparator.ordinal() > Comparator.LT.ordinal()) comparator = Comparator.EXISTS;
        } else {
            if (comparator.ordinal() > Comparator.NOT_EXISTS.ordinal()) comparator = Comparator.EXISTS;
        }
    }
}
