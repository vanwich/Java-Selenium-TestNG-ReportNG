package webdriver.controls;

import org.openqa.selenium.By;
import data.TestData;
import webdriver.BaseElement;
import webdriver.controls.waiters.Waiter;

public abstract class AbstractStringElement<I> extends BaseElement<I, String> {
    protected AbstractStringElement(By locator, Waiter waiter) {
        super(locator, waiter);
    }

    protected AbstractStringElement(BaseElement<?, ?> parent, By locator, Waiter waiter) {
        super(parent, locator, waiter);
    }

    @Override
    public TestData.Type testDataType() {
        return TestData.Type.STRING;
    }

    @Override
    protected String normalize(Object rawValue) {
        return NormalizeValueHelpers.stringValue(rawValue);
    }
}
