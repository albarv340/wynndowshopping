package red.bread.wynndowshopping.client.item;

import java.util.HashMap;
import java.util.Map;

public class Icon {
    public Object value; // Can be a Map<String, String> or a String
    public String format;

    public Map<String, String> getMap() {
        if (value instanceof Map<?, ?> rawMap) {
            Map<String, String> safeMap = new HashMap<>();

            for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                    safeMap.put((String) entry.getKey(), (String) entry.getValue());
                } else {
                    return null;
                }
            }
            return safeMap;
        }

        return null;
    }
    public String getString() {
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }
}
