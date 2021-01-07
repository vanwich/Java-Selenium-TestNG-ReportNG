package help.data;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import help.config.ClassConfigurator;

public abstract class TestData {
    protected static final Logger LOG = LoggerFactory.getLogger(TestData.class);

    public enum Type implements ITestDataType {
        STRING {
            @Override public boolean isList() { return false; }
            @Override public boolean hasString() { return true; }
        },
        TESTDATA {
            @Override public boolean isList() { return false; }
            @Override public boolean hasString() { return false; }
        },
        LIST_STRING {
            @Override public boolean isList() { return true; }
            @Override public boolean hasString() { return true; }
        },
        LIST_TESTDATA {
            @Override public boolean isList() { return true; }
            @Override public boolean hasString() { return false; }
        };
    };

    protected Map<String, Object> internalData;
    private Map<String, Object> adjustments;

    protected String groupPath;
    protected String collectionName;

    public static final String LINK_TOKEN = "@";
    public static final Pattern LINKED_DATA = Pattern.compile(String.format("^(?:%1$s(<?\\w+>?))??(?:%1$s(\\w+))??%1$s(\\w+)?$", Pattern.quote(LINK_TOKEN)));
    private static final Pattern LINK_ALIAS = Pattern.compile("<(.*)>");
    private static final String PATH_SEPARATOR = "|";
    private static final Pattern INDEXED_KEY = Pattern.compile("(.*)\\[(\\d+)\\]");

    @ClassConfigurator.Configurable
    private static Map<String, String> testDataAliases = new HashMap<String, String>();

    @ClassConfigurator.Configurable(byClassName = true) private static MarkupParser parser = new DefaultMarkupParser();
    @ClassConfigurator.Configurable(byClassName = true) private static KeyModifier keyModifier = new KeyModifier() {
        @Override
        public String modify(String key) { return key; }
    };

    static {
        ClassConfigurator configurator = new ClassConfigurator(TestData.class);
        configurator.applyConfiguration();
    }

    protected class ValueContainer {
        private Object value = null;

        ValueContainer(Object value) {
            this.value = value;
        }

        Object getValue() {
            return value;
        }

        boolean exists() {
            return value != null;
        }

        <T> T extract(final Class<T> klass) {
            Object ret;

            if (value == null) {
                ret = null;
            } else if (klass.isInstance(value)) {
                ret = value;
            } else if ((klass == TestData.class) && (value instanceof Map)) {
                ret = wrapMap(value);
            } else if ((klass == String.class) && (
                    //	primitive types that have literal representation
                    Boolean.TYPE.isInstance(value) || Boolean.class.isInstance(value) ||
                            Integer.TYPE.isInstance(value) || Integer.class.isInstance(value) ||
                            Long.TYPE.isInstance(value) || Long.class.isInstance(value) ||
                            Float.TYPE.isInstance(value) || Float.class.isInstance(value) ||
                            Double.TYPE.isInstance(value) || Double.class.isInstance(value) ||
                            Character.TYPE.isInstance(value) || Character.class.isInstance(value)
            )) {
                ret = value.toString();
            } else {
                throw new TestDataException("Value " + value + " is not of type " + klass.getName());
            }
            return klass.cast(ret);
        }

        @SuppressWarnings("unchecked")
        <T> List<T> extractList(final Class<T> klass) {
            Object ret;

            if (value == null) {
                ret = new ArrayList<T>();
            } else if (value instanceof List) {
                List<Object> olist = (List<Object>) value;
                Class<?> listClass = TestData.getListClass(olist);
                if (listClass == null) {
                    listClass = klass;
                }
                if (klass.isAssignableFrom(listClass)) {
                    ret = value;
                } else if ((klass == TestData.class) && (Map.class.isAssignableFrom(listClass))) {
                    List<TestData> tdlist = new ArrayList<TestData>(olist.size());
                    for (Object o : olist) {
                        tdlist.add((o == null) ? (TestData) o : wrapMap(o));
                    }
                    ret = tdlist;
                } else {
                    throw new TestDataException("List " + value + " does not contain items of type " + klass.getName());
                }
            } else if (klass.isAssignableFrom(value.getClass())) {
                ret = Arrays.asList(value);
            } else if ((klass == TestData.class) && (value instanceof Map)) {
                ret = Arrays.asList(wrapMap(value));
            } else {
                throw new TestDataException("Value " + value + " is not of type List<" + klass.getName() + ">");
            }
            return (List<T>) ret;
        }

        @SuppressWarnings("unchecked")
        private TestData wrapMap(Object o) {
            TestData gdp = spawn();
            gdp.internalData = (Map<String, Object>) o;
            return gdp;
        }

        boolean isTestData() {
            return (value instanceof TestData) || (value instanceof Map);
        }

        boolean isTestDataList() {
            if (value instanceof List) {
                Class<?> listClass = TestData.getListClass((List<?>) value);
                return ((listClass != null) && ((TestData.class.isAssignableFrom(listClass)) || (Map.class.isAssignableFrom(listClass))));
            } else {
                return false;
            }
        }

        /**
         * Plain values are: String, List<String>, null
         */
        boolean isPlainValue() {
            return !(isTestData() || isTestDataList());
        }
    }

    class ModMap {
        private Map<String, Object> map;

        ModMap(Map<String, Object> m) {
            map = m;
        }

        boolean containsKey(String key) {
            if (map == null) {
                return false;
            } else {
                return map.containsKey(key) || map.containsKey(keyModifier.modify(key));
            }
        }

        Object get(String key) {
            if (map == null) {
                return null;
            } else {
                String modKey = keyModifier.modify(key);
                if (map.containsKey(modKey)) {
                    return map.get(modKey);
                } else {
                    return map.get(key);
                }
            }
        }
    }
    protected TestData() {
        this(null, null);
    }

    protected TestData(String groupName, String collectionName) {
        this.groupPath = groupName;
        this.collectionName = collectionName;
        this.internalData = new LinkedHashMap<String, Object>();
        this.adjustments = new LinkedHashMap<String, Object>();
    }

    protected TestData spawn(String groupPath, String collectionName) {
        try {
            Constructor<? extends TestData> ctor = getClass().getDeclaredConstructor(String.class, String.class);
            return ctor.newInstance(groupPath, collectionName);
        } catch (Exception e) {
            throw new TestDataException("Error upon test data instantiation for group " + groupPath + " and collection " + collectionName, e);
        }
    }

    protected abstract TestData spawn();

    private TestData copy() {
        TestData td = spawn();
        td.internalData = internalData;
        td.adjustments = new LinkedHashMap<String, Object>(adjustments);
        return td;
    }

    @SuppressWarnings("unchecked")
    protected static Object stringify(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Map) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            for (Map.Entry<String, ?> entry : ((Map<String, ?>) value).entrySet()) {
                map.put(entry.getKey(), stringify(entry.getValue()));
            }
            return map;
        } else if (value instanceof TestData) {
            TestData td = (TestData) value;
            td.internalData = (Map<String, Object>) stringify(td.internalData);
            return td;
        } else if (value instanceof List) {
            List<Object> list = new ArrayList<Object>();
            for (Object o : (List<?>) value) {
                list.add(stringify(o));
            }
            return list;
        } else if (value.getClass().isArray()) {
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < Array.getLength(value); i++) {
                list.add(stringify(Array.get(value, i)));
            }
            return list;
        } else {
            return value.toString();
        }
    }

    private <T> T safeGet(final Class<T> klass, String... keys) {
        try {
            return getByPath(0, keys).extract(klass);
        } catch (TestDataException e) {
            throw new TestDataException("Error retrieving value by keys " + Arrays.toString(keys), e);
        }
    }

    private <T> List<T> safeGetList(final Class<T> klass, String... keys) {
        try {
            return getByPath(0, keys).extractList(klass);
        } catch (TestDataException e) {
            throw new TestDataException("Error retrieving value by keys " + Arrays.toString(keys), e);
        }
    }

    private Object getByKey(ModMap currData, ModMap currAdjustments, String key) {
        ModMap map = (currAdjustments.containsKey(key)) ? currAdjustments : currData;
        return map.get(key);
    }

    protected ValueContainer getByPath(int startIndex, String... keys) {
        return getByPath(internalData, adjustments, startIndex, keys);
    }

    private ValueContainer getByPath(Map<String, Object> currData, Map<String, Object> currAdjustments, int startIndex, String [] keys) {
        int len = keys.length;
        if (startIndex >= len) {
            throw new IllegalArgumentException("At least one key should be specified!");
        } else {
            //	get value from adjustments or from the original data
            Object value = getByKey(new ModMap(currData), new ModMap(currAdjustments), keys[startIndex]);

            //	resolve links
            if (value instanceof String) {
                value = resolveLinkedData((String) value);
            } else if (value instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> xs = new ArrayList<Object>((List<Object>) value);
                for (int i = 0; i < xs.size(); i++) {
                    Object obj = xs.get(i);
                    if (obj instanceof String) {
                        obj = resolveLinkedData((String) obj);
                        xs.set(i, obj);
                    }
                }
                getListClass(xs);
                value = xs;
            }

            //	return resolved and adjusted value
            if (startIndex == len - 1) {
                return new ValueContainer(value);
            } else {
                if (value instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> mapValue = (Map<String, Object>) value;
                    return getByPath(mapValue, null, startIndex + 1, keys);
                } else if (value instanceof TestData) {
                    return ((TestData) value).getByPath(startIndex + 1, keys);
                } else {
                    throw new TestDataException("Value by key " + keys[startIndex] + " is not a TestData");
                }
            }
        }
    }

    private Object resolveLinkedData(String origValue) {
        Object retValue;
        Matcher linkMatcher = TestData.LINKED_DATA.matcher(origValue);
        if (linkMatcher.matches()) {
            String groupPart = linkMatcher.group(1);
            String collectionPart = linkMatcher.group(2);
            String key = linkMatcher.group(3);
            File remoteGroup;
            if (key == null) {
                retValue = spawn();
            } else {
                if (groupPart == null) {
                    remoteGroup = new File(groupPath);
                } else {
                    Matcher aliasMatcher = TestData.LINK_ALIAS.matcher(groupPart);
                    if (aliasMatcher.matches()) {
                        String alias = aliasMatcher.group(1);
                        if (testDataAliases.containsKey(alias)) {
                            remoteGroup = new File(new File(DataProviderFactory.rootPath), testDataAliases.get(alias));
                        } else {
                            throw new TestDataException("Test data alias " + alias + " is not defined. Check " + TestData.class.getName() + ".configuration file");
                        }
                    } else {
                        remoteGroup = new File(new File(groupPath).getParent(), groupPart);
                    }
                }

                String remoteCollection = (collectionPart == null) ? collectionName : collectionPart;
                retValue = spawn(remoteGroup.getPath(), remoteCollection).getTestData(false, key);
            }
        } else {
            retValue = origValue;
        }
        return retValue;
    }

    private static Class<?> getListClass(List<?> list) {
        Class<?> itemClass = list.stream().filter(i -> i != null).findFirst().map(seed -> {
            Class<?> klazz = seed.getClass();
            for (int i = 0; i < list.size(); i++) {
                Object curr = list.get(i);
                if (curr != null && curr != seed) {
                    Class<?> currClass = curr.getClass();
                    while (!klazz.isAssignableFrom(currClass)) {
                        klazz = klazz.getSuperclass();
                    }
                }
            }
            return klazz;
        }).orElse(null);

        if (itemClass == Object.class) {
            throw new TestDataException("Failed to find common superclass for items of " + list);
        } else {
            return itemClass;
        }
    }

    public Object getValue(Type valueType, String... keys) {
        switch (valueType) {
            case STRING:
                return getValue(keys);
            case TESTDATA:
                return getTestData(keys);
            case LIST_STRING:
                return getList(keys);
            case LIST_TESTDATA:
                return getTestDataList(keys);
            default:
                throw new IllegalArgumentException("unknown value type " + valueType);
        }
    }

    /**
     * Get string value by specified keys
     * @param keys - sequence of keys pointing to the desired value
     * @return string value
     */
    public String getValue(String... keys) {
        String value = safeGet(String.class, keys);
        if (value != null) {
            String markupValue = parser.parse(value);
            if (!markupValue.equals(value)) {
                value = markupValue;
                adjust(makeKeyPath(keys), value);
            }
        }
        return value;
    }

    /**
     * Get list by specified keys
     * @param keys - sequence of keys pointing to the desired value
     * @return list of strings
     */
    public List<String> getList(String... keys) {
        List<String> list = safeGetList(String.class, keys);
        if (list != null) {
            boolean needAdjustment = false;
            for (int i = 0; i < list.size(); i++) {
                String markupValue = parser.parse(list.get(i));
                if (!markupValue.equals(list.get(i))) {
                    list.set(i, markupValue);
                    needAdjustment = true;
                }
            }
            if (needAdjustment) {
                adjust(makeKeyPath(keys), list);
            }
        }
        return list;
    }

    /**
     * Get nested/linked test data collection value by specified keys
     * @param keys - sequence of keys pointing to the desired value
     * @return nested/linked TestData object
     */
    public TestData getTestData(String... keys) {
        return getTestData(true, keys);
    }

    private TestData getTestData(boolean forceNewObj, String... keys) {
        TestData td = safeGet(TestData.class, keys);
        return (forceNewObj && td != null) ? td.copy() : td;
    }

    /**
     * Get list of nested/linked test data collections by specified keys
     * @param keys - sequence of keys pointing to the desired value
     * @return list of TestData
     */
    public List<TestData> getTestDataList(String... keys) {
        return safeGetList(TestData.class, keys);
    }

    /**
     * Get set of available keys (with respect to applied adjustments)
     * @return set of keys
     */
    public Set<String> getKeys() {
        Set<String> keys = new LinkedHashSet<String>(internalData.keySet());
        Set<String> remove = new LinkedHashSet<String>();
        keys.addAll(adjustments.keySet());
        ModMap currData = new ModMap(internalData);
        ModMap currAdjs = new ModMap(adjustments);

        for (String key : keys) {
            if (getByKey(currData, currAdjs, key) == null) {
                remove.add(key);
            }
        }

        keys.removeAll(remove);
        return keys;
    }

    /**
     * Check whether specified key is present (with respect to applied adjustments)
     * @param key - key to check
     * @return - true if the value by key exists and is not null, false otherwise
     */
    public boolean containsKey(String key) {
        return getKeys().contains(key);
    }

    @Override
    public String toString() {
        List<String> items = new ArrayList<String>();
        Set<String> keys = new LinkedHashSet<String>(internalData.keySet());
        keys.addAll(adjustments.keySet());
        for (String key : keys) {
            String value;
            if (adjustments.containsKey(key)) {
                value = "~=" + adjustments.get(key);
            } else {
                value = "=" + internalData.get(key);
            }
            items.add(key + value);
        }
        return "{" + StringUtils.join(items, ", ") + "}";
    }

    /**
     * Build key path for deep adjustments and adjustment removal
     * @param keys - sequence of keys
     * @return combined path
     */
    public static String makeKeyPath(String ... keys) {
        return StringUtils.join(keys, PATH_SEPARATOR);
    }

    /**
     * Apply single key-String value adjustment.
     * Notes:
     * <ul>
     * <li>if key already exists and value is null, this key is "masked" (i.e. appears to be removed from the data collection)</li>
     * <li>if value is not null, new value will be returned by this key from now on (key does not have to exist in the initial data)</li>
     * </ul>
     * @param keyPath - key or key path to adjust. In case of "deep" adjustments keyPath should contain path to adjusted value separated by {@link #PATH_SEPARATOR}
     * @param value - new value.
     * @return this TestData object
     */
    public TestData adjust(String keyPath, String value) {
        return createAdjustment(parseKeyPath(keyPath), 0, value);
    }

    /**
     * Apply single key-TestData value adjustment. See {@link #adjust(String, String)} for more details.
     * @param keyPath - key or key path to adjust. In case of "deep" adjustments keyPath should contain path to adjusted value separated by {@link #PATH_SEPARATOR}
     * @param nestedData - new value
     * @return this TestData object
     */
    public TestData adjust(String keyPath, TestData nestedData) {
        return createAdjustment(parseKeyPath(keyPath), 0, nestedData);
    }

    /**
     * Apply single key-List value adjustment. See {@link #adjust(String, String)} for more details.
     * @param keyPath - key or key path to adjust. In case of "deep" adjustments keyPath should contain path to adjusted value separated by {@link #PATH_SEPARATOR}
     * @param list - new list value (list of String or TestData)
     * @return this TestData object
     */
    public TestData adjust(String keyPath, List<?> list) {
        return createAdjustment(parseKeyPath(keyPath), 0, list);
    }

    /**
     * Overlay bulk adjustment on top of initial data.
     * Notes:
     * <ul>
     * <li>if a key exists only in initial data but not in adjustment, initial value will be used</li>
     * <li>if a key exists in adjustment, adjusted value will be used, irrespective of whether this key was present in the initial data</li>
     * <li>if adjusted value by some key is null, this key is "masked" (i.e. appears to be removed from the data collection)</li>
     * </ul>
     * @param adjustment - TestData object for the overlay
     * @return this TestData object
     */
    public TestData adjust(TestData adjustment) {
        for (Map.Entry<String, Object> e : adjustment.getInternalData().entrySet()) {
            putAdjustment(e.getKey(), e.getValue());
        }
        return this;
    }

    /**
     * Hide (via "masking" adjustment) all specified keys.
     * @param keys - keys to mask
     * @return this TestData object
     */
    public TestData mask(String... keys) {
        for (String key : keys) {
            createAdjustment(parseKeyPath(key), 0, null);
        }
        return this;
    }

    /**
     * Hide (via "masking" adjustment) all keys except those specified in the parameters.
     * @param keys - keys to retain
     * @return this TestData object
     */
    public TestData ksam(String... keys) {
        List<String> keysToRetain = Arrays.asList(keys);
        for (String key : getKeys()) {
            if (!keysToRetain.contains(key)) {
                createAdjustment(parseKeyPath(key), 0, null);
            }
        }
        return this;
    }

    /**
     * Remove adjustment for specified key
     * @param keyPath - key or key path to remove adjustment from. In case of "deep" adjustment removal keyPath should contain path to adjusted value separated by {@link #PATH_SEPARATOR}
     * @return this TestData object
     */
    public TestData removeAdjustment(String keyPath) {
        return removeAdjustment(parseKeyPath(keyPath));
    }

    /**
     * Remove all applied adjustments
     * @return this TestData object
     */
    public TestData purgeAdjustments() {
        adjustments.clear();
        return this;
    }

    protected Map<String, Object> getInternalData() {
        return internalData;
    }

    /**
     * Dump TestData as a test suite
     * @param dw - desired datawriter
     */
    public void dump(DataWriter dw) {
        dw.write(buildDumpableMap());
    }

    /**
     * Dump TestData as a test group
     * @param groupName - desired test data group name
     * @param dw - desired datawriter
     */
    public void dump(String groupName, DataWriter dw) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put(groupName, buildDumpableMap());
        dw.write(data);
    }

    private Map<String, Object> buildDumpableMap() {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        Map<String, Object> merged = new LinkedHashMap<String, Object>();
        merged.putAll(internalData);
        for (String key : adjustments.keySet()) {
            merged.put(key, adjustments.get(key));
        }

        for (Map.Entry<String, Object> entry : merged.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                data.put(key, value);
            } else if (value instanceof TestData) {
                data.put(key, ((TestData) value).buildDumpableMap());
            } else {
                data.put(key, value);
            }
        }
        return data;
    }

    private static String [] parseKeyPath(String keyPath) {
        String [] parsedPath = keyPath.split(Pattern.quote(PATH_SEPARATOR));
        if (parseKey(parsedPath[parsedPath.length - 1]).getRight() < 0) {
            return parsedPath;
        } else {
            throw new IllegalArgumentException("Last part of the key path '" + keyPath + "' cannot refer to an indexed list element.");
        }
    }

    private static Pair<String, Integer> parseKey(String key) {
        Matcher matcher = INDEXED_KEY.matcher(key);
        if (matcher.matches()) {
            Integer index;
            try {
                index = Integer.parseInt(matcher.group(2));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot parse index from the key '" + key + "'.", e);
            }
            return Pair.of(matcher.group(1), index);
        } else {
            return Pair.of(key, -1);
        }
    }

    private TestData createAdjustment(String [] keyPath, int startIndex, Object value) {
        int len = keyPath.length;
        if (startIndex >= len) {
            throw new IllegalArgumentException("Keys for adjustment must be specified");
        } else if (startIndex == len - 1) {
            return putAdjustment(keyPath[startIndex], value);
        } else {
            Pair<String, Integer> keyAndIndex = parseKey(keyPath[startIndex]);
            if (keyAndIndex.getRight() >= 0) {
                List<TestData> tdList;
                try {
                    tdList = getTestDataList(keyAndIndex.getLeft());
                } catch (TestDataException e) {
                    throw new IllegalArgumentException("Failed to adjust value by path [" + String.join(",", keyPath) +
                            "]. Value by key '" + keyPath[startIndex] + "' is not a list of TestData.");
                }
                if (keyAndIndex.getRight() >= 0 && keyAndIndex.getRight() < tdList.size()) {
                    tdList.get(keyAndIndex.getRight()).createAdjustment(keyPath, startIndex + 1, value);
                } else {
                    throw new IllegalArgumentException("Failed to adjust value by path [" + String.join(",", keyPath) +
                            "]. Element by key '" + keyPath[startIndex] + "' does not exist.");
                }
                return putAdjustment(keyAndIndex.getLeft(), tdList);
            } else {
                TestData tmp;
                try {
                    tmp = getTestData(false, keyPath[startIndex]);
                } catch (TestDataException e) {
                    throw new IllegalArgumentException("Failed to adjust value by path [" + String.join(",", keyPath) +
                            "]. Value by key '" + keyPath[startIndex] + "' is not a TestData.");
                }
                if (tmp == null) {
                    throw new IllegalArgumentException("Failed to adjust value by path [" + String.join(",", keyPath) +
                            "]. Value by key '" + keyPath[startIndex] + "' does not exist.");
                } else {
                    return putAdjustment(keyPath[startIndex], tmp.createAdjustment(keyPath, startIndex + 1, value));
                }
            }
        }
    }

    protected TestData putAdjustment(String key, Object value) {
        if (value == null || value instanceof String || value instanceof TestData || value instanceof Map) {
            adjustments.put(key, value);
        } else if (value instanceof List) {
            Class<?> listClass = getListClass((List<?>) value);
            if (listClass == null || listClass == String.class || TestData.class.isAssignableFrom(listClass)) {
                adjustments.put(key, value);
            }
        } else {
            throw new IllegalArgumentException("Only strings, TestData and lists of them is allowed!");
        }
        return this;
    }

    private TestData removeAdjustment(String [] keyPath) {
        TestData currData = this;
        for (int i = 0; i < keyPath.length - 1; i++) {
            currData = currData.getTestData(false, keyPath[i]);
        }
        currData.adjustments.remove(keyPath[keyPath.length - 1]);
        return this;
    }

    /**
     * Transform test data by turning all links into nested test data objects. Original data remains intact.
     * Note: This operation is costly, avoid if possible.
     * @return transformed test data
     * @throws TestDataException on detection of a cyclic link
     */
    public TestData resolveLinks() {
        return resolveLinks(new Stack<Integer>());
    }

    private TestData resolveLinks(Stack<Integer> processedData) {
        Integer hashCode = internalHashCode();
        if (processedData.search(hashCode) >= 1) {
            throw new TestDataException("Cannot resolve links in the test data: cyclic link detected");
        } else {
            processedData.push(hashCode);
        }

        TestData resolved = spawn();
        for (String key : getKeys()) {
            ValueContainer vc = getByPath(0, key);
            if (vc.isPlainValue()) {
                resolved.internalData.put(key, vc.getValue());
            } else if (vc.isTestData()) {
                resolved.internalData.put(key, vc.extract(TestData.class).resolveLinks(processedData));
            } else if (vc.isTestDataList()) {
                List<TestData> ls = new ArrayList<TestData>();
                for (TestData td : vc.extractList(TestData.class)) {
                    if (td == null) {
                        ls.add(td);
                    } else {
                        ls.add(td.resolveLinks(processedData));
                    }
                }
                resolved.internalData.put(key, ls);
            } else {
                throw new TestDataException("Unexpected condition: value " + vc.getValue() + " of type " + vc.getValue().getClass());
            }
        }
        processedData.pop();
        return resolved;
    }

    /**
     * Internal implementation of hash code, used only to detect cyclic links.
     * Not to be used as proper Object.hashCode()!
     */
    private Integer internalHashCode() {
        return Objects.hash(groupPath, collectionName, internalData, adjustments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resolveLinks().buildDumpableMap());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof TestData)) {
            return false;
        } else if (this == other) {
            return true;
        } else {
            Map<String, Object> thisData = resolveLinks().buildDumpableMap();
            Map<String, Object> otherData = ((TestData) other).resolveLinks().buildDumpableMap();
            return thisData.equals(otherData);
        }
    }

    /**
     * Return difference between this and other TestData objects. Key order is ignored.
     * @param other other TestData object
     * @return internal map difference
     */
    public MapDifference<String, Object> diff(TestData other) {
        if (other == null) {
            throw new IllegalArgumentException("Other TestData cannot be null!");
        } else {
            Map<String, Object> thisData = resolveLinks().buildDumpableMap();
            Map<String, Object> otherData = ((TestData) other).resolveLinks().buildDumpableMap();
            return Maps.difference(thisData, otherData);
        }
    }

    /**
     * Get type of value by specified key(s)
     * @param keys - sequence of keys pointing to the desired value
     * @return value type
     */
    public Type getValueType(String... keys) {
        ValueContainer vc = getByPath(0, keys);
        if (vc.isTestData()) return Type.TESTDATA;
        else if (vc.isTestDataList()) return Type.LIST_TESTDATA;
        else if (vc.getValue() instanceof List) return Type.LIST_STRING;
        else return Type.STRING;
    }
}
