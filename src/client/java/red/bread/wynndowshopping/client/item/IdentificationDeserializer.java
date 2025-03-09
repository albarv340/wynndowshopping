package red.bread.wynndowshopping.client.item;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class IdentificationDeserializer implements JsonDeserializer<Map<String, Identification>> {
    @Override
    public Map<String, Identification> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Map<String, Identification> identifications = new HashMap<>();

        JsonObject obj = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (value.isJsonObject()) {
                identifications.put(key, context.deserialize(value, Identification.class));
            } else if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
                identifications.put(key, new Identification(value.getAsInt()));
            }
        }

        return identifications;
    }
}

