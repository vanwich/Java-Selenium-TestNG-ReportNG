package help.webdriver.controls;

import java.util.function.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import help.config.ClassConfigurator;
import help.data.TestData;
import help.webdriver.BaseElement;
import help.webdriver.controls.strategies.DefaultGetTextStrategy;
import help.webdriver.controls.waiters.Waiter;

public abstract class AbstractNonEditableStringElement extends AbstractStringElement<Void>{
    @ClassConfigurator.Configurable(byClassName = true) private static Function<WebElement, String> getValueStrategyDefault = new DefaultGetTextStrategy();
    @ClassConfigurator.Configurable(byClassName = true) private Function<WebElement, String> getValueStrategy = getValueStrategyDefault;
    private ClassConfigurator config;

    static {
        ClassConfigurator configurator = new ClassConfigurator(AbstractNonEditableStringElement.class);
        configurator.applyConfiguration();
    }

    protected AbstractNonEditableStringElement(By locator, Waiter waiter) {
        this(null, locator, waiter);
    }

    protected AbstractNonEditableStringElement(BaseElement<?, ?> parent, By locator, Waiter waiter) {
        super(parent, locator, waiter);
        config = new ClassConfigurator(this);
    }

    @Override
    public AbstractNonEditableStringElement applyConfiguration(String configurationName){
        super.applyConfiguration(configurationName);
        return (AbstractNonEditableStringElement) config.applyConfiguration(configurationName);
    }

    @Override
    protected void setRawValue(Void value) {
        throw new UnsupportedOperationException("Value of an element of class " + this.getClass() + " cannot be set.");
    }

    @Override
    protected String getRawValue() {
        return getGetValueStrategy().apply(getWebElement());
    }

    @Override
    public void fill(TestData td) {
        if (td.containsKey(name)) {
            throw new UnsupportedOperationException("Element of class " + this.getClass() + " cannot be 'filled' with test data value by key " + name);
        }
    }

    protected Function<WebElement, String> getGetValueStrategy() {
        return getValueStrategy;
    }
}
