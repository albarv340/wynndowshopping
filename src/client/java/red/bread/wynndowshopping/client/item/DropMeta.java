package red.bread.wynndowshopping.client.item;

import java.util.List;

public class DropMeta {
    public List<Integer> coordinates;
    public String name;
    // Can be a list or a string
    public Object type;

    public String getType() {
        StringBuilder res = new StringBuilder();
        boolean first = true;

        if (type instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof String) {
                    if (!first) {
                        res.append(", ");
                    }
                    res.append((String) item);
                    first = false;
                } else {
                    res.append(item.toString());
                }
            }
        }
        else if (type instanceof String) {
            res.append((String) type);
        } else {
            return "";
        }

        return res.toString();
    }
}
