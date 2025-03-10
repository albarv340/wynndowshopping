package red.bread.wynndowshopping.client.item;

import net.minecraft.text.Text;
import red.bread.wynndowshopping.client.util.Utils;

public class Identification {
    public int min, max, raw;
    public String name;

    public boolean isRaw() {
        return min == max && max == raw;
    }

    public Identification(int raw) {
        this.raw = raw;
        this.min = raw;
        this.max = raw;
    }

    public String getFormattedStatString() {
        String statName = name;
        String statSuffix = "";
        boolean isNegative = raw < 0;
        String statPrefix = isNegative ? "§c" : "§a+";
        if (name.contains("Cost")) {
            isNegative = !isNegative;
            statPrefix = isNegative ? "§c" : "§a";
        }
        String statRangePrefix = isNegative ? "§4" : "§2";
        if (statName.startsWith("raw")) {
            if (statName.equals("rawAttackSpeed")) {
                statSuffix = " tier";
            }
            statName = statName.replace("raw", "");
        } else if (statName.endsWith("Raw")) {
            statName = statName.replace("Raw", "");
        } else if (statName.endsWith("poison")) {
            statSuffix = "/3s";
        } else {
            if (statName.equals("manaRegen")) {
                statSuffix = "/5s";
            } else if (statName.equals("manaSteal") || statName.equals("lifeSteal")) {
                statSuffix = "/3s";
            } else {
                statSuffix = "%";
            }
        }
        if (isRaw()) {
            return statPrefix + raw + statSuffix + " §7" + Utils.toUpperCamelCaseWithSpaces(statName);
        }
        return statPrefix + min + statRangePrefix + " to " + statPrefix + max + statSuffix + " §7" + Utils.toUpperCamelCaseWithSpaces(statName);

    }
}
