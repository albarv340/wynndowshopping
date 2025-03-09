package red.bread.wynndowshopping.client.item;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class WynnItem {
    public String internalName;
    public String type;
    public String subType;
    public Icon icon;
    public boolean identifier;
    public boolean allow_craftsman;

    // Type-dependent attributes
    public String armourMaterial;  // For armors
    public String armourType;  // For armors
    public String armourColor;  // For armors
    public String attackSpeed;     // For weapons
    public Integer averageDPS;     // For weapons
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
    public Base base;
    public Requirements requirements;
    public Map<String, Identification> identifications;
}

