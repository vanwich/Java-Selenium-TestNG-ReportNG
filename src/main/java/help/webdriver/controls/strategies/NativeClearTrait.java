package help.webdriver.controls.strategies;

import org.openqa.selenium.WebElement;

public interface NativeClearTrait extends TextInputStrategy{
    @Override
    default void clearText(WebElement we) {
        we.clear();
    }
}
