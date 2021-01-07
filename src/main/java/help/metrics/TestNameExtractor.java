package help.metrics;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestResult;

public class TestNameExtractor {
    private TestNameExtractor() {}

    public static String getTestName(ITestResult testResult) {
        return new StringBuilder()
                .append(testResult.getTestClass().getName())
                .append(".")
                .append(testResult.getMethod().getMethodName())
                .toString();
    }

    private static String objectToString(Object obj) {
        if (obj == null) {
            return "<null>";
        } else if (obj.getClass().isArray()) {
            return Arrays.toString((Object []) obj);
        } else if (obj instanceof ITestResult) {
            return ITestResult.class.getSimpleName();
        } else {
            return obj.toString();
        }
    }

    public static String getTestParameters(ITestResult testResult, boolean escapeParams) {
        Object [] params = testResult.getParameters();
        return params.length > 0
                ? (escapeParams ? String.format("<%s parameters>", params.length) : Stream.of(params).map(TestNameExtractor::objectToString).collect(Collectors.joining(", ")))
                : null;
    }

    public static String getTestParameters(ITestResult testResult) {
        return getTestParameters(testResult, false);
    }

    public static String getDecoratedTestParameters(ITestResult testResult, boolean escapeParams) {
        String str = getTestParameters(testResult, escapeParams);
        str = (str == null) ? "" : "(" + str + ")";
        return StringUtils.abbreviate(str, 100);
    }

    public static String getDecoratedTestParameters(ITestResult testResult) {
        return getDecoratedTestParameters(testResult, false);
    }

    public static String getFullTestName(ITestResult testResult, boolean escapeParams) {
        return getTestName(testResult) + getDecoratedTestParameters(testResult, escapeParams);
    }

    public static String getFullTestName(ITestResult testResult) {
        return getFullTestName(testResult, false);
    }
}
