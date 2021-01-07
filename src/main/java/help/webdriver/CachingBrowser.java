package help.webdriver;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.WeakHashMap;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Locatable;
import help.exceptions.JstrException;

public class CachingBrowser extends Browser{
    public Map<BaseElement<?, ?>, WebElement> elementMap;

    CachingBrowser(WebDriver driver) {
        super(driver);
        elementMap = new WeakHashMap<>();
    }

    @Override
    public WebElement getElement(BaseElement<?, ?> el) {
        if (elementMap.containsKey(el)) {
            log.trace("Retrieving element from cache for " + el);
            return elementMap.get(el);
        } else {
            return addEntry(el);
        }
    }

    private WebElement addEntry(BaseElement<?, ?> be) {
        InvocationHandler handler = new WebElementHandler(be);
        WebElement proxy = (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                new Class [] {WebElement.class, WrapsDriver.class, WrapsElement.class, Locatable.class}, handler);
        log.trace("Adding element to cache for " + be);
        elementMap.put(be, proxy);
        return proxy;
    }

    private void resetCache() {
        elementMap.clear();
    }

    @Override
    public void open(String url) {
        resetCache();
        super.open(url);
    }

    @Override
    public void quit() {
        resetCache();
        super.quit();
    }

    @Override
    public Object executeScript(String script, Object... args) {
        //	this method is overridden to always refresh cached elements because there is no way to intercept exceptions thrown by JS execution
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Proxy) {
                InvocationHandler ih = Proxy.getInvocationHandler(args[i]);
                if (ih instanceof WebElementHandler) {
                    ((WebElementHandler) ih).refreshWebElement("executeScript");
                }
            }
        }

        return super.executeScript(script, args);
    }

    private class WebElementHandler implements InvocationHandler, WrapsElement {
        private WeakReference<BaseElement<?, ?>> baseElement;
        private WebElement webElement;

        WebElementHandler(BaseElement<?, ?> be) {
            this.baseElement = new WeakReference<BaseElement<?, ?>>(be);
            this.webElement = findElement(be);
        }

        private BaseElement<?, ?> getBaseElement() {
            BaseElement<?, ?> be = baseElement.get();
            if (be == null) {
                throw new JstrException("Base element related to " + webElement + " has been cleared by GC. This is a bug!");
            } else {
                return be;
            }
        }

        private void refreshWebElement(String methodName) {
            BaseElement<?, ?> be = getBaseElement();
            log.trace("Refreshing cache for element " + be + " for method " + methodName);
            this.webElement = findElement(be);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            Object instance = (method.getDeclaringClass().equals(WrapsElement.class)) ? this : webElement;
            try {
                result = method.invoke(instance, args);
            } catch (InvocationTargetException ite) {
                Throwable cause = ite.getCause();
                if (isRefreshNeeded(cause)) {
                    refreshWebElement(method.getName());
                    result = invoke(proxy, method, args);
                } else {
                    throw cause;
                }
            }
            return result;
        }

        @Override
        public WebElement getWrappedElement() {
            return webElement;
        }

        private boolean isRefreshNeeded(Throwable ex) {
            return (ex instanceof StaleElementReferenceException) ||
                    //	Gecko-FF-specific branch
                    (ex instanceof NoSuchElementException &&
                            ex.getMessage() != null &&
                            ex.getMessage().contains("Web element reference not seen before"));
        }
    }
}
