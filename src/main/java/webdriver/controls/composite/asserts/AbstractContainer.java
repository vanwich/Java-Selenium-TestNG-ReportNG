package webdriver.controls.composite.asserts;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.Quotes;
import config.ClassConfigurator;
import config.HasConfiguration;
import data.TestData;
import data.impl.SimpleDataProvider;
import exceptions.JstrException;
import webdriver.BaseElement;
import webdriver.ByT;
import webdriver.controls.StaticElement;
import webdriver.controls.composite.asserts.metadata.AssetDescriptor;
import webdriver.controls.composite.asserts.metadata.MetaData;
import webdriver.controls.waiters.Waiter;
import webdriver.controls.waiters.Waiters;

public abstract class AbstractContainer<I, O > extends BaseElement<I, O> implements HasConfiguration {
    @ClassConfigurator.Configurable
    private static String requiredLabelLocatorTemplate = "./ancestor::td[1]/preceding-sibling::td[./div[contains(@class, 'crm-required')] or ./ancestor::tr[position()<3][contains(@class, 'required')]]";
    @ClassConfigurator.Configurable
    private static String warningLocatorTemplate = "./ancestor::td[1]/following-sibling::td";
    @ClassConfigurator.Configurable
    private static String assetLocatorTemplate =
            "//tr[td[normalize-space(.)=%1$s] and not(contains(@class, 'hidden'))]/td[2]";

    @ClassConfigurator.Configurable
    protected String requiredLabelLocator = requiredLabelLocatorTemplate;
    @ClassConfigurator.Configurable
    protected String warningLocator = warningLocatorTemplate;
    @ClassConfigurator.Configurable
    protected String assetLocator = assetLocatorTemplate;

    @ClassConfigurator.Configurable
    protected Map<String, String> assetConfigurations = new LinkedHashMap<>();

    private Map<String, BaseElement<?, ?>> assetCollection = new LinkedHashMap<>();

    protected static Map<Class<? extends BaseElement<?, ?>>, String> assetControls = new LinkedHashMap<>();
    @ClassConfigurator.Configurable
    private static Map<String, String> customControls = new LinkedHashMap<>();

    private ClassConfigurator config;

    protected Class<? extends MetaData> metaDataClass = null;
    protected boolean metaDataRegistered = false;

    static {
        //Default configuration creation
//        assetControls.put(Button.class, "//input");
//        assetControls.put(CheckBox.class, "//input");
//        assetControls.put(ComboBox.class, "//select");
//        assetControls.put(Link.class, "//a");
//        assetControls.put(ListBox.class, "//select");
//        assetControls.put(RadioGroup.class, "//table");
//        assetControls.put(TextBox.class, "//*[self::input[(@type='text' or @type='password') and not(" + LocatorHelpers.STYLE_DISPLAY_NONE + ")] or self::textarea]");
//        assetControls.put(DoubleTextBox.class, "/ancestor::tr[1]");
//        assetControls.put(DoubleComboBox.class, "/ancestor::tr[1]");
        assetControls.put(StaticElement.class, "//*");
        //	TODO add when implemented
        //assetControls.put(ComboCheckbox.class, "/*");

        //Load configuration
        ClassConfigurator configurator = new ClassConfigurator(AbstractContainer.class);
        configurator.applyConfiguration();

        //Register custom controls taken from configuration
        registerCustomControls();
    }

    private static void registerCustomControls() {
        for (Map.Entry<String, String> entry: customControls.entrySet()){
            Class<?> rawClass;
            try {
                rawClass = Class.forName(entry.getKey());
            } catch (ClassNotFoundException e) {
                throw new JstrException("Class name " + entry.getKey() + " cannot be resolved");
            }
            if (BaseElement.class.isAssignableFrom(rawClass)) {
                @SuppressWarnings("unchecked")
                Class<? extends BaseElement<?, ?>> rc = (Class<? extends BaseElement<?, ?>>) rawClass;
                assetControls.put(rc, entry.getValue());
            } else {
                throw new JstrException("Class " + rawClass + " is not a subclass of BaseElement");
            }
        }
    }

    private AbstractContainer(BaseElement<?, ?> parent, By locator) {
        super(parent, locator, Waiters.DEFAULT);
        config = new ClassConfigurator(this);
    }

    public AbstractContainer(By locator) {
        this(null, locator);
    }

    public AbstractContainer(By locator, Class<? extends MetaData> metaDataClass) {
        this(null, locator, metaDataClass);
    }

    public AbstractContainer(BaseElement<?, ?> parent, By locator, Class<? extends MetaData> metaDataClass) {
        this(parent, locator);
        this.metaDataClass = metaDataClass;
        setName(metaDataClass.getSimpleName());
    }

    /**
     * Wrapper for {@link ClassConfigurator#applyConfiguration(String configurationName)} method
     * @param configurationName name of configuration going to be applied
     * @return instance of AssetList having applied configuration
     */
    @SuppressWarnings("unchecked")
    @Override
    public AbstractContainer<I, O> applyConfiguration(String configurationName){
        super.applyConfiguration(configurationName);
        AbstractContainer<I, O> retValue = (AbstractContainer<I, O>) config.applyConfiguration(configurationName);
        getAssetCollection().entrySet().forEach(entry -> applyAssetConfiguration(entry.getKey(), entry.getValue()));
        return retValue;
    }

    protected void applyAssetConfiguration(String assetName, BaseElement<?, ?> asset) {
        if (assetConfigurations.containsKey(assetName)) {
            String configName = assetConfigurations.get(assetName);
            if (asset instanceof HasConfiguration) {
                ((HasConfiguration) asset).applyConfiguration(configName);
            } else {
                log.warn("Attempt to configure non-configurable asset {} with configuration {}", assetName, configName);
            }
        }
    }

    protected Map<String, BaseElement<?, ?>> getAssetCollection() {
        registerMetaData();
        return assetCollection;
    }

    /**
     * Force register assets from metadata (if defined)
     */
    protected void registerMetaData() {
        if (!metaDataRegistered && metaDataClass != null) {
            for (AssetDescriptor<?> ad : MetaData.getAssets(metaDataClass)) {
                registerAsset(ad.getLabel(), ad.getControlClass(), ad.getWaiter(), ad.getMetaClass(), ad.hasParent(), ad.getLocator(), ad.isChained());
            }
            metaDataRegistered = true;
        }
    }

    protected void registerAsset(String assetName, Class<? extends BaseElement<?, ?>> controlClass,
            Waiter waiter, Class<? extends MetaData> metaClass,
            boolean hasParent, By assetLocator, boolean isChained) {
        By controlLocator = (assetLocator == null) ? buildControlLocator(assetName, controlClass, hasParent) : assetLocator;
        Class<?> [] paramTypes;
        Object [] initArgs;
        if (metaClass == null) {	//	simple control or assetlist without metadata
            if (controlLocator == null) {
                throw new JstrException("Locator is not defined for control of type " + controlClass);
            } else {
                if (hasParent) {	//	control with parent
                    if (isChained) {
                        paramTypes = new Class<?> [] {By.class, Waiter.class};
                        initArgs = new Object [] {new ByChained(this.getLocator(), controlLocator), waiter};
                    } else {
                        paramTypes = new Class<?> [] {BaseElement.class, By.class, Waiter.class};
                        initArgs = new Object [] {this, controlLocator, waiter};
                    }

                } else {	//	standalone control
                    paramTypes = new Class<?> [] {By.class, Waiter.class};
                    initArgs = new Object [] {controlLocator, waiter};
                }
            }
        } else if (AbstractContainer.class.isAssignableFrom(controlClass)) {
            if (controlLocator == null) {
                if (hasParent) {	//	logical container (locator is the same as parent)
                    paramTypes = new Class<?> [] {By.class, Class.class};
                    initArgs = new Object [] {this.getLocator(), metaClass};
                } else {	//	independent physical container (defines its own locator)
                    paramTypes = new Class<?> [] {Class.class};
                    initArgs = new Object [] {metaClass};
                }
            } else {
                if (hasParent) {	//	physical container with parent and registered locator
                    if (isChained) {
                        paramTypes = new Class<?> [] {By.class, Class.class};
                        initArgs = new Object [] {new ByChained(this.getLocator(), controlLocator), metaClass};
                    } else {
                        paramTypes = new Class<?> [] {BaseElement.class, By.class, Class.class};
                        initArgs = new Object [] {this, controlLocator, metaClass};
                    }
                } else {	//	physical container with registered locator
                    paramTypes = new Class<?> [] {By.class, Class.class};
                    initArgs = new Object [] {controlLocator, metaClass};
                }
            }
        } else {
            throw new IllegalArgumentException("Metadata can only be specified for " + AbstractContainer.class.getSimpleName() + " subclasses!");
        }

        Constructor<? extends BaseElement<?, ?>> constructor;
        BaseElement<?, ?> element;
        try {
            constructor = controlClass.getConstructor(paramTypes);
            element = constructor.newInstance(initArgs);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new JstrException(String.format("Can't register asset with type '%s'", controlClass), e);
        }
        element.setName(assetName);
        applyAssetConfiguration(assetName, element);
        addAsset(element);
    }

    /**
     * Manually register simple control under this asset list
     * @param assetName asset name
     * @param controlClass asset control type
     * @param waiter asset control waiter
     */
    public void registerAsset(String assetName, Class<? extends BaseElement<?, ?>> controlClass, Waiter waiter) {
        registerAsset(assetName, controlClass, waiter, null, true, null, false);
    }

    /**
     * Manually register container (nested asset list) under this asset list
     * @param assetName asset name
     * @param controlClass asset control type
     * @param metaClass container MetaData class
     */
    public void registerAsset(String assetName, Class<? extends AbstractContainer<?, ?>> controlClass, Class<? extends MetaData> metaClass) {
        registerAsset(assetName, controlClass, Waiters.NONE, metaClass, true, null, false);
    }

    /**
     * Add already existing control to the asset list. Used for custom controls processing.
     * @param control control to be added
     */
    public void addAsset(BaseElement<?, ?> control) {
        String assetName = control.getName();
        if (assetName == null) {
            throw new IllegalArgumentException("Cannot add asset without a name!");
        } else {
            control.setContainer(this);
            assetCollection.put(assetName, control);
        }
    }

    protected By buildControlLocator(String assetName, final Class<? extends BaseElement<?, ?>> controlClass, boolean hasParent) {
        if (assetControls.containsKey(controlClass)) {
            return ByT.xpath((hasParent ? "." : "") + assetLocator + assetControls.get(controlClass)).format(Quotes.escape(assetName));
        } else {
            return null;
        }
    }

    /**
     * Extract control for asset with specified name
     * @param assetName name of the asset
     * @return control for specified asset
     */
    public BaseElement<?, ?> getAsset(String assetName) {
        BaseElement<?, ?> el = getAssetCollection().get(assetName);
        if (el == null) {
            throw new JstrException(String.format("Control for '%s' asset isn't present", assetName));
        } else {
            return el;
        }
    }

    /**
     * Extract control for asset with specified name and type
     * @param <C> asset type
     * @param assetName name of the asset
     * @param controlClass expected control class
     * @return control for specified asset
     */
    public <C extends BaseElement<?, ?>> C getAsset(String assetName, Class<C> controlClass) {
        return controlClass.cast(getAsset(assetName));
    }

    /**
     * Extract control for asset with specified asset descriptor
     * @param <C> asset type
     * @param assetDesc descriptor of the asset
     * @return control for specified asset
     */
    public <C extends BaseElement<?, ?>> C getAsset(AssetDescriptor<C> assetDesc) {
        return assetDesc.getControlClass().cast(getAsset(assetDesc.getLabel()));
    }

    /**
     * Get list of assets with specified names
     * @param assetNames asset names
     * @return list of assets
     */
    public List<BaseElement<?, ?>> getAssets(String... assetNames) {
        return Stream.of(assetNames).map(this::getAsset).collect(Collectors.toList());
    }

    /**
     * Get list of assets with specified names
     * @param assetNames asset names
     * @return list of assets
     */
    public List<BaseElement<?, ?>> getAssets(List<String> assetNames) {
        return assetNames.stream().map(this::getAsset).collect(Collectors.toList());
    }

    /**
     * Get list of assets with specified descriptors
     * @param assetDescs asset descriptors
     * @return list of assets
     */
    public List<BaseElement<?, ?>> getAssets(AssetDescriptor<?>... assetDescs) {
        return Stream.of(assetDescs).map(ad -> getAsset(ad)).collect(Collectors.toList());
    }

    /**
     * Extract warning message for asset with specified name
     * @param assetName name of the asset
     * @return control for specified asset
     */
    public StaticElement getWarning(String assetName) {
        BaseElement<?, ?> el = getAsset(assetName);
        return new StaticElement(el, By.xpath(warningLocator));
    }

    /**
     * Extract warning message for asset with specified asset descriptor
     * @param assetDesc descriptor of the asset
     * @return control for specified asset
     */
    public StaticElement getWarning(AssetDescriptor<?> assetDesc) {
        return getWarning(assetDesc.getLabel());
    }

    /**
     * Check whether asset is required (mandatory)
     * @param assetName asset name
     * @return true if asset is required
     */
    public boolean isAssetRequired(String assetName) {
        BaseElement<?, ?> asset = getAsset(assetName);
        if (asset.isPresent()) {
            if (asset instanceof AbstractContainer) {
                return ((AbstractContainer<?, ?>) asset).isRequired();
            } else {
                return !asset.getWebElement().findElements(By.xpath(requiredLabelLocator)).isEmpty();
            }
        } else {
            return false;
        }
    }

    /**
     * Check whether asset is required (mandatory)
     * @param assetDesc asset descriptor
     * @return true if asset is required
     */
    public boolean isAssetRequired(AssetDescriptor<?> assetDesc) {
        return isAssetRequired(assetDesc.getLabel());
    }

    /**
     * Check whether this container is required (i.e. has required assets)
     * @return true if this container is required
     */
    public boolean isRequired() {
        return getAssetCollection().keySet().stream().anyMatch(this::isAssetRequired);
    }

    /**
     * Return list of registered asset names
     * @return asset names list
     */
    public Set<String> getAssetNames() {
        return new LinkedHashSet<>(getAssetCollection().keySet());
    }

    /**
     * Extracts values from asset controls
     * @param assetNames asset names list
     * @return asset controls values
     */
    protected TestData getAssetValues(Collection<String> assetNames) {
        Map<String, Object> rawData = new LinkedHashMap<>();
        for (String key : assetNames) {
            BaseElement<?, ?> be = getAsset(key);
            if (be.isPresent()) {
                rawData.put(key, getAsset(key).getValue());
            }
        }
        return new SimpleDataProvider(rawData);
    }

    @Override
    public boolean isEnabled() {
        return getAssetCollection().values().stream().anyMatch(e -> e.isPresent() && e.isEnabled());
    }

    @Override
    protected void ensureInteractive() {
        //	NOOP
    }

    /**
     * Get list of all required (mandatory) assets
     * @return list of required assets
     */
    public List<String> getRequiredAssetNames() {
        return getAssetCollection()
                .entrySet()
                .stream()
                .filter(e -> isAssetRequired(e.getKey()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    protected String buildSetValueErrorMessage(final I value, final Exception exception) {
        String superMessage = super.buildSetValueErrorMessage(value, exception);
        if (exception != null) {
            Throwable current = exception;
            Throwable firstIstfException = null;

            do {
                if (current instanceof JstrException) {
                    firstIstfException = current;
                }
            } while ((current = current.getCause()) != null);

            if (firstIstfException != null) {
                superMessage = superMessage + "\nFramework root cause: " + firstIstfException.getMessage();
            }
        }
        return superMessage;
    }

    /**
     * Programmatically override instance locator templates (without applying configuration)
     * @param assetLocator - asset locator (if null, skipped)
     * @param warningLocator - warning locator (if null, skipped)
     * @param requiredLabelLocator - required label locator (if null, skipped)
     */
    public void overrideLocators(String assetLocator, String warningLocator, String requiredLabelLocator) {
        if (assetLocator != null) {
            this.assetLocator = assetLocator;
        }
        if (warningLocator != null) {
            this.warningLocator = warningLocator;
        }
        if (requiredLabelLocator != null) {
            this.requiredLabelLocator = requiredLabelLocator;
        }
    }

    @Override
    protected void ensureVisible() {
        //	NOOP - no need to ensure visibility for containers (because they will not be interacted with directly anyway)
    }
}
