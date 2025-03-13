package red.bread.wynndowshopping.client.item;

import com.google.gson.*;
import red.bread.wynndowshopping.client.WynndowshoppingClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class WynnItemDeserializer implements JsonDeserializer<Map<String, WynnItem>> {
    @Override
    public Map<String, WynnItem> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Map<String, WynnItem> items = new HashMap<>();
        WynndowshoppingClient.possibleFilters.put("type", new HashSet<>());
        WynndowshoppingClient.possibleFilters.put("rarity", new HashSet<>());
        WynndowshoppingClient.possibleFilters.put("identification", new HashSet<>());
        WynndowshoppingClient.possibleFilters.put("base", new HashSet<>());
        WynndowshoppingClient.possibleFilters.put("restriction", new HashSet<>());
        WynndowshoppingClient.possibleFilters.put("majorId", new HashSet<>());


        JsonObject obj = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            WynnItem wynnItem = context.deserialize(value, WynnItem.class);
            items.put(key, wynnItem);
            if (wynnItem.identifications != null) {
                for (String identification : wynnItem.identifications.keySet()) {
                    WynndowshoppingClient.possibleFilters.get("identification").add(identification);
                }
            }
            if (wynnItem.base != null) {
                for (String identification : wynnItem.base.keySet()) {
                    WynndowshoppingClient.possibleFilters.get("base").add(identification);
                }
            }
            if (wynnItem.majorIds != null) {
                for (String identification : wynnItem.majorIds.keySet()) {
                    WynndowshoppingClient.possibleFilters.get("majorId").add(identification);
                }
            }
            if (wynnItem.rarity != null) {
                WynndowshoppingClient.possibleFilters.get("rarity").add(wynnItem.rarity);
            }
            if (wynnItem.type != null) {
                WynndowshoppingClient.possibleFilters.get("type").add(wynnItem.type);
            }
            if (wynnItem.armourType != null) {
                WynndowshoppingClient.possibleFilters.get("type").add(wynnItem.armourType);
            }
            if (wynnItem.weaponType != null) {
                WynndowshoppingClient.possibleFilters.get("type").add(wynnItem.weaponType);
            }
            if (wynnItem.accessoryType != null) {
                WynndowshoppingClient.possibleFilters.get("type").add(wynnItem.accessoryType);
            }
            if (wynnItem.restrictions != null) {
                WynndowshoppingClient.possibleFilters.get("restriction").add(wynnItem.restrictions);
            }
        }

        return items;
    }
}

