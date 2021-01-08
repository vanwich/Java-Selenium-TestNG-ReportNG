package help.webdriver.controls;

import java.util.function.Predicate;
import org.openqa.selenium.By;
import help.config.ClassConfigurator;
import help.exceptions.JstrException;
import help.webdriver.BaseElement;
import help.webdriver.controls.waiters.Waiter;
import help.webdriver.controls.waiters.Waiters;

public class Link extends AbstractClickableStringElement implements HighlightableElement{
    private ClassConfigurator config;
    @ClassConfigurator.Configurable(byClassName = true)
    private Predicate<Link> isEnabledPredicate = e -> {
        //	this condition does not look valid but leaving it as is for historical reasons
        String onClickAtr = e.getAttribute("onclick");
        return (onClickAtr == null || onClickAtr.matches(".+\\(.*\\).*"));
    };

    public Link(By locator) {
        this(locator, Waiters.DEFAULT);
    }

    public Link(By locator, Waiter waiter) {
        this(null, locator, waiter);
    }

    public Link(BaseElement<?, ?> parent, By locator) {
        this(parent, locator, Waiters.DEFAULT);
    }

    public Link(BaseElement<?, ?> parent, By locator, Waiter waiter) {
        super(parent, locator, waiter);
        this.config = new ClassConfigurator(this);
    }

    public Link applyConfiguration(String configurationName){
        return (Link) config.applyConfiguration(configurationName);
    }

    @Override
    protected String getRawValue() {
        return getWebElement().getText();
    }

    @Override
    public boolean isEnabled() {
        try {
            return super.isEnabled() && isEnabledPredicate.test(this);
        } catch (Exception e) {
            throw new JstrException(String.format("Can't find Link %s", this), e);
        }
    }
}
