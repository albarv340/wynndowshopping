package red.bread.wynndowshopping.client.item;

import com.google.gson.annotations.SerializedName;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import red.bread.wynndowshopping.client.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WynnItem {
    public String internalName;
    public String type;
    public String subType;
    public Icon icon;
    public boolean identifier;
    public boolean allowCraftsman;

    // Type-dependent attributes
    public String armourMaterial;  // For armors
    public String armourType;  // For armors
    public String armourColor;  // For armors
    public String attackSpeed;     // For weapons
    public Integer averageDps;     // For weapons
    public Integer gatheringSpeed; // For tools
    public String tier;            // For ingredients and materials
    public String rarity;          // For normal items

    public ConsumableOnlyIDs consumableOnlyIDs; // For ingredients
    public IngredientPositionModifiers ingredientPositionModifiers; // For ingredients
    public ItemOnlyIDs itemOnlyIDs; // For ingredients
    public Map<String, String> majorIds; // Key-value pair of major IDs and descriptions

    @SerializedName("craftable")
    public List<String> craftable; // Can be a string or a list

    public Integer powderSlots;
    public String lore;
    public String dropRestriction;
    public String restriction;
    public Boolean raidReward;

    public DropMeta dropMeta;
    public Map<String, Identification> base;
    public Requirements requirements;
    public Map<String, Identification> identifications;

    private Formatting getNameFormatting() {
        final Map<String, Formatting> gearTierMap = Map.of(
                "normal", Formatting.WHITE,
                "unique", Formatting.YELLOW,
                "rare", Formatting.LIGHT_PURPLE,
                "set", Formatting.GREEN,
                "legendary", Formatting.AQUA,
                "fabled", Formatting.RED,
                "mythic", Formatting.DARK_PURPLE,
                "crafted", Formatting.DARK_AQUA
        );
        if (this.type.equals("ingredient")) {
            return Formatting.GRAY;
        }
        return this.rarity != null ? gearTierMap.getOrDefault(this.rarity, Formatting.WHITE) : Formatting.WHITE;
    }

    public int getBackgroundColor() {
        if (!this.type.equals("ingredient") && !this.type.equals("material")) {
            return getNameFormatting().getColorValue();
        }
        switch (tier) {
            case "1" -> {
                return Formatting.WHITE.getColorValue();
            }
            case "2" -> {
                return Formatting.YELLOW.getColorValue();
            }
            case "3" -> {
                return Formatting.GOLD.getColorValue();
            }
            default -> {
                return Formatting.GRAY.getColorValue();
            }
        }
    }

    private Text getTierText() {
        final Map<String, String> ingredientTierMap = Map.of(
                "0", " §7[§8✫✫✫§7]",
                "1", " §6[§e✫§8✫✫§6]",
                "2", " §5[§d✫✫§8✫§5]",
                "3", " §3[§b✫✫✫§3]"
        );
        final Map<String, String> materialTierMap = Map.of(
                "1", " §6[§e✫§8✫✫§6]",
                "2", " §6[§e✫✫§8✫§6]",
                "3", " §6[§e✫✫✫§6]"
        );
        return Text.of(tier != null ? type.equals("material") ? materialTierMap.get(tier) : ingredientTierMap.get(tier) : "");
    }

    public Text getFormattedDisplayName(String name) {
        if (this.tier != null) {
            name = name.replace(tier, "").trim();
        }
        MutableText nameText = Text.literal(name);
        nameText.setStyle(nameText.getStyle().withColor(this.getNameFormatting()).withItalic(false));

        nameText.append(getTierText());
        return nameText;
    }

    private String getMaterialProfessionLabel() {
        if (!type.equals("material")) {
            return "";
        }
        if (internalName.contains("Wood") || internalName.contains("Paper")) {
            return "§f" + Utils.getProfessionIcon("woodcutting") + "§7 Woodcutting";
        }
        if (internalName.contains("Grain") || internalName.contains("String")) {
            return "§f" + Utils.getProfessionIcon("farming") + "§7 Farming";
        }
        if (internalName.contains("Oil") || internalName.contains("Meat")) {
            return "§f" + Utils.getProfessionIcon("fishing") + "§7 Fishing";
        }
        if (internalName.contains("Gem") || internalName.contains("Ingot")) {
            return "§f" + Utils.getProfessionIcon("mining") + "§7 Mining";
        }
        return "";
    }

    public List<Text> getLore() {
        List<Text> result = new ArrayList<>();
        if (this.type.equals("ingredient")) {
            result.add(Text.of("§8Crafting Ingredient"));
        }
        if (this.type.equals("material")) {
            result.add(Text.of("§7Crafting Material"));
        }
        if (this.attackSpeed != null) {
            result.add(Text.of("§7" + Utils.snakeToUpperCamelCaseWithSpaces(this.attackSpeed) + " Attack Speed"));
        }
        if (base != null) {
            result.add(Text.empty());
            List<String> baseStatOrder = List.of("baseHealth", "baseEarthDefence", "baseThunderDefence", "baseWaterDefence", "baseFireDefence", "baseAirDefence", "baseDamage", "baseEarthDamage", "baseThunderDamage", "baseWaterDamage", "baseFireDamage", "baseAirDamage");
            for (String baseStatName : baseStatOrder) {
                if (!base.containsKey(baseStatName)) {
                    continue;
                }
                Identification value = base.get(baseStatName);
                String statName = baseStatName.replace("base", "");
                String statSuffix = "";
                String statPrefix = "";
                if (statName.equals("Health")) {
                    statPrefix = "§4❤ ";
                }
                if (statName.equals("Damage")) {
                    statPrefix = "§6✣ ";
                    statName = "Neutral" + statName;
                }
                if (statName.startsWith("Earth")) {
                    statPrefix = "§2✤ ";
                    statName = statName.replace("Earth", "Earth§7");
                }
                if (statName.startsWith("Thunder")) {
                    statPrefix = "§e✦ ";
                    statName = statName.replace("Thunder", "Thunder§7");
                }
                if (statName.startsWith("Water")) {
                    statPrefix = "§b❉ ";
                    statName = statName.replace("Water", "Water§7");
                }
                if (statName.startsWith("Fire")) {
                    statPrefix = "§c✹ ";
                    statName = statName.replace("Fire", "Fire§7");
                }
                if (statName.startsWith("Air")) {
                    statPrefix = "§f❋ ";
                    statName = statName.replace("Air", "Air§7");
                }
                String statValue = value.isRaw() ? String.valueOf(value.raw) : value.min + "-" + value.max;
                result.add(Text.of(statPrefix + Utils.toUpperCamelCaseWithSpaces(statName) + ": " + statValue + statSuffix));
            }
        }
        if (averageDps != null) {
            result.add(Text.of("§8Average DPS: §7" + averageDps));
        }
        if (!type.equals("material") && !type.equals("ingredient")) {
            result.add(Text.empty());
            if (requirements.classRequirement != null) {
                result.add(Text.of("§c✖ §7Class Req: " + Utils.toUpperCamelCaseWithSpaces(requirements.classRequirement)));
            }
            if (requirements.level > 0) {
                result.add(Text.of("§c✖ §7Combat Lv. Min: " + requirements.level));
            }
            if (requirements.strength > 0) {
                result.add(Text.of("§c✖ §7Strength Min: " + requirements.level));
            }
            if (requirements.dexterity > 0) {
                result.add(Text.of("§c✖ §7Dexterity Min: " + requirements.level));
            }
            if (requirements.intelligence > 0) {
                result.add(Text.of("§c✖ §7Intelligence Min: " + requirements.level));
            }
            if (requirements.defence > 0) {
                result.add(Text.of("§c✖ §7Defense Min: " + requirements.level));
            }
            if (requirements.agility > 0) {
                result.add(Text.of("§c✖ §7Agility Min: " + requirements.level));
            }
        }

        if (identifications != null) {
            result.add(Text.empty());
            for (Map.Entry<String, Identification> identification : identifications.entrySet()) {
                String statName = identification.getKey();
                String statSuffix = "";
                boolean isNegative = identification.getValue().raw < 0;
                String statPrefix = isNegative ? "§c" : "§a";
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
                result.add(Text.of(statPrefix + (!isNegative ? "+" : "") + identification.getValue().min + statRangePrefix + " to " + statPrefix + identification.getValue().max + statSuffix + " §7" + Utils.toUpperCamelCaseWithSpaces(statName)));
            }
        }
        if (majorIds != null) {
            for (Map.Entry<String, String> mid : majorIds.entrySet()) {
                String majorIdDesc = "§b" + mid.getValue().replaceAll("<[^>]*>", "").replace(":", ":§3");
                List<String> lengthLimitedStrings = Utils.splitStringByLength(majorIdDesc, 40);
                for (String line : lengthLimitedStrings) {
                    result.add(Text.of("§3" + line));
                }
            }
        }

        if (rarity != null) {
            result.add(Text.empty());
            if (powderSlots != null) {
                result.add(Text.of("§7[0/" + powderSlots + "] Powder Slots"));
            }
            result.add(Text.of(getNameFormatting() + Utils.toUpperCamelCaseWithSpaces(rarity) + " Item"));
        }
        if (type.equals("ingredient")) {
            result.add(Text.empty());
            if (consumableOnlyIDs != null && itemOnlyIDs != null) {
                if (consumableOnlyIDs.duration != null && consumableOnlyIDs.duration != 0 && itemOnlyIDs.durabilityModifier != null && itemOnlyIDs.durabilityModifier != 0) {
                    String durationPrefix = consumableOnlyIDs.duration < 0 ? "§c" : "§a+";
                    String durabilityPrefix = itemOnlyIDs.durabilityModifier < 0 ? "§c" : "§a+";
                    result.add(Text.of(durabilityPrefix + (itemOnlyIDs.durabilityModifier / 1000) + " Durability §7or " + durationPrefix + consumableOnlyIDs.duration + "s Duration"));
                } else if (consumableOnlyIDs.duration != null && consumableOnlyIDs.duration != 0) {
                    String durationPrefix = consumableOnlyIDs.duration < 0 ? "§c" : "§a+";
                    result.add(Text.of(durationPrefix + consumableOnlyIDs.duration + "s Duration"));
                } else if (itemOnlyIDs.durabilityModifier != null && itemOnlyIDs.durabilityModifier != 0) {
                    String durabilityPrefix = itemOnlyIDs.durabilityModifier < 0 ? "§c" : "§a+";
                    result.add(Text.of(durabilityPrefix + (itemOnlyIDs.durabilityModifier / 1000) + " Durability"));
                }
                if (consumableOnlyIDs.charges != null && consumableOnlyIDs.charges != 0) {
                    String chargesPrefix = consumableOnlyIDs.charges < 0 ? "§c" : "§a+";
                    result.add(Text.of(chargesPrefix + consumableOnlyIDs.charges + " Charge" + (consumableOnlyIDs.charges > 1 ? "s" : "")));
                }
                if (itemOnlyIDs.strengthRequirement != null && itemOnlyIDs.strengthRequirement != 0) {
                    String prefix = itemOnlyIDs.strengthRequirement < 0 ? "§a" : "§c+";
                    result.add(Text.of(prefix + itemOnlyIDs.strengthRequirement + " Strength Min."));
                }
                if (itemOnlyIDs.dexterityRequirement != null && itemOnlyIDs.dexterityRequirement != 0) {
                    String prefix = itemOnlyIDs.dexterityRequirement < 0 ? "§a" : "§c+";
                    result.add(Text.of(prefix + itemOnlyIDs.dexterityRequirement + " Dexterity Min."));
                }
                if (itemOnlyIDs.intelligenceRequirement != null && itemOnlyIDs.intelligenceRequirement != 0) {
                    String prefix = itemOnlyIDs.intelligenceRequirement < 0 ? "§a" : "§c+";
                    result.add(Text.of(prefix + itemOnlyIDs.intelligenceRequirement + " Intelligence Min."));
                }
                if (itemOnlyIDs.defenceRequirement != null && itemOnlyIDs.defenceRequirement != 0) {
                    String prefix = itemOnlyIDs.defenceRequirement < 0 ? "§a" : "§c+";
                    result.add(Text.of(prefix + itemOnlyIDs.defenceRequirement + " Defence Min."));
                }
                if (itemOnlyIDs.agilityRequirement != null && itemOnlyIDs.agilityRequirement != 0) {
                    String prefix = itemOnlyIDs.agilityRequirement < 0 ? "§a" : "§c+";
                    result.add(Text.of(prefix + itemOnlyIDs.agilityRequirement + " Agility Min."));
                }
            }
            if (requirements.level > 0) {
                result.add(Text.empty());
                result.add(Text.of("§c✖ §7Crafting Lv. Min: " + requirements.level));
            }
            if (!requirements.skills.isEmpty()) {
                for (String skill : requirements.skills) {
                    result.add(Text.of("   §8✖ §f" + Utils.getProfessionIcon(skill) + " §7" + Utils.toUpperCamelCaseWithSpaces(skill)));
                }
            }
        }
        if (type.equals("material")) {
            if (requirements.level > 0) {
                result.add(Text.empty());
                result.add(Text.of("§c✖ §7" + getMaterialProfessionLabel() + " Lv. Min: " + requirements.level));
            }
        }
        return result;
    }
}

