package red.bread.wynndowshopping.client.util;

import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import red.bread.wynndowshopping.client.item.WynnItem;

import java.util.List;
import java.util.Map;

public class ItemStackBuilder {
    public static ItemStack buildItem(String name, WynnItem wynnItem) {
        ComponentChanges.Builder builder = ComponentChanges.builder();
        builder.add(DataComponentTypes.CUSTOM_NAME, wynnItem.getFormattedDisplayName(name));
        builder.add(DataComponentTypes.LORE, new LoreComponent(wynnItem.getLore()));
        builder.add(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        try {
            ItemStack result = getBaseItem(wynnItem);
            result.applyChanges(builder.build());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            ItemStack placeholderItemStack = new ItemStack(RegistryEntry.of(Item.byRawId(2)));
            placeholderItemStack.applyChanges(builder.build());
            return placeholderItemStack;
        }
    }

    private static ItemStack getBaseItem(WynnItem wynnItem) {
        ItemStack result = new ItemStack(RegistryEntry.of(Item.byRawId(1)));
        if (wynnItem.icon != null) {
            switch (wynnItem.icon.format) {
                case "attribute" -> {
                    Map<String, String> value = wynnItem.icon.getMap();
                    result = new ItemStack(Registries.ITEM.get(Identifier.of(value.get("id"))));
                    result.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(List.of(Float.parseFloat(value.get("customModelData"))), List.of(), List.of(), List.of()));
                }
                case "legacy" -> {
                    result = new ItemStack(Registries.ITEM.get(Identifier.of("minecraft:" + Utils.legacyNameMap.getOrDefault(wynnItem.icon.getString(), "stone"))));
                }
                case "skin" -> {
                    result = new ItemStack(Registries.ITEM.get(Identifier.of("minecraft:player_head")));
                    SkinUtils.setPlayerHeadFromUUID(result, wynnItem.icon.getString());
                }
            }
        } else if (wynnItem.type.equals("armour")) {
            String itemId = (wynnItem.armourMaterial.equals("chain") ? "chainmail" : wynnItem.armourMaterial) + "_" + wynnItem.armourType;
            result = new ItemStack(Registries.ITEM.get(Identifier.of("minecraft:" + itemId)));
            if (wynnItem.armourColor != null) {
                result.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(Utils.parseRGB(wynnItem.armourColor).getRGB(), false));
            }
        }
        if (result.toString().contains("minecraft:air")) {
            result = new ItemStack(RegistryEntry.of(Item.byRawId(1)));
        }
        return result;
    }
}
