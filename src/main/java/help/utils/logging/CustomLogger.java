package help.utils.logging;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.config.PropertyProvider;
import help.config.TestProperties;

public class CustomLogger {
    private static final ThreadLocal<String> LOG_FILE_PATH = new ThreadLocal<String>();
    private static final String LOG_DIR_NAME = "LOG";

    static {
        cleanLogDirectory();
    }

    private static void setTestMethodLogFileProperties(String testMethodLogFilePath) {
        LOG_FILE_PATH.set(testMethodLogFilePath);
    }

    public static String getCurrentLogFilePath() {
        return LOG_FILE_PATH.get();
    }

    /**
     * Returns initialized logger
     * @return logger
     */
    public static Logger getInstance(){
        String className = "";

        try {
            className = Class.forName(new Throwable().getStackTrace()[2].getClassName()).toString();
        }
        catch(Exception e){}

        return LoggerFactory.getLogger(className);
    }

    /**
     * Gets directory where log file is located
     * @return directory path
     */
    public static String getLogDirectory() {
        return new File("").getAbsolutePath() + File.separator +
                FilenameUtils.separatorsToSystem(PropertyProvider.getProperty(TestProperties.OUTPUT)) +
                File.separator + LOG_DIR_NAME;
    }

    /**
     * Sets current test method name
     * @param testMethodFullName method name in full notation
     */
    public static void setTestMethodName(String testMethodFullName) {
        String testMethodLogFilePath;
        if(testMethodFullName != null) {
            testMethodLogFilePath = getLogDirectory() + File.separator + testMethodFullName.replace(".", File.separator) + ".log";
            File testMethodLogFile = new File(testMethodLogFilePath);
            if (!testMethodLogFile.getParentFile().getAbsoluteFile().exists()) {
                testMethodLogFile.getParentFile().mkdirs();
            }
        } else {
            testMethodLogFilePath = null;
        }
        setTestMethodLogFileProperties(testMethodLogFilePath);

    }

    /**
     * Cleans up log directory from previously generated log file
     */
    public static void cleanLogDirectory() {
        try {
            FileUtils.deleteDirectory(new File(getLogDirectory()));
        } catch (IOException e) {
            // LOG.info("Can't delete log directory.", e);
        }
    }

    /**
     * @return get log file
     */
    public static String getLogFile() {
        String filePath = LOG_FILE_PATH.get();
        if(filePath == null) {
            String fileName = FilenameUtils.separatorsToSystem(PropertyProvider.getProperty(TestProperties.LOG_FILE_NAME, "log.log"));
            filePath = getLogDirectory() + File.separator + fileName;
        }
        if(!new File(filePath).getParentFile().exists()) {
            new File(filePath).getParentFile().mkdirs();
        }
        return filePath;
    }
}
