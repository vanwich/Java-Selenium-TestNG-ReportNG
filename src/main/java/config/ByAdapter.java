package config;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import com.google.gson.*;

public class ByAdapter extends LocatorAdapter<By> implements JsonSerializer<By> {

    @Override
    public JsonElement serialize(By src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        Pattern p = Pattern.compile("^By\\.(\\w+): (.*)$");
        Matcher m = p.matcher(src.toString());
        if (m.matches()) {
            result.addProperty(m.group(1), m.group(2));
        } else {
            throw new IllegalArgumentException("Cannot serialize object " + src);
        }
        return result;
    }


    @Override
    protected By parseEntry(Map.Entry<String, JsonElement> entry, JsonDeserializationContext context) {
        By by = null;
        JsonElement val = entry.getValue();
        switch (entry.getKey()) {
            case "id":
                by = By.id(val.getAsString());
                break;
            case "linkText":
                by = By.linkText(val.getAsString());
                break;
            case "partialLinkText":
                by = By.partialLinkText(val.getAsString());
                break;
            case "name":
                by = By.name(val.getAsString());
                break;
            case "tagName":
                by = By.tagName(val.getAsString());
                break;
            case "xpath":
                by = By.xpath(val.getAsString());
                break;
            case "className":
                by = By.className(val.getAsString());
                break;
            case "cssSelector":
                by = By.cssSelector(val.getAsString());
                break;
            case "idOrName":
                by = new ByIdOrName(val.getAsString());
                break;
            case "all":
                by = new ByAll(extractArray(val, context));
                break;
            case "chained":
                by = new ByChained(extractArray(val, context));
                break;
            default:
                throw new IllegalArgumentException("Failed to parse " + val.toString() + " as instance of By");
        }
        return by;
    }

    private By [] extractArray(JsonElement el, JsonDeserializationContext context) {
        JsonArray jarr = el.getAsJsonArray();
        By [] byarr = new By[jarr.size()];
        for (int i = 0; i < jarr.size(); i++) {
            byarr[i] = context.deserialize(jarr.get(i), By.class);
        }
        return byarr;
    }
}
