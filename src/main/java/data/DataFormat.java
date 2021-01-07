package data;

public enum  DataFormat {
    XLS("xls"),
    YAML("yaml"),
    JSON("json"),
    AUTO(null);

    private String extension;

    DataFormat(String e) {
        extension = e;
    }

    public String getExtension() {
        return extension;
    }

    public static DataFormat fromName(String str) {
        for (DataFormat fmt : DataFormat.values()) {
            if (fmt.toString().equalsIgnoreCase(str)) {
                return fmt;
            }
        }
        return null;
    }

    public static DataFormat fromExtension(String e) {
        for (DataFormat type : DataFormat.values()) {
            if (type.getExtension().equalsIgnoreCase(e)) {
                return type;
            }
        }
        return null;
    }
}
