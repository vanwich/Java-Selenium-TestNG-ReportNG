package help.data;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import help.data.impl.BrokenTestData;

public abstract class FileBasedDataProvider extends TestData{
    protected String filePath;

    protected FileBasedDataProvider() {
        super();
    }

    protected FileBasedDataProvider(String groupPath, boolean globalOnly) {
        super(groupPath, null);
        List<String> siblings = getChildren(groupPath);
        internalData = new LinkedHashMap<String, Object>();
        for (String siblingName : siblings) {
            try {
                TestData d = spawn(groupPath, siblingName);
                if (globalOnly) {
                    lift(d);
                } else {
                    internalData.put(siblingName, d);
                }
            } catch (Exception e) {
                if (globalOnly) {
                    throw new TestDataException("One of the collections under " + groupPath + " is broken.", e);
                } else {
                    internalData.put(siblingName, new BrokenTestData("Error loading test data from " + groupPath, e));
                }
            }
        }
    }

    protected FileBasedDataProvider(String groupPath, String collectionName) {
        super(groupPath, collectionName);
        this.filePath = makePath();

        if (TestDataCache.hasData(filePath, collectionName)) {
            internalData = TestDataCache.get(filePath, collectionName);
        } else {
            internalData = readInputStream();
            if (internalData == null) {
                throw new TestDataException("Collection by path " + filePath + " does not contain any data");
            }
            TestDataCache.put(filePath, collectionName, internalData);
        }
    }

    protected abstract String getExtensionSuffix();
    protected abstract String makePath();
    protected abstract Map<String, Object> readTestData(InputStream is);

    protected void lift(TestData d) {
        for (String k : d.getKeys()) {
            //	should throw if it is not a testdata - non-testdata values cannot be lifted!
            Object o = internalData.put(k, d.getTestData(k));
            if (o != null) {
                LOG.warn("Non-unique key " + k + " when lifting " + groupPath + " test data");
            }
        }
    }

    protected Map<String, Object> readInputStream() {
        Map<String, Object> data;
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(filePath));
            data = readTestData(is);
        } catch (FileNotFoundException e) {
            throw new TestDataException("File not found on path " + filePath, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error("close failed", e);
                }
            }
        }
        return data;
    }

    protected List<String> getChildren(String path) {
        File [] files = new File(path).listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(getExtensionSuffix());
            }
        });
        List<String> names = new ArrayList<String>();
        if (files != null) {
            for (File f : files) {
                names.add(f.getName().replaceAll("\\" + getExtensionSuffix() + "$", ""));
            }
        }
        return names;
    }

    public static Boolean isValidDataStorage(String dirPath, final String fileNamePattern, final String extensionPattern) {
        File [] files = new File(dirPath).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return Pattern.matches(fileNamePattern + "\\." + extensionPattern, name);
            }
        });

        return (files != null) && (files.length > 0);
    }
}
