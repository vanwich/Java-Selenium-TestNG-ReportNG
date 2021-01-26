package help.utils.excel.bind.cache;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.bind.ReflectionHelper;
import help.utils.excel.bind.annotation.ExcelTableElement;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.sheet.ExcelSheet;
import help.utils.excel.io.entity.area.table.ExcelTable;

public abstract class TableClassInfo {
    protected static Logger log = LoggerFactory.getLogger(TableClassInfo.class);
    private final Class<?> tableClass;
    protected List<Field> tableColumnsFields;
    private ExcelFile excelFile;
    private Class<?> annotatedTableClass;
    private Boolean isCaseIgnoredForAllColumns;
    private List<TableFieldInfo> tableFieldsInfos;
    private String primaryKeysSeparator;
    private Field primaryKeyColumnField;
    private Integer primaryKeyColumnIndex;

    TableClassInfo(Class<?> tableClass, ExcelFile excelFile) {
        this.tableClass = tableClass;
        this.excelFile = excelFile;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public Class<?> getAnnotatedTableClass() {
        if (this.annotatedTableClass == null) {
            this.annotatedTableClass = ReflectionHelper.getThisAndAllSuperClasses(tableClass).stream().filter(clazz -> clazz.isAnnotationPresent(ExcelTableElement.class)).findFirst().orElseThrow(
                    () -> new ExcelProcessingException(String.format("Unable to find excel table for \"%1$s\" class, neither it nor any super class has \"%2$s\" annotation", tableClass.getSimpleName(), ExcelTableElement.class.getSimpleName())));
        }
        return this.annotatedTableClass;
    }

    public boolean isCaseIgnoredForAllColumns() {
        if (this.isCaseIgnoredForAllColumns == null) {
            this.isCaseIgnoredForAllColumns = getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).ignoreCase();
        }
        return isCaseIgnoredForAllColumns;
    }

    public List<Field> getTableColumnsFields() {
        if (this.tableColumnsFields == null) {
            this.tableColumnsFields = ReflectionHelper.getAllAccessibleFieldsFromThisAndSuperClasses(getTableClass());
        }
        return Collections.unmodifiableList(this.tableColumnsFields);
    }

    public abstract ExcelSheet getExcelSheet();

    public abstract ExcelTable getExcelTable();

    public void flushInfo() {
        this.tableColumnsFields = null;
        this.tableFieldsInfos = null;
    }

    public String getPrimaryKeysSeparator() {
        if (this.primaryKeysSeparator == null) {
            this.primaryKeysSeparator = getFieldInfo(getPrimaryKeyColumnField()).getPrimaryKeySeparator();
        }
        return this.primaryKeysSeparator;
    }

    public Field getPrimaryKeyColumnField() {
        if (this.primaryKeyColumnField == null) {
            this.primaryKeyColumnField = getTableFieldsInfos().stream().filter(TableFieldInfo::isPrimaryKeyField).findFirst()
                    .orElseThrow(() -> new ExcelProcessingException(String.format("\"%s\" class does not have any primary key field", tableClass.getName()))).getTableField();
        }
        return this.primaryKeyColumnField;
    }

    public Integer getPrimaryKeyColumnIndex() {
        if (this.primaryKeyColumnIndex == null) {
            this.primaryKeyColumnIndex = getHeaderColumnIndex(getPrimaryKeyColumnField());
        }
        return this.primaryKeyColumnIndex;
    }

    public boolean isCaseIgnoredInAnyColumnField() {
        return getTableColumnsFields().stream().anyMatch(this::isCaseIgnored);
    }

    ExcelFile getExcelFile() {
        return excelFile;
    }

    public TableFieldInfo getFieldInfo(Field tableField) {
        for (TableFieldInfo tableFieldInfo : getTableFieldsInfos()) {
            if (tableFieldInfo.getTableField().equals(tableField)) {
                return tableFieldInfo;
            }
        }
        throw new ExcelProcessingException(String.format("Class \"%s\" does not have \"%s\" field", getTableClass().getName(), tableField));
    }

    public boolean isCaseIgnored(Field tableField) {
        return isCaseIgnoredForAllColumns() || getFieldInfo(tableField).isCaseIgnored();
    }

    public String getHeaderColumnName(Field tableField) {
        return getFieldInfo(tableField).getHeaderColumnName();
    }

    public int getHeaderColumnIndex(Field tableField) {
        return getFieldInfo(tableField).getHeaderColumnIndex(getExcelTable().getHeader(), isCaseIgnoredForAllColumns());
    }

    public List<Integer> getHeaderColumnsIndexes(Field tableField) {
        return getFieldInfo(tableField).getHeaderColumnsIndexes(getExcelTable().getHeader(), isCaseIgnoredForAllColumns());
    }

    public TableFieldInfo.BindType getBindType(Field tableField) {
        return getFieldInfo(tableField).getBindType();
    }

    public Class<?> getFieldType(Field tableField) {
        return getFieldInfo(tableField).getFieldType();
    }

    public DateTimeFormatter[] getDateTimeFormatters(Field tableField) {
        return getFieldInfo(tableField).getDateTimeFormatters();
    }

    public CellType<?> getCellType(Field tableField) {
        return getFieldInfo(tableField).getCellType(this.excelFile.getCellTypes());
    }

    private List<TableFieldInfo> getTableFieldsInfos() {
        if (this.tableFieldsInfos == null) {
            List<Field> tableColumnsFields = getTableColumnsFields();
            this.tableFieldsInfos = new ArrayList<>(tableColumnsFields.size());
            for (Field tableField : tableColumnsFields) {
                this.tableFieldsInfos.add(new TableFieldInfo(tableField, getTableClass(), getExcelFile().getCellTypes()));
            }
        }
        return this.tableFieldsInfos;
    }
}
