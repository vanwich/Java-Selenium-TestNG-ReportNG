package help.webdriver.controls.strategies.impl;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import help.webdriver.BrowserController;
import help.webdriver.controls.strategies.DefaultSelectionTrait;
import help.webdriver.controls.strategies.NativeClearTrait;

public class DefaultTextInputStrategy implements DefaultSelectionTrait, NativeClearTrait {
    @Override
    public void enterText(WebElement we, String value) {
        we.sendKeys(value);
        BrowserController.get().getActions().sendKeys(we, Keys.TAB).build().perform();
    }
}
