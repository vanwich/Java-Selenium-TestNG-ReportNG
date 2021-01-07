package help.webdriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.webdriver.controls.HighlightableElement;

public class ElementHighlighter {
    private static final Logger LOG = LoggerFactory.getLogger(ElementHighlighter.class);
    private static ThreadLocal<BaseElement<?, ?>> highlightedElement = new ThreadLocal<>();
    private static final boolean IS_ENABLED = PropertyProvider.getProperty(TestProperties.ENABLE_ELEMENT_HIGHLIGHTING, false);

    public static void highlight(BaseElement<?, ?> element) {
        if (IS_ENABLED) {
            reset();
            if (element instanceof HighlightableElement) {
                try {
                    ((HighlightableElement) element).highlight();
                    highlightedElement.set(element);
                } catch (Exception e) {
                    LOG.debug("Unable to highlight element: [{}]", element.toString());
                }
            }
        }
    }

    public static void reset() {
        if (IS_ENABLED) {
            BaseElement<?, ?> element = highlightedElement.get();
            if (element != null) {
                try {
                    ((HighlightableElement) element).unhighlight();
                } catch (Exception e) {
                    LOG.debug("Unable to unhighlight element: [{}]", element.toString());
                }
            }
            highlightedElement.remove();
        }
    }
}
