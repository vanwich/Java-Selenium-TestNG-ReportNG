package help.webdriver;

import java.util.Arrays;
import java.util.stream.Stream;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import help.exceptions.JstrException;

public final class ByTComposite extends ByT{
    private ByT[] templates;

    private ByTComposite(Type type, ByT... templates) {
        super("", type);
        this.templates = templates;
    }

    @Override
    public String getLocatorTemplate() {
        throw new UnsupportedOperationException("Cannot get locator template from instance of " + ByTComposite.class.getName());
    }

    public Stream<ByT> getNested() {
        return Stream.of(templates);
    }

    public static ByT chained(ByT... templates) {
        return new ByTComposite(Type.CHAINED, templates);
    }

    public static ByT all(ByT... templates) {
        return new ByTComposite(Type.ALL, templates);
    }

    @Override
    public By format(Object... args) {
        int len = templates.length;
        By [] bys = new By[len];
        for (int i = 0; i < len; i++) {
            bys[i] = templates[i].format(args);
        }
        switch (type) {
            case CHAINED: return new ByChained(bys);
            case ALL: return new ByAll(bys);
            default: throw new JstrException("Unsupported composite locator type " + type);
        }
    }

    @Override
    public String toString() {
        return String.format("%1$s.%2$s: %3$s", getClass().getSimpleName(), type.value, Arrays.asList(templates));
    }
}
