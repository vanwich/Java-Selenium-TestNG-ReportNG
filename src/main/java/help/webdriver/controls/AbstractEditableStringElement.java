package help.webdriver.controls;

import org.openqa.selenium.By;
import help.data.TestData;
import help.webdriver.BaseElement;
import help.webdriver.controls.waiters.Waiter;

public abstract class AbstractEditableStringElement extends AbstractStringElement<String>{
    protected AbstractEditableStringElement(By locator, Waiter waiter) {
        super(locator, waiter);
    }

    protected AbstractEditableStringElement(BaseElement<?, ?> parent, By locator, Waiter waiter) {
        super(parent, locator, waiter);
    }

    @Override
    public void fill(TestData td) {
        if (td.containsKey(name)) {
            setValue(td.getValue(name));
        }
    }
}
