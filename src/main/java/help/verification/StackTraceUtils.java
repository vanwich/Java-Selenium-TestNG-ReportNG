package help.verification;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class StackTraceUtils {
    private static final Logger log = LoggerFactory.getLogger(StackTraceUtils.class);
    private static final Set<Class<? extends Annotation>> testNGMethodAnnotations = new HashSet<>(Arrays.asList(
            Test.class,
            AfterClass.class,
            AfterGroups.class,
            AfterMethod.class,
            AfterSuite.class,
            AfterTest.class,
            BeforeClass.class,
            BeforeGroups.class,
            BeforeMethod.class,
            BeforeSuite.class,
            BeforeTest.class
    ));

    private static final String [] skipStackTraceElements = new String [] {
            "sun.reflect", "org.testng","java.lang", "org.apache.maven", "java.util"
    };

    private StackTraceUtils() {}

    /**
     * Filter stack trace by removing unnecessary elements
     * @param stackTraceElements original stack trace
     * @return filtered stack trace
     */
    public static StackTraceElement [] filterStackTrace(StackTraceElement [] stackTraceElements) {
        List<StackTraceElement> stackTraceElementsList = new ArrayList<StackTraceElement>();

        for (StackTraceElement ste : stackTraceElements) {
            if (!StringUtils.startsWithAny(ste.toString(), skipStackTraceElements) && !"<generated>".equals(ste.getFileName())) {
                stackTraceElementsList.add(ste);
            }
        }

        return stackTraceElementsList.toArray(new StackTraceElement[stackTraceElementsList.size()]);
    }

    /**
     * Filter stack trace (see {@link #filterStackTrace(StackTraceElement[])} and append an extra element with custom message
     * @param stackTraceElements original stack trace
     * @param errorMessage message for the extra element
     * @return modified stack trace
     */
    public static StackTraceElement [] filterStackTrace(StackTraceElement [] stackTraceElements, String errorMessage) {
        StackTraceElement [] filteredElements = filterStackTrace(stackTraceElements);
        StackTraceElement lastFilteredElement = filteredElements[filteredElements.length - 1];

        return (StackTraceElement []) ArrayUtils.addAll(new StackTraceElement [] {
                new StackTraceElement("\n", errorMessage, lastFilteredElement.getFileName(),
                        lastFilteredElement.getLineNumber())}, filteredElements);
    }

    /**
     * Check whether stack trace element belongs to one of TestNG's test/configuration methods
     * @param ste stack trace element
     * @return true for TestNG's test/configuration methods
     */
    public static boolean isTestMethod(StackTraceElement ste) {
        try {
            if (ste.getClassName().startsWith("sun.reflect")) {
                return false;
            } else {
                Annotation[] anns = Class.forName(ste.getClassName()).getMethod(ste.getMethodName()).getAnnotations();
                return Arrays.stream(anns).anyMatch(ann -> testNGMethodAnnotations.contains(ann.annotationType()));
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;	//	probably private method or unloaded class, we are not interested
        } catch (Exception e) {
            log.error("Unexpected error during stack trace analysis", e);
            return false;
        }
    }

    /**
     * Find TestNG's test/configuration method's name in the stack trace if exists
     * @param stackTraceElements original stack trace
     * @return optional of stack trace element with method's name or empty if there are no testNG methods in the stack trace
     */
    public static java.util.Optional<StackTraceElement> findTestMethod(StackTraceElement [] stackTraceElements) {
        return Arrays.stream(stackTraceElements)
                .filter(StackTraceUtils::isTestMethod)
                .findFirst();
    }

    /**
     * Find TestNG's test/configuration method's name in the stack trace if exists
     * @param stackTraceElements original stack trace
     * @return optional of className.methodName or empty if there are no testNG methods in the stack trace
     */
    public static Optional<String> findTestMethodName(StackTraceElement [] stackTraceElements) {
        return findTestMethod(stackTraceElements)
                .map(ste -> String.format("%1$s.%2$s", ste.getClassName(), ste.getMethodName()));
    }
}
