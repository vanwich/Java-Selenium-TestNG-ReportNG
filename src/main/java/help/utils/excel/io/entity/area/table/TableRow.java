package help.utils.excel.io.entity.area.table;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelCell;
import help.utils.excel.io.entity.area.ExcelRow;

public class TableRow extends ExcelRow<TableCell> {
    public TableRow(Row row, int rowIndexInTable, int rowIndexOnSheet, List<Integer> columnsIndexesOnSheet, ExcelTable table) {
        this(row, rowIndexInTable, rowIndexOnSheet, columnsIndexesOnSheet, table, table.getCellTypes());
    }

    public TableRow(Row row, int rowIndexInTable, int rowIndexOnSheet, List<Integer> columnsIndexesOnSheet, ExcelTable table, List<CellType<?>> cellTypes) {
        super(row, rowIndexInTable, rowIndexOnSheet, columnsIndexesOnSheet, table, cellTypes);
    }

    public ExcelTable getTable() {
        return (ExcelTable) getArea();
    }

    public Map<String, Object> getTableValues() {
        Map<String, Object> values = new LinkedHashMap<>(getCellsNumber());
        for (TableCell cell : getCells()) {
            values.put(cell.getHeaderColumnName(), cell.getValue());
        }
        return values;
    }

    public Map<String, String> getTableStringValues() {
        Map<String, String> values = new LinkedHashMap<>(getCellsNumber());
        for (TableCell cell : getCells()) {
            values.put(cell.getHeaderColumnName(), cell.getStringValue());
        }
        return values;
    }

    @Override
    protected TableCell createCell(int columnIndexInTable, int columnIndexOnSheet) {
        Cell poiCell = getPoiRow() != null ? getPoiRow().getCell(columnIndexOnSheet - 1) : null;
        return new TableCell(poiCell, columnIndexInTable, columnIndexOnSheet, this, getCellTypes());
    }

    @Override
    protected void addCell(TableCell cell) {
        super.addCell(cell);
    }

    @Override
    public String toString() {
        return "TableRow{" +
                "sheetName=" + getSheetName() +
                ", rowIndex=" + getIndex() +
                ", rowIndexOnSheet=" + getIndexOnSheet() +
                ", columnsNumber=" + getCellsNumber() +
                ", values=" + getTableStringValues() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    public boolean hasColumn(String headerColumnName) {
        return hasColumn(headerColumnName, false);
    }

    public boolean hasColumn(String headerColumnName, boolean ignoreCase) {
        return getTable().getHeader().hasColumn(headerColumnName, ignoreCase);
    }

    public int getIndex(String headerColumnName) {
        return getIndex(headerColumnName, false);
    }

    public int getIndex(String headerColumnName, boolean ignoreCase) {
        return getTable().getHeader().getColumnIndex(headerColumnName, ignoreCase);
    }

    public int getIndexOnSheet(String headerColumnName) {
        return getIndexOnSheet(headerColumnName, false);
    }

    public int getIndexOnSheet(String headerColumnName, boolean ignoreCase) {
        return getTable().getHeader().getColumnIndexOnSheet(headerColumnName, ignoreCase);
    }

    public String getColumnName(int columnIndex) {
        return getTable().getHeader().getColumnName(columnIndex);
    }

    public List<TableCell> getCellsByHeaderColumnNames(List<String> headerColumnsNames) {
        return getCellsByHeaderColumnNames(headerColumnsNames, false);
    }

    public List<TableCell> getCellsByHeaderColumnNames(List<String> headerColumnsNames, boolean ignoreCase) {
        List<Integer> headerColumnsIndexes = headerColumnsNames.stream().map(columnName -> getIndex(columnName, ignoreCase)).collect(Collectors.toList());
        return getCellsByIndexes(headerColumnsIndexes);
    }

    public TableCell getCell(String headerColumnName) {
        return getCell(headerColumnName, false);
    }

    public TableCell getCell(String headerColumnName, boolean ignoreCase) {
        return getTable().getColumn(headerColumnName, ignoreCase).getCell(getIndex());
    }

    public Object getValue(String headerColumnName) {
        return getValue(getIndex(headerColumnName));
    }

    public <T> T getValue(String headerColumnName, CellType<T> cellType, DateTimeFormatter... dateTimeFormatters) {
        return getValue(getIndex(headerColumnName), cellType, dateTimeFormatters);
    }

    public String getStringValue(String headerColumnName) {
        return getStringValue(getIndex(headerColumnName));
    }

    public Boolean getBoolValue(String headerColumnName) {
        return getBoolValue(getIndex(headerColumnName));
    }

    public Integer getIntValue(String headerColumnName) {
        return getIntValue(getIndex(headerColumnName));
    }

    public Double getDoubleValue(String headerColumnName) {
        return getDoubleValue(getIndex(headerColumnName));
    }

    public <T extends Temporal> T getDateValue(String headerColumnName, DateTimeFormatter... dateTimeFormatters) {
        return getDateValue(getIndex(headerColumnName));
    }

    public TableRow setValue(String headerColumnName, Object value) {
        return (TableRow) setValue(getIndex(headerColumnName), value);
    }

    public <T> TableRow setValue(String headerColumnName, T value, CellType<T> valueType) {
        return (TableRow) setValue(getIndex(headerColumnName), value, valueType);
    }

    public boolean hasValue(String headerColumnName, Object expectedValue, DateTimeFormatter... dateTimeFormatters) {
        return hasValue(headerColumnName, false, expectedValue, dateTimeFormatters);
    }

    public boolean hasValue(String headerColumnName, boolean ignoreHeaderColumnNameCase, Object expectedValue, DateTimeFormatter... dateTimeFormatters) {
        return hasValue(getIndex(headerColumnName, ignoreHeaderColumnNameCase), expectedValue, dateTimeFormatters);
    }

    public boolean isEmpty(String headerColumnName) {
        return isEmpty(headerColumnName, false);
    }

    public boolean isEmpty(String headerColumnName, boolean ignoreCase) {
        return isEmpty(getIndex(headerColumnName, ignoreCase));
    }

    public List<TableCell> getCells(String... headerColumnNames) {
        return getCells(false, headerColumnNames);
    }

    public List<TableCell> getCells(boolean ignoreCase, String... headerColumnNames) {
        List<TableCell> cells = new ArrayList<>(headerColumnNames.length);
        List<String> headerColumnNamesList = ignoreCase ? Arrays.stream(headerColumnNames).map(String::toLowerCase).collect(Collectors.toList()) : Arrays.asList(headerColumnNames);
        for (TableCell cell : getCells()) {
            if (ignoreCase ? headerColumnNamesList.contains(cell.getHeaderColumnName().toLowerCase()) : headerColumnNamesList.contains(cell.getHeaderColumnName())) {
                cells.add(cell);
            }
        }
        return cells;
    }

    public List<TableCell> getCellsContains(String headerColumnNamePattern) {
        return getCellsContains(headerColumnNamePattern, false);
    }

    public List<TableCell> getCellsContains(String headerColumnNamePattern, boolean ignoreCase) {
        List<TableCell> cells = new ArrayList<>();
        for (TableCell cell : getCells()) {
            if (ignoreCase ? cell.getHeaderColumnName().toLowerCase().contains(headerColumnNamePattern.toLowerCase()) : cell.getHeaderColumnName().contains(headerColumnNamePattern)) {
                cells.add(cell);
            }
        }
        return cells;
    }

    public int getIntSum(String... headerColumnNames) {
        return getIntSum(false, headerColumnNames);
    }

    public int getIntSum(boolean ignoreCase, String... headerColumnNames) {
        Integer[] indexes = getCells(ignoreCase, headerColumnNames).stream().map(ExcelCell::getColumnIndex).toArray(Integer[]::new);
        return getIntSum(indexes);
    }

    public int getIntSumContains(String headerColumnNamePattern) {
        return getIntSumContains(false, headerColumnNamePattern);
    }

    public int getIntSumContains(boolean ignoreCase, String headerColumnNamePattern) {
        Integer[] indexes = getCellsContains(headerColumnNamePattern, ignoreCase).stream().map(TableCell::getColumnIndex).toArray(Integer[]::new);
        return getIntSum(indexes);
    }
}
