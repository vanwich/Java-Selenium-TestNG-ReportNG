package help.data;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestDataCache {
    private static Map<String, Map<String, Object>> cache = new LinkedHashMap<String, Map<String, Object>>();
    private static final String SEPARATOR = "!";

    private TestDataCache() {
    }

    public static Map<String, Object> get(String groupName, String itemName) {
        return cache.get(getKeyName(groupName, itemName));
    }

    public static void put(String groupName, String itemName, Map<String, Object> data) {
        cache.put(getKeyName(groupName, itemName), data);
    }

    private static String getKeyName(String groupName, String itemName) {
        return groupName + SEPARATOR + itemName;
    }

    public static boolean hasData(String groupName, String itemName) {
        return cache.containsKey(getKeyName(groupName, itemName));
    }
}
