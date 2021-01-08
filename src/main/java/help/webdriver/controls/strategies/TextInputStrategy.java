package help.webdriver.controls.strategies;

import org.openqa.selenium.WebElement;
import help.webdriver.controls.AbstractEditableStringElement;

public interface TextInputStrategy {
    default void setValue(AbstractEditableStringElement e, String value) {
        WebElement we = e.getWebElement();
        if (value == null || value.isEmpty()) {
            clearText(we);
        } else {
            selectText(we);
            enterText(we, value);
        }
    }

    void selectText(WebElement we);
    void enterText(WebElement we, String value);
    void clearText(WebElement we);
}
