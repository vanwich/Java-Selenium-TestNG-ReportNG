package webdriver.controls;

import org.openqa.selenium.WebElement;
import webdriver.BrowserController;

public interface HighlightableElement {
    WebElement getWebElement();

    default void highlight() {
        BrowserController.get().executeScript("arguments[0].style.border='3px solid red'", getWebElement());
    }

    default void unhighlight() {
        BrowserController.get().executeScript("arguments[0].style.border=''", getWebElement());
    }
}
