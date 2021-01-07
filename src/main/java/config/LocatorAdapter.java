package config;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import com.google.gson.*;

abstract class LocatorAdapter<L> implements JsonSerializer<L>, JsonDeserializer<L> {
    @Override
    public L deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        L by = null;
        JsonObject jo = json.getAsJsonObject();
        Iterator<Map.Entry<String, JsonElement>> itr = jo.entrySet().iterator();
        if (itr.hasNext()) {
            by = parseEntry(itr.next(), context);
        }
        if (itr.hasNext()) {
            throw new IllegalArgumentException("Only one key-value pair is allowed for a serialized locator! Actual JSON: " + json);
        }
        return by;
    }

    protected abstract L parseEntry(Map.Entry<String, JsonElement> entry, JsonDeserializationContext context);
}
