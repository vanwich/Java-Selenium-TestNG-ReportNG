package webdriver.controls.strategies;

import java.util.function.Function;
import org.openqa.selenium.WebElement;

public class DefaultGetTextStrategy implements Function<WebElement, String> {
    @Override
    public String apply(WebElement we) {
        return we.getText();
    }
}
