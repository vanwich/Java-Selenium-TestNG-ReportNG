package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public abstract class DataWriter {
    protected String targetPath;
    private boolean forceArrays;

    /**
     * Crate DataWriter instance
     * @param path - path to store data to (without extension!)
     * @param farrs - if true, all string values are converted to single-entry arrays (useful for metadata)
     */
    public DataWriter(String path, boolean farrs) {
        targetPath = path;
        forceArrays = farrs;
    }

    /**
     * Write the whole data collection
     * @param data - data map (suite) whose direct children should be other maps (groups)
     */
    public void write(Map<String, Object> data) {
        start();
        if (forceArrays) {
            data = arraify(data);
        }

        for (Map.Entry<String, Object> groupEntry : data.entrySet()) {
            Map<String, Object> group = safeCast(groupEntry.getValue(), Map.class);
            try {
                writeGroup(groupEntry.getKey(), group);
            } catch (IOException e) {
                throw new TestDataException("Failed to write data group " + groupEntry.getKey(), e);
            }
        }

        finish();
    }

    /**
     * Start writing data collection
     */
    protected void start() {
        try {
            FileUtils.forceMkdir(new File(targetPath));
        } catch (IOException e) {
            throw new TestDataException("Cannot create data folder " + targetPath, e);
        }
    }

    /**
     * Write data group
     * @param groupName - name of the group
     * @param group - group map (may contain strings, lists or other maps)
     * @throws IOException if there is an I/O error
     */
    protected abstract void writeGroup(String groupName, Map<String, Object> group) throws IOException;

    /**
     * Finish writing data collection
     */
    protected void finish() {};

    /**
     * Cast object to specified class
     * @param <T> type to cast the object to
     * @param obj object to cast
     * @param clazz class to cast to
     * @return cast object
     */
    @SuppressWarnings("unchecked")
    protected <T> T safeCast(Object obj, Class<?> clazz) {
        if (clazz.isInstance(obj)) {
            return (T) clazz.cast(obj);
        } else {
            throw new TestDataException("Object " + obj.toString() + " is not a " + clazz.getName());
        }
    }

    //	i know this is ugly. this is needed for single-entry metadata values (to ensure they are serialized as arrays, not strings)
    @SuppressWarnings("unchecked")
    protected <T> T arraify(T data) {
        if (data instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) data;
            for (Map.Entry<String, Object> e : map.entrySet()) {
                map.put(e.getKey(), arraify(e.getValue()));
            }
            return (T) map;
        } else if (data instanceof String) {
            List<String> list = new ArrayList<String>();
            list.add((String) data);
            return (T) list;
        } else if (data instanceof List) {
            List<Object> list = (List<Object>) data;
            if (list.isEmpty() || list.get(0) instanceof String) {
                return data;
            } else {
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, arraify(list.get(i)));
                }
                return (T) list;
            }
        } else {
            throw new IllegalArgumentException("Cannot process parameter " + data + " of type " + data.getClass());
        }
    }
}
