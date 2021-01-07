package help.webdriver.controls.composite.asserts.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.SortedSet;
import java.util.TreeSet;
import org.openqa.selenium.By;
import help.exceptions.JstrException;
import help.webdriver.BaseElement;
import help.webdriver.controls.composite.asserts.AbstractContainer;
import help.webdriver.controls.waiters.Waiter;
import help.webdriver.controls.waiters.Waiters;

public class MetaData {
    /**
     * Create builder non-container asset with default waiter, locator, hasParent = true and isChained = false
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @return new instance of {@link AssetDescriptor.Builder}
     */
    protected static <C extends BaseElement<?, ?>> AssetDescriptor.Builder<C> define(String label, Class<C> controlClass) {
        return new AssetDescriptor.Builder<C>(label, controlClass);
    }

    /**
     * Create builder container asset with default waiter, locator, hasParent = true and isChained = false
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param metaClass container metadata class
     * @return new instance of {@link AssetDescriptor.Builder}
     */
    protected static <C extends AbstractContainer<?, ?>> AssetDescriptor.Builder<C> define(String label, Class<C> controlClass, Class<? extends MetaData> metaClass) {
        return new AssetDescriptor.Builder<C>(label, controlClass, metaClass);
    }


    /**
     * Declare asset with all properties specified explicitly
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param waiter asset wait mode and timeout
     * @param hasParent specifies whether the control should have parent (AssetList) or accessed independently
     * @param locator asset locator (remember to use "." in XPath locators with hasParent = true)
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends BaseElement<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Waiter waiter, boolean hasParent, By locator) {
        return new AssetDescriptor<C>(label, controlClass, waiter, null, hasParent, locator, false);
    }

    /**
     * Declare asset with all properties specified explicitly and default locator
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param waiter asset wait mode and timeout
     * @param hasParent specifies whether the control should have parent (AssetList) or accessed independently
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends BaseElement<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Waiter waiter, boolean hasParent) {
        return new AssetDescriptor<C>(label, controlClass, waiter, null, hasParent, null, false);
    }

    /**
     * Declare asset with parent, all remaining properties specified explicitly
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param waiter asset wait mode and timeout
     * @param locator asset locator (remember to use "." in XPath locators with hasParent = true)
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends BaseElement<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Waiter waiter, By locator) {
        return new AssetDescriptor<C>(label, controlClass, waiter, null, true, locator, false);
    }

    /**
     * Declare asset with parent and default locator, all remaining properties specified explicitly
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param waiter asset wait mode and timeout
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends BaseElement<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Waiter waiter) {
        return new AssetDescriptor<C>(label, controlClass, waiter, null, true, null, false);
    }

    /**
     * Declare asset with parent with default wait mode
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param locator asset locator (remember to use "." in XPath locators with hasParent = true)
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends BaseElement<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, By locator) {
        return declare(label, controlClass, Waiters.DEFAULT, locator);
    }

    /**
     * Declare asset with parent with default wait mode and locator
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends BaseElement<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass) {
        return declare(label, controlClass, Waiters.DEFAULT);
    }

    /**
     * Declare asset with its own metadata class (for nested asset lists)
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param metaClass asset metadata class
     * @param hasParent specifies whether the control should have parent (AssetList) or accessed independently
     * @param locator asset locator (remember to use "." in XPath locators with hasParent = true)
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends AbstractContainer<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Class<? extends MetaData> metaClass, boolean hasParent, By locator) {
        return new AssetDescriptor<C>(label, controlClass, Waiters.DEFAULT, metaClass, hasParent, locator, false);
    }

    /**
     * Declare asset with its own metadata class and default locator (for nested asset lists)
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param metaClass asset metadata class
     * @param hasParent specifies whether the control should have parent (AssetList) or accessed independently
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends AbstractContainer<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Class<? extends MetaData> metaClass, boolean hasParent) {
        return new AssetDescriptor<C>(label, controlClass, Waiters.DEFAULT, metaClass, hasParent, null, false);
    }

    /**
     * Declare asset with parent with its own metadata class (for nested asset lists)
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param metaClass asset metadata class
     * @param locator asset locator (remember to use "." in XPath locators with hasParent = true)
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends AbstractContainer<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Class<? extends MetaData> metaClass, By locator) {
        return declare(label, controlClass, metaClass, true, locator);
    }

    /**
     * Declare asset with parent with its own metadata class and default locator (for nested asset lists)
     * @param <C> asset control type
     * @param label asset label
     * @param controlClass asset control type
     * @param metaClass asset metadata class
     * @return new instance of {@link AssetDescriptor}
     * @deprecated Use {@link #define(String, Class, Class)} with modifier methods and {@link AssetDescriptor.Builder#build()}
     */
    @Deprecated
    protected static <C extends AbstractContainer<?, ?>> AssetDescriptor<C> declare(String label, Class<C> controlClass, Class<? extends MetaData> metaClass) {
        return declare(label, controlClass, metaClass, true);
    }

    /**
     * Get list of assets in specified metadata class in the order of declaration
     * @param clazz metadata class to get assets from
     * @return set of asset descriptors
     */
    public static SortedSet<AssetDescriptor<?>> getAssets(Class<? extends MetaData> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        SortedSet<AssetDescriptor<?>> assetFields = new TreeSet<AssetDescriptor<?>>(AssetDescriptor.getComparator());

        for (Field field : fields) {
            int modifiers = field.getModifiers();

            if (Modifier.isStatic(modifiers) &&
                    Modifier.isPublic(modifiers) &&
                    field.getType().equals(AssetDescriptor.class)) {
                try {
                    assetFields.add((AssetDescriptor<?>) field.get(null));
                } catch (Exception e) {
                    throw new JstrException("Something weird happened here. We should never get here.", e);
                }
            }
        }
        return assetFields;
    }
}
