package help.webdriver.controls.composite.asserts.metadata;

import java.util.Comparator;
import org.openqa.selenium.By;
import help.webdriver.BaseElement;
import help.webdriver.controls.composite.asserts.AbstractContainer;
import help.webdriver.controls.waiters.Waiter;
import help.webdriver.controls.waiters.Waiters;

public class AssetDescriptor<C extends BaseElement<?,?>> {
    private final String label;
    private final Class<C> controlClass;
    private final Waiter waiter;
    private final Class<? extends MetaData> metaClass;
    private final long order;
    private final boolean hasParent;
    private final By locator;
    private final boolean isChained;

    /**
     * Asset descriptor builder
     * @param <C> asset type
     */
    public static class Builder<C extends BaseElement<?, ?>> {
        private final String label;
        private final Class<C> controlClass;
        private final Class<? extends MetaData> metaClass;

        private Waiter waiter;
        private boolean hasParent;
        private By locator;
        private boolean isChained;

        Builder(String label, Class<C> controlClass, Class<? extends MetaData> metaClass) {
            this.label = label;
            this.controlClass = controlClass;
            this.metaClass = metaClass;
            this.waiter = Waiters.DEFAULT;
            this.hasParent = true;
            this.locator = null;
            this.isChained = false;
        }

        Builder(String label, Class<C> controlClass) {
            this(label, controlClass, null);
        }

        /**
         * Specify custom waiter instead of the default one
         * @param waiter new waiter
         * @return builder instance
         */
        public Builder<C> waiter(Waiter waiter) {
            this.waiter = waiter;
            return this;
        }

        /**
         * Specify custom locator (overrides asset locator template in {@link AbstractContainer})
         * @param locator new locator
         * @return builder instance
         */
        public Builder<C> locator(By locator) {
            this.locator = locator;
            return this;
        }

        /**
         * Specify that asset has no parent (i.e. its locator is resolved in the scope of the whole page)
         * Note: {@link #noParent()} and {@link #chained()} should not be used together
         * @return builder instance
         */
        public Builder<C> noParent() {
            this.hasParent = false;
            this.isChained = false;
            return this;
        }

        /**
         * Specify that asset's locator is chained with the parent's locator
         * Note: {@link #noParent()} and {@link #chained()} should not be used together
         * @return builder instance
         */
        public Builder<C> chained() {
            this.hasParent = true;
            this.isChained = true;
            return this;
        }

        /**
         * Build asset descriptor
         * @return asset descriptor instance
         */
        public AssetDescriptor<C> build() {
            return new AssetDescriptor<C>(label, controlClass, waiter, metaClass, hasParent, locator, isChained);
        }
    }

    //	TODO make private when deprecated methods in MetaData are removed
    AssetDescriptor(String label, Class<C> controlClass, Waiter waiter, Class<? extends MetaData> metaClass, boolean hasParent, By locator, boolean isChained) {
        this.label = label;
        this.controlClass = controlClass;
        this.waiter = waiter;
        this.metaClass = metaClass;
        this.hasParent = hasParent;
        this.locator = locator;
        this.isChained = isChained;
        //	order of asset descriptor in metadata class is determined by the order of declaration
        this.order = System.nanoTime();
    }

    public String getLabel() {
        return label;
    }

    public Class<C> getControlClass() {
        return controlClass;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Class<? extends MetaData> getMetaClass() {
        return metaClass;
    }

    public boolean hasParent() {
        return hasParent;
    }

    public By getLocator() {
        return locator;
    }

    public boolean isChained() {
        return isChained;
    }

    static Comparator<AssetDescriptor<?>> getComparator() {
        return new Comparator<AssetDescriptor<?>>() {
            @Override
            public int compare(AssetDescriptor<?> left, AssetDescriptor<?> right) {
                return Long.signum(left.order - right.order);
            }
        };
    }
}
