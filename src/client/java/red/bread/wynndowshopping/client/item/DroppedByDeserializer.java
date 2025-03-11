package red.bread.wynndowshopping.client.item;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DroppedByDeserializer implements JsonDeserializer<DroppedBy> {

    @Override
    public DroppedBy deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        DroppedBy droppedBy = new DroppedBy();
        droppedBy.name = jsonObject.get("name").getAsString();

        JsonElement coordsElement = jsonObject.get("coords");

        List<List<Integer>> coords = new ArrayList<>();
        List<Integer> singleCoord = new ArrayList<>();
        if (coordsElement.isJsonArray()) {
            JsonArray coordsArray = coordsElement.getAsJsonArray();
            for (JsonElement element : coordsArray) {
                if (element.isJsonArray()) {
                    List<Integer> coord = new ArrayList<>();
                    JsonArray coordArray = element.getAsJsonArray();
                    for (JsonElement subElement : coordArray) {
                        coord.add(subElement.getAsInt());
                    }
                    coords.add(coord);
                } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                    singleCoord.add(element.getAsInt());
                }
            }
        }
        if (!singleCoord.isEmpty()) {
            coords.add(singleCoord);
        }

        droppedBy.coords = coords;
        return droppedBy;
    }
}
