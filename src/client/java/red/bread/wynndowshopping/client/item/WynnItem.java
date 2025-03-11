package red.bread.wynndowshopping.client.item;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import red.bread.wynndowshopping.client.util.Utils;

import java.util.*;

public class WynnItem {
    public String internalName;
    public String type;
    public Icon icon;
    public boolean allowCraftsman;

    // Type-dependent attributes
    public String armourMaterial;
    public String armourType;
    public String armourColor;
    public String attackSpeed;
    public Integer averageDps;
    public String weaponType;
    public String accessoryType;
    public Integer gatheringSpeed;
    public String tier;
    public String rarity;

    public ConsumableOnlyIDs consumableOnlyIDs;
    public ItemOnlyIDs itemOnlyIDs;
    public IngredientPositionModifiers ingredientPositionModifiers;
    public List<String> craftable;

    public Map<String, Identification> base;
    public Map<String, Identification> identifications;
    public Requirements requirements;
    public Map<String, String> majorIds;


    public Integer powderSlots;
    public String lore;

    public String restrictions;
    public Boolean raidReward;


    public DropMeta dropMeta;
    public List<DroppedBy> droppedBy;
    public String dropRestriction;

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

    public int getRarityValue() {
        if (rarity == null) {
            if (tier != null) {
                switch (tier) {
                    case "3" -> {
                        return 8;
                    }
                    case "2" -> {
                        return 9;
                    }
                    case "1" -> {
                        return 10;
                    }
                    case "0" -> {
                        return 11;
                    }
                }
            }
            return 100;
        }
        switch (rarity) {
            case "mythic" -> {
                return 0;
            }
            case "fabled" -> {
                return 1;
            }
            case "legendary" -> {
                return 2;
            }
            case "rare" -> {
                return 3;
            }
            case "unique" -> {
                return 4;
            }
            case "set" -> {
                return 5;
            }
            case "normal" -> {
                return 6;
            }
            default -> {
                return 7;
            }
        }
    }

    public int getItemTypeValue() {
        Map<String, Integer> itemValue = new HashMap<>();
        int counter = 0;
        itemValue.put("helmet", counter++);
        itemValue.put("chestplate", counter++);
        itemValue.put("leggings", counter++);
        itemValue.put("boots", counter++);
        itemValue.put("bow", counter++);
        itemValue.put("dagger", counter++);
        itemValue.put("relik", counter++);
        itemValue.put("spear", counter++);
        itemValue.put("wand", counter++);
        itemValue.put("ring", counter++);
        itemValue.put("bracelet", counter++);
        itemValue.put("necklace", counter++);
        itemValue.put("tome", counter++);
        itemValue.put("charm", counter++);
        itemValue.put("material", counter++);
        itemValue.put("ingredient", counter++);
        return  itemValue.getOrDefault(getItemTypeString(), 100);
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

    public String getMaterialProfessionLabel() {
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

    private boolean isEffectivenessIngredient() {
        if (!type.equals("ingredient")) {
            return false;
        }
        return ingredientPositionModifiers.above + ingredientPositionModifiers.left + ingredientPositionModifiers.notTouching + ingredientPositionModifiers.touching + ingredientPositionModifiers.right + ingredientPositionModifiers.under != 0;
    }

    private String getItemTypeString() {
        return switch (type) {
            case "armour" -> armourType;
            case "weapon" -> weaponType;
            case "accessory" -> accessoryType;
            default -> type;
        };
    }

    public List<Text> getLore() {
        List<Text> result = new ArrayList<>();
        if (this.type.equals("ingredient")) {
            result.add(Text.of("§8Crafting Ingredient"));
        }
        if (this.type.equals("material")) {
            result.add(Text.of("§7Crafting Material"));
        }
        if (this.type.equals("charm")) {
            result.add(Text.of("§8Active while on inventory"));
        }
        if (this.attackSpeed != null) {
            result.add(Text.of("§7" + Utils.snakeToUpperCamelCaseWithSpaces(this.attackSpeed) + " Attack Speed"));
        }
        if (base != null && !this.type.equals("charm")) {
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
            result.add(Text.of("   §8Average DPS: §7" + averageDps));
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
                result.add(Text.of("§c✖ §7Strength Min: " + requirements.strength));
            }
            if (requirements.dexterity > 0) {
                result.add(Text.of("§c✖ §7Dexterity Min: " + requirements.dexterity));
            }
            if (requirements.intelligence > 0) {
                result.add(Text.of("§c✖ §7Intelligence Min: " + requirements.intelligence));
            }
            if (requirements.defence > 0) {
                result.add(Text.of("§c✖ §7Defence Min: " + requirements.defence));
            }
            if (requirements.agility > 0) {
                result.add(Text.of("§c✖ §7Agility Min: " + requirements.agility));
            }
        }

        if (identifications != null) {
            result.add(Text.empty());
            List<String> rawSkillPoints = List.of("rawStrength", "rawDexterity", "rawIntelligence", "rawDefence", "rawAgility");
            Set<String> iteratedKeys = new HashSet<>();

            for (String key : rawSkillPoints) {
                if (identifications.containsKey(key)) {
                    result.add(Text.of(identifications.get(key).getFormattedStatString()));
                    iteratedKeys.add(key);
                }
            }

            for (Map.Entry<String, Identification> identification : identifications.entrySet()) {
                if (iteratedKeys.contains(identification.getKey())) {
                    continue;
                }
                result.add(Text.of(identification.getValue().getFormattedStatString()));
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

        if (type.equals("charm") && base != null) {
            result.add(Text.empty());
            if (base.containsKey("leveledLootBonus")) {
                Identification stat = base.get("leveledLootBonus");
                result.add(Text.of("§a+" + stat.min + " §2to §a" + stat.max + "% §7Loot from Lv. " + requirements.levelRange.min + "-" + requirements.levelRange.max + " content"));
            }
            if (base.containsKey("leveledXpBonus")) {
                Identification stat = base.get("leveledXpBonus");
                result.add(Text.of("§a+" + stat.min + " §2to §a" + stat.max + "% §7XP from Lv. " + requirements.levelRange.min + "-" + requirements.levelRange.max + " content"));
            }
            if (base.containsKey("damageFromMobs")) {
                Identification stat = base.get("damageFromMobs");
                result.add(Text.of("§c+" + stat.min + " §4to §c" + stat.max + "% §7Damage taken from mobs"));
            }
        }

        if (rarity != null) {
            result.add(Text.empty());
            if (powderSlots != null) {
                result.add(Text.of("§7[0/" + powderSlots + "] Powder Slots"));
            }
            result.add(Text.of(getNameFormatting() + Utils.toUpperCamelCaseWithSpaces(rarity) + " " + Utils.toUpperCamelCaseWithSpaces(getItemTypeString())));
        }
        if (type.equals("ingredient")) {
            if (isEffectivenessIngredient()) {
                result.add(Text.empty());
                if (ingredientPositionModifiers.left != 0) {
                    result.add(Text.of((ingredientPositionModifiers.left < 0 ? "§c" : "§a+") + ingredientPositionModifiers.left + "% §7Ingredient Effectiveness"));
                    result.add(Text.of("§7(To ingredients to the left of this one)"));
                }
                if (ingredientPositionModifiers.right != 0) {
                    result.add(Text.of((ingredientPositionModifiers.right < 0 ? "§c" : "§a+") + ingredientPositionModifiers.right + "% §7Ingredient Effectiveness"));
                    result.add(Text.of("§7(To ingredients to the right of this one)"));
                }
                if (ingredientPositionModifiers.above != 0) {
                    result.add(Text.of((ingredientPositionModifiers.above < 0 ? "§c" : "§a+") + ingredientPositionModifiers.above + "% §7Ingredient Effectiveness"));
                    result.add(Text.of("§7(To ingredients above this one)"));
                }
                if (ingredientPositionModifiers.under != 0) {
                    result.add(Text.of((ingredientPositionModifiers.under < 0 ? "§c" : "§a+") + ingredientPositionModifiers.under + "% §7Ingredient Effectiveness"));
                    result.add(Text.of("§7(To ingredients below this one)"));
                }
                if (ingredientPositionModifiers.touching != 0) {
                    result.add(Text.of((ingredientPositionModifiers.touching < 0 ? "§c" : "§a+") + ingredientPositionModifiers.touching + "% §7Ingredient Effectiveness"));
                    result.add(Text.of("§7(To ingredients touching this one)"));
                }
                if (ingredientPositionModifiers.notTouching != 0) {
                    result.add(Text.of((ingredientPositionModifiers.notTouching < 0 ? "§c" : "§a+") + ingredientPositionModifiers.notTouching + "% §7Ingredient Effectiveness"));
                    result.add(Text.of("§7(To ingredients not touching this one)"));
                }

            }
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
        if (restrictions != null) {
            result.add(Text.of("§c" + Utils.toUpperCamelCaseWithSpaces(restrictions.replace(" item", "Item"))));
        }
        return result;
    }
}

