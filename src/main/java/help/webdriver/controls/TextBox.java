package help.webdriver.controls;

import org.openqa.selenium.By;
import help.config.ClassConfigurator;
import help.config.HasConfiguration;
import help.utils.meters.WaitMeters;
import help.webdriver.BaseElement;
import help.webdriver.controls.strategies.TextInputStrategy;
import help.webdriver.controls.strategies.impl.DefaultTextInputStrategy;
import help.webdriver.controls.waiters.Waiter;
import help.webdriver.controls.waiters.Waiters;

public class TextBox extends AbstractEditableStringElement implements HighlightableElement, HasConfiguration {
    private ClassConfigurator config;

    @ClassConfigurator.Configurable(byClassName = true)
    private static TextInputStrategy textInputStrategyDefault = new DefaultTextInputStrategy();

    @ClassConfigurator.Configurable(byClassName = true)
    private TextInputStrategy textInputStrategy = textInputStrategyDefault;

    static {
        ClassConfigurator configurator = new ClassConfigurator(TextBox.class);
        configurator.applyConfiguration();
    }

    public TextBox(By locator) {
        this(locator, Waiters.DEFAULT);
    }

    public TextBox(By locator, Waiter waiter) {
        this(null, locator, waiter);
    }

    public TextBox(BaseElement<?, ?> parent, By locator) {
        this(parent, locator, Waiters.DEFAULT);
    }

    public TextBox(BaseElement<?, ?> parent, By locator, Waiter waiter) {
        super(parent, locator, waiter);
        config = new ClassConfigurator(this);
    }

    @Override
    public TextBox applyConfiguration(String configurationName){
        super.applyConfiguration(configurationName);
        return (TextBox) config.applyConfiguration(configurationName);
    }

    @Override
    protected void setRawValue(String value) {
        getTextInputStrategy().setValue(this, value);
        WaitMeters.capture(WaitMeters.PAGE_LOAD);
        waitForPageUpdate();
    }

    @Override
    protected String getRawValue() {
        return getAttribute("value").trim();
    }

    protected TextInputStrategy getTextInputStrategy() {
        return textInputStrategy;
    }
}
