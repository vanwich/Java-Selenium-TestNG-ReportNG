package help.utils.excel.bind.cache;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.ClassUtils;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.bind.ReflectionHelper;
import help.utils.excel.bind.annotation.ExcelColumnElement;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.table.TableCell;
import help.utils.excel.io.entity.area.table.TableHeader;

public final class TableFieldInfo {
    private final Field tableField;
    private final Class<?> tableClass;
    private final List<CellType<?>> availableCellTypes;

    private List<Integer> headerColumnsIndexes;
    private Boolean isCaseIgnored;
    private Boolean isPrimaryKeyField;
    private Boolean hasHeaderColumnNamePattern;
    private String primaryKeysSeparator;
    private String headerColumnName;
    private Class<?> fieldType;
    private BindType bindType;
    private DateTimeFormatter[] dateTimeFormatters;
    private CellType<?> cellType;

    public TableFieldInfo(Field tableField, Class<?> tableClass, List<CellType<?>> availableCellTypes) {
        this.tableField = tableField;
        this.tableClass = tableClass;
        this.availableCellTypes = new ArrayList<>(availableCellTypes);
        this.headerColumnsIndexes = new ArrayList<>();
    }

    public Field getTableField() {
        return tableField;
    }

    public Class<?> getTableClass() {
        return tableClass;
    }

    public boolean isCaseIgnored() {
        if (this.isCaseIgnored == null) {
            if (tableField.isAnnotationPresent(ExcelColumnElement.class)) {
                this.isCaseIgnored = tableField.getAnnotation(ExcelColumnElement.class).ignoreCase();
            } else {
                this.isCaseIgnored = ExcelColumnElement.DEFAULT_CASE_IGNORED;
            }
        }
        return this.isCaseIgnored;
    }

    public boolean isPrimaryKeyField() {
        if (this.isPrimaryKeyField == null) {
            this.isPrimaryKeyField = getTableField().isAnnotationPresent(ExcelColumnElement.class) && getTableField().getAnnotation(ExcelColumnElement.class).isPrimaryKey();
        }
        return this.isPrimaryKeyField;
    }

    public String getPrimaryKeySeparator() {
        if (this.primaryKeysSeparator == null) {
            if (tableField.isAnnotationPresent(ExcelColumnElement.class)) {
                this.primaryKeysSeparator = tableField.getAnnotation(ExcelColumnElement.class).primaryKeysSeparator();
            } else {
                this.primaryKeysSeparator = ExcelColumnElement.DEFAULT_PRIMARY_KEY_SEPARATOR;
            }
        }
        return this.primaryKeysSeparator;
    }

    public String getHeaderColumnName() {
        if (this.headerColumnName == null) {
            this.headerColumnName = hasHeaderColumnNamePattern() ? getHeaderColumnNamePattern(tableField) : getHeaderColumnName(tableField);
        }
        return this.headerColumnName;
    }

    public Class<?> getFieldType() {
        if (this.fieldType == null) {
            this.fieldType = ReflectionHelper.getFieldType(this.tableField);
        }
        return this.fieldType;
    }

    public BindType getBindType() {
        if (this.bindType == null) {
            if (ReflectionHelper.isTableClassField(getTableField())) {
                this.bindType = BindType.TABLE;
            } else if (List.class.isAssignableFrom(getTableField().getType())) {
                this.bindType = BindType.MULTI_COLUMNS;
            } else {
                this.bindType = BindType.REGULAR;
            }
        }
        return this.bindType;
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    public DateTimeFormatter[] getDateTimeFormatters() {
        if (this.dateTimeFormatters == null) {
            List<DateTimeFormatter> dateTimeFormattersList = new ArrayList<>();

            if (tableField.isAnnotationPresent(ExcelColumnElement.class)) {
                for (String datePattern : tableField.getAnnotation(ExcelColumnElement.class).dateFormatPatterns()) {
                    try {
                        dateTimeFormattersList.add(DateTimeFormatter.ofPattern(datePattern));
                    } catch (IllegalArgumentException e) {
                        throw new ExcelProcessingException(String.format("Unable to get valid DateTimeFormatter for field \"%1$s\" with date pattern \"%2$s\"", tableField.getName(), datePattern), e);
                    }
                }
            }

            this.dateTimeFormatters = dateTimeFormattersList.toArray(new DateTimeFormatter[dateTimeFormattersList.size()]);
        }
        return this.dateTimeFormatters;
    }

    public CellType<?> getCellType(List<CellType<?>> availableCellTypes) {
        if (this.cellType == null) {
            for (CellType<?> cellType : availableCellTypes) {
                if (ClassUtils.isAssignable(cellType.getEndType(), getFieldType(), true)) {
                    this.cellType = cellType;
                    return this.cellType;
                }
            }
            throw new ExcelProcessingException(String.format("Field type \"%1$s\" from \"%2$s\" class is not supported for unmarshalling, available cell types are: %3$s",
                    fieldType.getName(), getTableClass().getName(), availableCellTypes));
        }

        return this.cellType;
    }

    public boolean hasHeaderColumnNamePattern() {
        if (this.hasHeaderColumnNamePattern == null) {
            this.hasHeaderColumnNamePattern = tableField.isAnnotationPresent(ExcelColumnElement.class)
                    && !Objects.equals(tableField.getAnnotation(ExcelColumnElement.class).containsName(), ExcelColumnElement.DEFAULT_COLUMN_NAME);
        }
        return this.hasHeaderColumnNamePattern;
    }

    public int getHeaderColumnIndex(TableHeader header, boolean isCaseIgnoredForAllColumns) {
        return getHeaderColumnsIndexes(header, isCaseIgnoredForAllColumns).get(0);
    }

    public List<Integer> getHeaderColumnsIndexes(TableHeader header, boolean isCaseIgnoredForAllColumns) {
        if (this.headerColumnsIndexes.isEmpty()) {
            List<Integer> columnsIndexes = new ArrayList<>();
            boolean ignoreCase = isCaseIgnoredForAllColumns || isCaseIgnored();
            boolean isColumnFound = false;

            for (TableCell cell : header.getCells()) {
                String tableColumnName = cell.getStringValue();
                if (hasHeaderColumnNamePattern()) {
                    if (ignoreCase ? tableColumnName.toLowerCase().contains(getHeaderColumnName().toLowerCase()) : tableColumnName.contains(getHeaderColumnName())) {
                        isColumnFound = true;
                    }
                } else {
                    if (ignoreCase ? tableColumnName.toLowerCase().equals(getHeaderColumnName().toLowerCase()) : tableColumnName.equals(getHeaderColumnName())) {
                        isColumnFound = true;
                    }
                }

                if (isColumnFound) {
                    columnsIndexes.add(cell.getColumnIndex());
                    isColumnFound = false;
                    if (!getBindType().equals(BindType.MULTI_COLUMNS)) {
                        break;
                    }
                }
            }
            this.headerColumnsIndexes = columnsIndexes;
        }

        return Collections.unmodifiableList(this.headerColumnsIndexes);
    }

    private String getHeaderColumnNamePattern(Field field) {
        return field.getAnnotation(ExcelColumnElement.class).containsName();
    }

    private String getHeaderColumnName(Field field) {
        if (field.isAnnotationPresent(ExcelColumnElement.class) && !field.getAnnotation(ExcelColumnElement.class).name().equals(ExcelColumnElement.DEFAULT_COLUMN_NAME)) {
            return field.getAnnotation(ExcelColumnElement.class).name();
        }
        return field.getName();
    }

    public enum BindType {
        REGULAR,
        MULTI_COLUMNS,
        TABLE
    }
}
