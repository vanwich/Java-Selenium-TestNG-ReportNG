package webdriver.controls;

import java.util.List;
import data.TestData;

public final class NormalizeValueHelpers {
    private NormalizeValueHelpers() {}

    public static Boolean booleanValue(Object rawValue) {
        if (rawValue instanceof String) {
            return new Boolean((String) rawValue);
        } else {
            throw new IllegalArgumentException("Value " + rawValue + " has incorrect type " + rawValue.getClass());
        }
    }

    public static String stringValue(Object rawValue) {
        if (rawValue instanceof String) {
            return (String) rawValue;
        } else {
            throw new IllegalArgumentException("Value " + rawValue + " has incorrect type " + rawValue.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> stringListValue(Object rawValue) {
        boolean isCorrect = true;
        if (rawValue instanceof List) {
            for (Object o : (List<?>) rawValue) {
                if (!(o instanceof String)) {
                    isCorrect = false;
                    break;
                }
            }
        } else {
            isCorrect = false;
        }

        if (isCorrect) {
            return (List<String>) rawValue;
        } else {
            throw new IllegalArgumentException("Value " + rawValue + " has incorrect type and/or generic type " + rawValue.getClass());
        }
    }

    public static TestData testDataValue(Object rawValue) {
        if (rawValue instanceof TestData) {
            return (TestData) rawValue;
        } else {
            throw new IllegalArgumentException("Value " + rawValue + " has incorrect type " + rawValue.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<TestData> testDataListValue(Object rawValue) {
        boolean isCorrect = true;
        if (rawValue instanceof List) {
            for (Object o : (List<?>) rawValue) {
                if (!(o instanceof TestData)) {
                    isCorrect = false;
                    break;
                }
            }
        } else {
            isCorrect = false;
        }

        if (isCorrect) {
            return (List<TestData>) rawValue;
        } else {
            throw new IllegalArgumentException("Value " + rawValue + " has incorrect type and/or generic type " + rawValue.getClass());
        }
    }
}
