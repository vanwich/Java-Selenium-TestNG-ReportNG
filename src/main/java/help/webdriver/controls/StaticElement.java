package help.webdriver.controls;

import org.openqa.selenium.By;
import help.webdriver.BaseElement;
import help.webdriver.controls.waiters.Waiter;
import help.webdriver.controls.waiters.Waiters;

public class StaticElement extends AbstractNonEditableStringElement implements HighlightableElement{
    public StaticElement(By locator) {
        super(locator, Waiters.DEFAULT);
    }

    public StaticElement(By locator, Waiter waiter) {
        super(locator, waiter);
    }

    public StaticElement(BaseElement<?, ?> parent, By locator) {
        super(parent, locator, Waiters.DEFAULT);
    }

    public StaticElement(BaseElement<?, ?> parent, By locator, Waiter waiter) {
        super(parent, locator, waiter);
    }

    @Override
    protected void ensureInteractive() {
        //	NOOP
    }

    @Override
    protected void ensureVisible() {
        //	NOOP - no need to ensure visibility for static element
    }
}
