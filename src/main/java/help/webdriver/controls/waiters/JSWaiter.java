package help.webdriver.controls.waiters;

import java.util.function.Function;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import help.config.PropertyProvider;
import help.config.TestProperties;
import help.webdriver.BrowserController;

public class JSWaiter extends Waiter{
    private String jsCondition;
    private final String description;

    private class JSFunction implements Function<WebDriver, Boolean> {
        @Override
        public Boolean apply(WebDriver wd) {
            return (boolean) ((JavascriptExecutor) wd).executeScript(jsCondition);
        }

        @Override
        public String toString() {
            return "JS waiter {" + ((description == null) ? jsCondition : description) + "}";
        }
    }

    public JSWaiter(String jsCondition) {
        this(null, jsCondition);
    }

    public JSWaiter(String description, String jsCondition) {
        this(description, jsCondition, PropertyProvider.getProperty(TestProperties.WEBDRIVER_AJAX_TIMEOUT, 5000));
    }

    public JSWaiter(String jsCondition, long timeout) {
        this(null, jsCondition, timeout);
    }

    public JSWaiter(String description, String jsCondition, long timeout) {
        super(timeout);
        this.description = description;
        this.jsCondition = jsCondition;
    }

    @Override
    protected Waiter spawn() {
        return new JSWaiter(jsCondition, timeout);
    }

    @Override
    protected void doWait() {
        WebDriverWait wait = new WebDriverWait(BrowserController.get().driver(), timeout / 1000, Waiters.pollingInterval);
        wait.until(new JSFunction());
    }
}
