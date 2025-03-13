package red.bread.wynndowshopping.client.util;

public class Filter {
    private String option;
    public String value;
    public Comparator comparator;
    public double constant;

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
        this.constant = 0.0;
    }

    public Filter(String option, String value, Comparator comparator, double constant) {
        this.option = option;
        this.value = value;
        this.comparator = comparator;
        this.constant = constant;
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
        comparator = Comparator.values()[comparator.ordinal() + 1];
        if (option.equals("Stat")) {
            if (comparator.ordinal() > Comparator.LT.ordinal()) comparator = Comparator.EXISTS;
        } else if (option.equals("Charm Power")) {
            if (comparator.ordinal() > Comparator.LT.ordinal()) comparator = Comparator.EXISTS;
            if (comparator.ordinal() < Comparator.GTE.ordinal()) comparator = Comparator.GTE;
        } else {
            if (comparator.ordinal() > Comparator.NOT_EXISTS.ordinal()) comparator = Comparator.EXISTS;
        }
    }
}
