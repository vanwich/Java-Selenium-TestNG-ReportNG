package help.utils.excel.bind.cache;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import help.utils.excel.ExcelFile;
import help.utils.excel.bind.ReflectionHelper;

public abstract class TableClassesCache<T extends TableClassInfo> {
    protected final Map<Class<?>, T> tableClassesMap;
    private final ExcelFile excelFile;

    public TableClassesCache(ExcelFile excelFile) {
        this.excelFile = excelFile;
        this.tableClassesMap = new HashMap<>();
    }

    protected ExcelFile getExcelFile() {
        return excelFile;
    }

    public T of(Field field) {
        return of(ReflectionHelper.getFieldType(field));
    }

    public T of(Class<?> tableClass) {
        if (!this.tableClassesMap.containsKey(tableClass)) {
            T tableClassInfo = getTableClassInfoInstance(tableClass);
            this.tableClassesMap.put(tableClass, tableClassInfo);
            return tableClassInfo;
        }
        return this.tableClassesMap.get(tableClass);
    }

    public void flush(Class<?> tableClass) {
        of(tableClass).flushInfo();
        this.tableClassesMap.remove(tableClass);
    }

    public void flushAll() {
        tableClassesMap.values().forEach(TableClassInfo::flushInfo);
        this.tableClassesMap.clear();
    }

    protected abstract T getTableClassInfoInstance(Class<?> tableClass);
}
