package help.webdriver.controls.strategies;

import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import help.webdriver.BrowserController;

public interface DefaultSelectionTrait  extends TextInputStrategy{
    default void selectText(WebElement we) {
        if (BrowserController.get().getPlatform().is(Platform.MAC)) {
            BrowserController.get().executeScript("arguments[0].select()", we);
        } else {
            we.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        }
    }
}
