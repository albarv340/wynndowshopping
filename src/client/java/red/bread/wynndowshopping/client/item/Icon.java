package red.bread.wynndowshopping.client.item;

import java.util.Map;

public class Icon {
    public Object value; // Can be a Map<String, String> or a String
    public String format;

//    public String getId() {
//        if (value instanceof Map) {
//            Map<String, String> mapValue = (Map<String, String>) value;
//            return mapValue.getOrDefault("id", null);
//        } else if (value instanceof String) {
//            return (String) value;
//        }
//        return null;
//    }

    public Map<String, String> getMap() {
        if (value instanceof Map) {
            return (Map<String, String>) value;
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
