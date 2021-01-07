package webdriver;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.ClassConfigurator;
import config.HasConfiguration;
import config.PropertyProvider;
import config.TestProperties;
import data.TestData;
import exceptions.JstrException;
import utils.meters.WaitMeters;
import webdriver.controls.composite.asserts.AbstractContainer;
import webdriver.controls.waiters.Waiter;

abstract public class BaseElement<I,O> implements Named, HasConfiguration {
    protected static final Logger log = LoggerFactory.getLogger(BaseElement.class);
    protected BaseElement<?, ?> parent;
    protected By locator;
    protected Waiter waiter;
    protected String name;
    private static final Boolean IS_ENSURE_VISIBLE = PropertyProvider.getProperty(TestProperties.WEBDRIVER_ELEMENT_ENSURE_VISIBLE, true);
    private static final Boolean IS_ENSURE_VISIBLE_EAGERLY = PropertyProvider.getProperty(TestProperties.WEBDRIVER_ELEMENT_ENSURE_VISIBLE_EAGERLY, false);
    protected static final int WAIT_TIMEOUT = PropertyProvider.getProperty(TestProperties.WEBDRIVER_TIMEOUT, 10000);
    protected static final Boolean CHECK_INTERACTIVITY = PropertyProvider.getProperty(TestProperties.ENABLE_ELEMENT_INTERACTIVITY_CHECK, false);
    private static final boolean RENDER_LOCATORS = PropertyProvider.getProperty(TestProperties.WEBDRIVER_ELEMENT_RENDER_LOCATORS, true);
    /*	needed to delegate certain methods to parent assetlist. usually but not necessarily (in case of logical containers) equal to parent element */
    protected Optional<AbstractContainer<?, ?>> container = Optional.empty();
    private ClassConfigurator config;

    @ClassConfigurator.Configurable
    private static String ensureVisibleJs = "arguments[0].scrollIntoView();";
    @ClassConfigurator.Configurable(byClassName = true) private Predicate<BaseElement<I, O>> isRequiredPredicate =
            el -> el.container.map(c -> c.isAssetRequired(this.getName())).orElseThrow(() -> new UnsupportedOperationException("isRequired operation is supported only for assets or elements with configured isRequiredPredicate"));

    static {
        ClassConfigurator configurator = new ClassConfigurator(BaseElement.class);
        configurator.applyConfiguration();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BaseElement<I, O> applyConfiguration(String configurationName){
        return (BaseElement<I, O>) config.applyConfiguration(configurationName);
    }

    /**
     * Create element without a parent
     * @param locator element locator
     * @param waiter object describing wait mode and timeout
     */
    protected BaseElement(By locator, Waiter waiter) {
        this(null, locator, waiter);
    }

    /**
     * Create element with a parent
     * @param parent parent of the current element
     * @param locator element locator
     * @param waiter object describing wait mode and timeout
     */
    protected BaseElement(BaseElement<?, ?> parent, By locator, Waiter waiter) {
        this.parent = parent;
        this.locator = locator;
        this.waiter = waiter;
        this.config = new ClassConfigurator(this);
    }

    @Override
    public String toString() {
        String locStr = locator.toString();
        String nm;
        if (RENDER_LOCATORS) {
            nm = (name == null) ? "<unnamed>" : name;
            return "{" + nm + ": " + ((parent == null) ? locStr : parent.toString() + " -> " + locStr) + "}";
        } else {
            nm = (name == null) ? locStr : name;
            return "{" + nm + "}";
        }
    }

    /**
     * Get underlying WebElement instance
     * @return WebElement instance
     */
    public WebElement getWebElement() {
        return BrowserController.get().getElement(this);
    }

    /**
     * Get locator of the current element
     * @return locator
     */
    public By getLocator() {
        return locator;
    }

    /**
     * Get parent of the current element
     * @return parent element or null if there is no parent
     */
    public BaseElement<?, ?> getParent() {
        return parent;
    }

    /**
     * Wait for page update using current element's wait mode and timeout
     */
    public void waitForPageUpdate() {
        waiter.go();
    }

    /**
     * Wait for default timeout for predicate to evaluate to true
     * @param predicate predicate to wait for
     */
    public void waitFor(Predicate<? super BaseElement<?, ?>> predicate) {
        waitFor(WAIT_TIMEOUT, predicate);
    }

    /**
     * Wait for specified timeout (in ms) for predicate to evaluate to true
     * @param timeout wait timeout
     * @param predicate predicate predicate to wait for
     */
    public void waitFor(int timeout, Predicate<? super BaseElement<?, ?>> predicate) {
        waitFor(timeout, predicate, String.format("Predicate %1$s on %2$s did not evaluate to true within %3$d ms", predicate, this, timeout));
    }

    /**
     * Wait for specified timeout (in ms) for predicate to evaluate to true
     * @param timeout wait timeout
     * @param predicate predicate predicate to wait for
     * @param message message to be displayed in case of timeout expiration
     */
    public void waitFor(int timeout, Predicate<? super BaseElement<?, ?>> predicate, String message) {
        new FluentWait<BaseElement<?, ?>>(this)
                .withMessage(message)
                .withTimeout(Duration.ofMillis(timeout))
                .pollingEvery(Duration.ofSeconds(1L))
                .ignoring(NotFoundException.class)
                .until(be -> predicate.test(be));
    }

    /**
     * Wait for this element to become present and visible
     * @param timeout  wait timeout
     */
    public void waitForAccessible(int timeout) {
        waitForAccessible(timeout, true);
    }

    /**
     * Wait for this element's presence and visibility status to become as specified
     * @param timeout  wait timeout
     * @param status true for present/visible, false for absent/invisible
     */
    public void waitForAccessible(int timeout, final boolean status) {
        waitFor(timeout, e -> e.isPresent() == status, String.format("Element %1$s did not become accessible within %2$d ms", this, timeout));
    }

    /**
     * Set element's name (to be used to get values from test data)
     * @param name element's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get element's name
     * @return element's name or null if there is none
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Check if element is present and visible
     * @return true if visible, false if invisible or not present
     */
    public boolean isPresent() {
        try {
            return getWebElement().isDisplayed();
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * Check if element is visible. Same as {@link #isPresent()} but throws exception if element is not present
     * @return true if visible, false if invisible
     */
    public boolean isVisible() {
        return getWebElement().isDisplayed();
    }

    /**
     * Check if element is enabled
     * @return element's enabled status
     */
    public boolean isEnabled() {
        return getWebElement().isEnabled();
    }

    /**
     * Perform interactivity check if corresponding property is set
     * This is essentially an alias for {@link #isEnabled()}. It was introduced to be able to skip interactivity checks on container elements
     * and still use their original isEnabled method when needed.
     */
    protected void ensureInteractive() {
        if (CHECK_INTERACTIVITY && !isEnabled()) {
            throw new JstrException(String.format("%1$s %2$s is disabled", this.getClass().getSimpleName(), this));
        }
    }

    /**
     * Get element's output test data type
     * @return test data type
     */
    public abstract TestData.Type testDataType();

    /**
     * Method to transform raw value (usually received from test data) to element's output value.
     * @param rawValue raw value
     * @return transformed value
     */
    protected abstract O normalize(Object rawValue);

    /**
     * Set element's value and handle errors
     * @param value new value
     * @throws JstrException with standardized message
     */
    public void setValue(I value) {
        log.debug("Setting value '" + value + "' in the control " + this);
        ElementHighlighter.highlight(this);
        ensureInteractive();
        if (IS_ENSURE_VISIBLE_EAGERLY) {
            ensureVisible();
        }

        try {
            setRawValue(value);
        } catch (Exception e) {
            String errorMessage;
            Exception suppressed = null;
            try {
                errorMessage = buildSetValueErrorMessage(value, e);
            } catch (Exception e1) {
                errorMessage = String.format("Cannot set value of %1$s %2$s to '%3$s'", this.getClass().getSimpleName(), this, value);
                suppressed = e1;
            }

            JstrException mainException = new JstrException(errorMessage, e);
            if (suppressed != null) {
                mainException.addSuppressed(suppressed);
            }

            throw mainException;
        }
    }

    /**
     * Set element's value with overridden wait mode
     * @param value new value
     * @param waiter waiter to use after setting value
     */
    public void setValue(I value, Waiter waiter) {
        //	TODO make thread-safe
        Waiter saveWaiter = this.waiter;
        this.waiter = waiter;
        setValue(value);
        this.waiter = saveWaiter;
    }

    /**
     * Set element's value (without error handling)
     * @param value new value
     */
    protected abstract void setRawValue(I value);

    /**
     * Build error message for setValue() method. Can be overridden in subclasses to include more information (e.g. available values).
     * @param value element value for which error has occurred
     * @param exception exception that was thrown during setValue() execution
     * @return error message to be used in the thrown exception
     */
    protected String buildSetValueErrorMessage(final I value, final Exception exception) {
        return String.format("Cannot set value of %1$s %2$s to '%3$s'", this.getClass().getSimpleName(), this, value);
    }

    /**
     * Get element's value and handle errors
     * @return element value
     * @throws exceptions.JstrException with standardized message
     */
    public O getValue() {
        ElementHighlighter.highlight(this);
        try {
            return getRawValue();
        } catch (Exception e) {
            throw new JstrException(String.format("Cannot get value of %1$s %2$s",
                    this.getClass().getSimpleName(), this), e);
        }
    }

    /**
     * Get element's value using custom extractor
     * @param <T> desired value type
     * @param valueExtractor function to extract desired value from the WebElement
     * @return element value
     */
    public <T> T getValue(Function<WebElement, T> valueExtractor) {
        ElementHighlighter.highlight(this);
        try {
            return valueExtractor.apply(getWebElement());
        } catch (Exception e) {
            throw new JstrException(String.format("Cannot get value of %1$s %2$s",
                    this.getClass().getSimpleName(), this), e);
        }
    }

    /**
     * Get element's value (without error handling)
     * @return value element value
     */
    protected abstract O getRawValue();

    /**
     * Get element's partial value according to template. Should return full value for non-composite controls.
     * This method is used internally by the framework and normally should not be called by the client code.
     * @param template value template
     * @return element's partial value
     */
    public O getPartialValue(Object template) {
        return getValue();
    }

    /**
     * Fill element using element's name as a key from provided TestData
     * @param td TestData object that should contain appropriate value of type &lt;T&gt; under the appropriate key
     */
    public abstract void fill(TestData td);

    /**
     * Get element's attribute
     * @param attrName attribute name
     * @return attribute value
     */
    public String getAttribute(String attrName) {
        return getWebElement().getAttribute(attrName);
    }

    /**
     * Ensure the element is visible prior to interaction
     */
    protected void ensureVisible() {
        if (IS_ENSURE_VISIBLE) {
            BrowserController.get().executeScript(BaseElement.ensureVisibleJs, getWebElement());
        }
    }

    /**
     * Click the element
     */
    protected void click() {
        click(waiter);
    }

    /**
     * Click the element with overridden wait mode
     * @param waiter waiter to use after click
     */
    protected void click(Waiter waiter) {
        log.debug("Clicking control " + this);
        ElementHighlighter.highlight(this);
        ensureVisible();
        try {
            getWebElement().click();
        } catch (TimeoutException te) {
            throw new JstrException(String.format("Page failed to reload in time after click on %1$s", this), te);
        }
        WaitMeters.capture(WaitMeters.PAGE_LOAD);
        waiter.go();
    }

    /**
     * Hover mouse cursor over the element
     */
    public void mouseOver() {
        Actions action = new Actions(BrowserController.get().driver());
        action.moveToElement(getWebElement()).build().perform();
    }

    /**
     * Double-click the element
     */
    protected void doubleClick() {
        log.debug("Double-clicking control " + this);
        ElementHighlighter.highlight(this);
        ensureVisible();
        Actions action = new Actions(BrowserController.get().driver());
        action.doubleClick(getWebElement()).build().perform();
        waiter.go();
    }

    /**
     * Clear element
     */
    public void clear() {
        getWebElement().clear();
    }

    /**
     * Set element's container (if it is an asset in an asset list)
     * @param container asset list
     */
    public void setContainer(AbstractContainer<?, ?> container) {
        this.container = Optional.ofNullable(container);
    }

    /**
     * Check if asset is required (throws UnsupportedOperationException for non-asset elements unless isRequiredPredicate is configured)
     * @return true if required
     */
    public boolean isRequired() {
        return isRequiredPredicate.test(this);
    }

    /**
     * Get asset's warning (always empty for non-asset elements)
     * @return optional of asset's warning
     */
    public Optional<String> getWarning() {
        return container.flatMap(c -> {
            BaseElement<?, String> we = c.getWarning(this.getName());
            try {
                String value = we.getValue();
                return StringUtils.isBlank(value) ? Optional.empty() : Optional.of(value);
            } catch (Exception e) {
                return Optional.empty();
            }
        });
    }
}
