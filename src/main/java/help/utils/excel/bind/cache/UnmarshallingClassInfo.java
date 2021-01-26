package help.utils.excel.bind.cache;

import static org.assertj.core.api.Assertions.assertThat;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.bind.annotation.ExcelTableElement;
import help.utils.excel.io.entity.area.sheet.ExcelSheet;
import help.utils.excel.io.entity.area.table.ExcelTable;
import help.utils.excel.io.entity.area.table.TableCell;
import help.utils.excel.io.entity.area.table.TableColumn;
import help.utils.excel.io.entity.area.table.TableRow;

public class UnmarshallingClassInfo extends TableClassInfo{
    private final Map<Integer, Object> rowsIndexesAndCreatedObjects;
    private final boolean strictMatchBinding;
    private Map<Integer, TableRow> primaryKeyColumnValuesAndRows;
    private ExcelSheet excelSheet;
    private ExcelTable excelTable;

    UnmarshallingClassInfo(Class<?> tableClass, ExcelFile excelFile, boolean strictMatchBinding) {
        super(tableClass, excelFile);
        this.rowsIndexesAndCreatedObjects = new HashMap<>();
        this.strictMatchBinding = strictMatchBinding;
    }

    public boolean isStrictMatchBinding() {
        return strictMatchBinding;
    }

    @Override
    public ExcelSheet getExcelSheet() {
        if (this.excelSheet == null) {
            if (!getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).containsSheetName().equals(ExcelTableElement.DEFAULT_CONTAINS_SHEET_NAME)) {
                //take sheet which contains sheet name pattern from annotation if it's defined
                this.excelSheet = getExcelFile().getSheetContains(getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).containsSheetName());
            } else {
                if (getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).sheetName().equals(ExcelTableElement.DEFAULT_SHEET_NAME)) {
                    //take first sheet from excel if sheet name is unset
                    this.excelSheet = getExcelFile().getSheet(1);
                } else {
                    //take sheet with defined name from annotation
                    this.excelSheet = getExcelFile().getSheet(getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).sheetName());
                }
            }
        }
        return this.excelSheet;
    }

    @Override
    public ExcelTable getExcelTable() {
        if (this.excelTable == null) {
            this.excelTable = findTable();
            this.tableColumnsFields = filterTableColumnsFields(this.excelTable, getTableColumnsFields());
        }
        return this.excelTable;
    }

    @Override
    public void flushInfo() {
        super.flushInfo();
        this.rowsIndexesAndCreatedObjects.clear();
        if (this.primaryKeyColumnValuesAndRows != null) {
            this.primaryKeyColumnValuesAndRows.clear();
            this.primaryKeyColumnValuesAndRows = null;
        }
    }

    public TableRow getRow(Integer primaryKeyExpectedValue) {
        if (this.primaryKeyColumnValuesAndRows == null) {
            this.primaryKeyColumnValuesAndRows = new HashMap<>(getExcelTable().getRows().size());
            TableColumn column = getExcelTable().getColumn(getPrimaryKeyColumnIndex());
            for (TableCell cell : column.getCells()) {
                this.primaryKeyColumnValuesAndRows.put(cell.getIntValue(), cell.getRow());
            }
        }

        if (!this.primaryKeyColumnValuesAndRows.containsKey(primaryKeyExpectedValue)) {
            throw new ExcelProcessingException(String.format("There is no \"%1$s\" value in primary key column #%2$s in table %3$s", primaryKeyExpectedValue, getPrimaryKeyColumnIndex(), getExcelTable()));
        }

        return this.primaryKeyColumnValuesAndRows.get(primaryKeyExpectedValue);
    }

    public List<TableRow> getRows(List<Integer> primaryKeyExpectedValues) {
        if (CollectionUtils.isEmpty(primaryKeyExpectedValues)) {
            return getExcelTable().getRows();
        }

        List<TableRow> foundRows = new ArrayList<>(primaryKeyExpectedValues.size());
        for (Integer expectedValue : primaryKeyExpectedValues) {
            foundRows.add(getRow(expectedValue));
        }
        return foundRows;
    }

    public boolean hasObject(int rowIndex) {
        return this.rowsIndexesAndCreatedObjects.containsKey(rowIndex);
    }

    public Object getObject(Integer rowIndex) {
        return this.rowsIndexesAndCreatedObjects.get(rowIndex);
    }

    public void setObject(int rowIndex, Object object) {
        this.rowsIndexesAndCreatedObjects.put(rowIndex, object);
    }

    private List<String> getHeaderColumnNames() {
        return getTableColumnsFields().stream().map(this::getHeaderColumnName).collect(Collectors.toList());
    }

    private ExcelTable findTable() {
        assertThat(getTableClass().isPrimitive()).as("\"%s\" is primitive type. Only non-primitive types are supported for excel table model definition", getTableClass().getSimpleName()).isFalse();
        int headerRowIndex = getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).headerRowIndex();
        ExcelTable table;

        boolean hasEmptyRows = getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).hasEmptyRows();
        ExcelSheet sheet = getExcelSheet();
        if (headerRowIndex < 0) {
            List<String> headerColumnNames = getHeaderColumnNames();
            table = sheet.getTable(isCaseIgnoredInAnyColumnField(), hasEmptyRows, headerColumnNames.toArray(new String[headerColumnNames.size()]));
        } else {
            if (isStrictMatchBinding()) {
                List<String> headerColumnNames = getHeaderColumnNames();
                table = sheet.getTable(headerRowIndex, null, hasEmptyRows, isCaseIgnoredInAnyColumnField(), headerColumnNames.toArray(new String[headerColumnNames.size()]));
            } else {
                table = sheet.getTable(headerRowIndex);
            }
        }

        List<Integer> extraTableColumnsIndexes = table.getColumnsIndexes();
        for (Field columnField : getTableColumnsFields()) {
            extraTableColumnsIndexes.removeAll(getFieldInfo(columnField).getHeaderColumnsIndexes(table.getHeader(), isCaseIgnoredForAllColumns()));
        }

        if (!extraTableColumnsIndexes.isEmpty()) {
            List<String> extraTableColumnNames = table.getHeader().getCellsByIndexes(extraTableColumnsIndexes).stream().map(TableCell::getStringValue).collect(Collectors.toList());
            String message = String.format("Extra header column(s) detected in excel table on sheet \"%1$s\" without binded field(s) from class \"%2$s\": %3$s.",
                    table.getSheetName(), getTableClass().getName(), extraTableColumnNames);
            if (isStrictMatchBinding()) {
                throw new ExcelProcessingException("Excel unmarshalling with strict match binding has been failed. " + message);
            }
            log.warn("{} Result object will not have missed field(s)", message);
            table.excludeColumns(extraTableColumnsIndexes.toArray(new Integer[extraTableColumnsIndexes.size()]));
        }

        return table;
    }

    private List<Field> filterTableColumnsFields(ExcelTable table, List<Field> tableFields) {
        List<Field> filteredTableColumnsFields = new ArrayList<>(tableFields);
        List<Field> missedTableColumnsFields = new ArrayList<>();
        for (Field columnField : filteredTableColumnsFields) {
            if (getFieldInfo(columnField).getHeaderColumnsIndexes(table.getHeader(), isCaseIgnoredForAllColumns()).isEmpty()) {
                missedTableColumnsFields.add(columnField);
            }
        }

        if (!missedTableColumnsFields.isEmpty()) {
            List<String> missedFieldColumnNames = new ArrayList<>(missedTableColumnsFields.size());
            for (Field f : missedTableColumnsFields) {
                missedFieldColumnNames.add(getFieldType(f).getSimpleName() + " " + f.getName());
            }
            String message = String.format("Missed header column(s) in excel table on sheet \"%1$s\" for field(s) from class \"%2$s\": %3$s.",
                    table.getSheet().getSheetName(), getTableClass().getName(), missedFieldColumnNames);

            if (isStrictMatchBinding()) {
                throw new ExcelProcessingException("Excel unmarshalling with strict match binding has been failed." + message);
            }
            log.warn("{} Field(s) with missed column(s) in result object will have default value(s) of appropriate type(s)", message);
        }

        filteredTableColumnsFields.removeAll(missedTableColumnsFields);
        return filteredTableColumnsFields;
    }
}
