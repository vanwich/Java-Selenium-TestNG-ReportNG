package help.utils.excel;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.utils.excel.bind.ReflectionHelper;
import help.utils.excel.bind.cache.TableClassesCache;
import help.utils.excel.bind.cache.UnmarshallingCache;
import help.utils.excel.bind.cache.UnmarshallingClassInfo;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelCell;
import help.utils.excel.io.entity.area.table.TableCell;
import help.utils.excel.io.entity.area.table.TableRow;

public class ExcelUnmarshaller implements Closeable {
    private final List<CellType<?>> allowableCellTypes;
    private final boolean strictMatchBinding;
    private final ExcelFile excelFile;
    private final TableClassesCache<UnmarshallingClassInfo> cache;

    private Logger log = LoggerFactory.getLogger(ExcelUnmarshaller.class);

    public ExcelUnmarshaller(File excelFile) {
        this(excelFile, true, ExcelCell.getBaseTypes());
    }

    public ExcelUnmarshaller(InputStream inputStream) {
        this(inputStream, true, ExcelCell.getBaseTypes());
    }

    public ExcelUnmarshaller(File excelFile, boolean strictMatchBinding, List<CellType<?>> allowableCellTypes) {
        Assertions.assertThat(excelFile).as("Unable to unmarshal null or not existent file").isNotNull().exists();
        this.allowableCellTypes = Collections.unmodifiableList(allowableCellTypes);
        this.strictMatchBinding = strictMatchBinding;
        this.excelFile = new ExcelFile(excelFile, allowableCellTypes);
        this.cache = new UnmarshallingCache(this.excelFile, strictMatchBinding);
    }

    public ExcelUnmarshaller(InputStream inputStream, boolean strictMatchBinding, List<CellType<?>> allowableCellTypes) {
        this.allowableCellTypes = Collections.unmodifiableList(allowableCellTypes);
        this.strictMatchBinding = strictMatchBinding;
        this.excelFile = new ExcelFile(inputStream, allowableCellTypes);
        this.cache = new UnmarshallingCache(excelFile, strictMatchBinding);
    }

    public List<CellType<?>> getAllowableCellTypes() {
        return Collections.unmodifiableList(allowableCellTypes);
    }

    public boolean isStrictMatchBinding() {
        return strictMatchBinding;
    }

    @Override
    public void close() {
        flushCache();
        this.excelFile.close();
    }

    public <T> T unmarshal(Class<T> excelFileModel) {
        log.info("Getting excel file object of \"{}\" model from {} {} strict match binding",
                excelFileModel.getSimpleName(),
                this.excelFile.initializedFromFile() ? "file \"" + this.excelFile.getFile().getAbsolutePath() + "\"" : "InputStream",
                isStrictMatchBinding() ? "with" : "without");

        T excelFileObject = ReflectionHelper.getInstance(excelFileModel);
        for (Field tableField : ReflectionHelper.getAllAccessibleTableFieldsFromThisAndSuperClasses(excelFileModel)) {
            List<?> tablesObjects = unmarshalRows(cache.of(tableField).getTableClass());
            ReflectionHelper.setFieldValue(tableField, excelFileObject, tablesObjects);
        }

        log.info("Excel file unmarshalling completed successfully.");
        return excelFileObject;
    }

    public <T> List<T> unmarshalRows(Class<T> excelTableModel) {
        return unmarshalRows(excelTableModel, null);
    }

    public <T> List<T> unmarshalRows(Class<T> excelTableModel, List<Integer> rowsWithPrimaryKeyValues) {
        log.info("Getting table rows objects list of \"{}\" model from {}{} {} strict match binding",
                excelTableModel.getSimpleName(),
                this.excelFile.initializedFromFile() ? "file \"" + this.excelFile.getFile().getAbsolutePath() + "\"" : "InputStream",
                CollectionUtils.isNotEmpty(rowsWithPrimaryKeyValues) ? ", containing values in primary key columns: " + rowsWithPrimaryKeyValues : "",
                isStrictMatchBinding() ? "with" : "without");

        List<TableRow> rows = cache.of(excelTableModel).getRows(rowsWithPrimaryKeyValues);
        List<T> tablesObjects = new ArrayList<>(rows.size());
        for (TableRow row : rows) {
            T tableRowObject = getTableRowObject(excelTableModel, row);
            tablesObjects.add(tableRowObject);
        }

        log.info("Excel table rows unmarshalling completed successfully.");
        return tablesObjects;
    }

    public ExcelUnmarshaller flushCache() {
        this.cache.flushAll();
        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> T getTableRowObject(Class<T> tableClass, TableRow row) {
        if (cache.of(tableClass).hasObject(row.getIndex())) {
            return (T) cache.of(tableClass).getObject(row.getIndex());
        }

        T tableObject = (T) ReflectionHelper.getInstance(cache.of(tableClass).getTableClass());
        for (Field tableColumnField : cache.of(tableClass).getTableColumnsFields()) {
            Object value = null;
            switch (cache.of(tableClass).getBindType(tableColumnField)) {
                case REGULAR:
                    value = getFieldValue(tableClass, tableColumnField, row.getCell(cache.of(tableClass).getHeaderColumnIndex(tableColumnField)));
                    break;
                case TABLE:
                    value = getTableValue(tableColumnField, row.getCell(cache.of(tableClass).getHeaderColumnIndex(tableColumnField)));
                    break;
                case MULTI_COLUMNS:
                    value = getMultiColumnsFieldValue(tableClass, tableColumnField, row);
                    break;
            }
            ReflectionHelper.setFieldValue(tableColumnField, tableObject, value);
        }

        cache.of(tableClass).setObject(row.getIndex(), tableObject);
        return tableObject;
    }

    private Object getFieldValue(Class<?> tableClass, Field field, TableCell cell) {
        if (cell.isEmpty()) {
            return null;
        }
        return cell.getValue(cache.of(tableClass).getCellType(field), cache.of(tableClass).getDateTimeFormatters(field));
    }

    private Object getTableValue(Field field, TableCell cell) {
        if (cell.isEmpty()) {
            return null;
        }
        if (!List.class.isAssignableFrom(field.getType())) {
            return getTableRowObject(cache.of(field).getTableClass(), cache.of(field).getRow(cell.getIntValue()));
        }

        List<Integer> linkedTableRowIds = null;
        if (cell.hasType(ExcelCell.INTEGER_TYPE)) {
            linkedTableRowIds = Collections.singletonList(cell.getIntValue());
        } else {
            String linkedTableRowIDsString = cell.getStringValue();
            if (linkedTableRowIDsString != null) {
                String[] linkedTableRowIDs = linkedTableRowIDsString.split(cache.of(field).getPrimaryKeysSeparator());
                linkedTableRowIds = new ArrayList<>(linkedTableRowIDs.length);
                for (String id : linkedTableRowIDs) {
                    linkedTableRowIds.add(Integer.valueOf(id));
                }
            }
        }

        return getTableObjectValues(cache.of(field).getTableClass(), linkedTableRowIds);
    }

    private List<Object> getMultiColumnsFieldValue(Class<?> tableClass, Field field, TableRow row) {
        List<Object> multiColumnsValues = new ArrayList<>(cache.of(tableClass).getHeaderColumnsIndexes(field).size());
        for (Integer columnIndex : cache.of(tableClass).getHeaderColumnsIndexes(field)) {
            multiColumnsValues.add(getFieldValue(tableClass, field, row.getCell(columnIndex)));
        }
        return multiColumnsValues;
    }

    private List<Object> getTableObjectValues(Class<?> tableClass, List<Integer> tableRowsIds) {
        if (CollectionUtils.isEmpty(tableRowsIds)) {
            return null;
        }
        List<TableRow> tableRows = cache.of(tableClass).getRows(tableRowsIds);
        List<Object> tableObjectValues = new ArrayList<>(tableRows.size());
        for (TableRow row : tableRows) {
            tableObjectValues.add(getTableRowObject(tableClass, row));
        }
        return tableObjectValues;
    }
}
