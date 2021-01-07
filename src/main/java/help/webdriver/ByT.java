package help.webdriver;

import java.util.function.Function;
import org.openqa.selenium.By;
import help.config.ClassConfigurator;
import help.exceptions.JstrException;

public class ByT {
    @ClassConfigurator.Configurable(byClassName = true)
    protected static Function<String, String> templateTransformer = Function.identity();

    static {
        new ClassConfigurator(ByT.class).applyConfiguration();
    }

    public enum Type {
        ID("id"),
        LINK_TEXT("linkText"),
        PARTIAL_LINK_TEXT("partialLinkText"),
        NAME("name"),
        TAG_NAME("tagName"),
        XPATH("xpath"),
        CLASS_NAME("className"),
        CSS_SELECTOR("cssSelector"),
        CHAINED("chained"),
        ALL("all");

        public final String value;

        Type(String value) {
            this.value = value;
        }

        public static Type fromString(String s) {
            for (Type t : values()) {
                if (t.value.equals(s)) {
                    return t;
                }
            }
            return null;
        }
    };

    final String locatorTemplate;
    final Type type;

    protected ByT(String lt, Type t) {
        locatorTemplate = templateTransformer.apply(lt);
        type = t;
    }

    public Type getType() {
        return type;
    }

    public String getLocatorTemplate() {
        return locatorTemplate;
    }

    public static ByT id(final String id) {
        return new ByT(id, Type.ID);
    }

    public static ByT linkText(final String linkText) {
        return new ByT(linkText, Type.LINK_TEXT);
    }

    public static ByT partialLinkText(final String linkText) {
        return new ByT(linkText, Type.PARTIAL_LINK_TEXT);
    }

    public static ByT name(final String name) {
        return new ByT(name, Type.NAME);
    }

    public static ByT tagName(final String tagName) {
        return new ByT(tagName, Type.TAG_NAME);
    }

    public static ByT xpath(final String xpathExpression) {
        return new ByT(xpathExpression, Type.XPATH);
    }

    public static ByT className(final String className) {
        return new ByT(className, Type.CLASS_NAME);
    }

    public static ByT cssSelector(final String selector) {
        return new ByT(selector, Type.CSS_SELECTOR);
    }

    /**
     * Format locator template using provided arguments (similar to {@link String#format(String, Object...)}
     * @param args arguments for formatting
     * @return instance of By
     */
    public By format(Object... args) {
        String locator = String.format(locatorTemplate, args);
        switch (type) {
            case ID: return By.id(locator);
            case LINK_TEXT: return By.linkText(locator);
            case PARTIAL_LINK_TEXT: return By.partialLinkText(locator);
            case NAME: return By.name(locator);
            case TAG_NAME: return By.tagName(locator);
            case XPATH: return By.xpath(locator);
            case CLASS_NAME: return By.className(locator);
            case CSS_SELECTOR: return By.cssSelector(locator);
            default: throw new JstrException("Unsupported primitive locator type " + type);
        }
    }

    @Override
    public String toString() {
        return String.format("%1$s.%2$s: %3$s", getClass().getSimpleName(), type.value, locatorTemplate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ByT byt = (ByT) o;
        return toString().equals(byt.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
