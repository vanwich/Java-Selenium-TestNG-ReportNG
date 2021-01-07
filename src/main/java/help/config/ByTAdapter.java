package help.config;

import java.lang.reflect.Type;
import java.util.Map;
import com.google.gson.*;
import help.webdriver.ByT;
import help.webdriver.ByTComposite;

public class ByTAdapter extends LocatorAdapter<ByT> implements JsonSerializer<ByT> {
    @Override
    public JsonElement serialize(ByT src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        if (src instanceof ByTComposite) {
            JsonArray jarr = new JsonArray();
            ((ByTComposite) src).getNested().forEach(byt -> jarr.add(serialize(byt, byt.getClass(), context)));
            result.add(src.getType().value, jarr);
        } else {
            result.addProperty(src.getType().value, src.getLocatorTemplate());
        }
        return result;
    }

    @Override
    protected ByT parseEntry(Map.Entry<String, JsonElement> entry, JsonDeserializationContext context) {
        ByT.Type type = ByT.Type.fromString(entry.getKey());
        JsonElement je = entry.getValue();
        switch (type) {
            case ID: return ByT.id(je.getAsString());
            case LINK_TEXT: return ByT.linkText(je.getAsString());
            case PARTIAL_LINK_TEXT: return ByT.partialLinkText(je.getAsString());
            case NAME: return ByT.name(je.getAsString());
            case TAG_NAME: return ByT.tagName(je.getAsString());
            case XPATH: return ByT.xpath(je.getAsString());
            case CLASS_NAME: return ByT.className(je.getAsString());
            case CSS_SELECTOR: return ByT.cssSelector(je.getAsString());
            case CHAINED: return ByTComposite.chained(extractArray(je, context));
            case ALL: return ByTComposite.all(extractArray(je, context));
            default: throw new IllegalArgumentException("Failed to parse " + entry.toString() + " as instance of ByT");
        }
    }

    private ByT [] extractArray(JsonElement el, JsonDeserializationContext context) {
        JsonArray jarr = el.getAsJsonArray();
        ByT [] byarr = new ByT[jarr.size()];
        for (int i = 0; i < jarr.size(); i++) {
            byarr[i] = context.deserialize(jarr.get(i), ByT.class);
        }
        return byarr;
    }
}
