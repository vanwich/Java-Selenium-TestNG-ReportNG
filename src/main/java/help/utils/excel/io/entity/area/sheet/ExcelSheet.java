package help.utils.excel.io.entity.area.sheet;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelArea;
import help.utils.excel.io.entity.area.table.ExcelTable;

public class ExcelSheet extends ExcelArea<SheetCell, SheetRow, SheetColumn> {
    private final int sheetIndex;
    private List<ExcelTable> tables;

    public ExcelSheet(Sheet sheet, int sheetIndex, ExcelFile excelFile) {
        this(sheet, sheetIndex, excelFile, excelFile.getCellTypes());
    }

    public ExcelSheet(Sheet sheet, int sheetIndex, ExcelFile excelFile, List<CellType<?>> cellTypes) {
        this(sheet, sheetIndex, null, null, excelFile, cellTypes);
    }

    public ExcelSheet(Sheet sheet, int sheetIndex, List<Integer> columnsIndexes, List<Integer> rowsIndexes, ExcelFile excelFile, List<CellType<?>> cellTypes) {
        super(sheet, columnsIndexes, rowsIndexes, excelFile, cellTypes);
        this.sheetIndex = sheetIndex;
        this.tables = new ArrayList<>();
    }

    public int getSheetIndex() {
        return this.sheetIndex;
    }

    /**
     * @return only previously added tables by {@link #addTable(ExcelTable)} or found by {@link #getTable(String...)}, {@link #getTable(boolean, String...)} and  {@link #getTable(int, List, boolean, boolean, String...)} methods
     */
    public List<ExcelTable> getTables() {
        return Collections.unmodifiableList(this.tables);
    }

    @Override
    protected SheetRow createRow(Row row, int rowIndexInArea, int rowIndexOnSheet) {
        return new SheetRow(row, rowIndexOnSheet, getColumnsIndexesOnSheet(), this, getCellTypes());
    }

    @Override
    protected SheetColumn createColumn(int columnIndexInArea, int columnIndexOnSheet) {
        return new SheetColumn(columnIndexOnSheet, getRowsIndexesOnSheet(), this, getCellTypes());
    }

    /**
     * Register cell types for next found ExcelTables and ExcelRows and update cell types for found {@link #tables}
     */
    @Override
    public ExcelSheet registerCellType(List<CellType<?>> cellTypes) {
        super.registerCellType(cellTypes);
        getTables().forEach(t -> t.registerCellType(cellTypes));
        return this;
    }

    @Override
    public String toString() {
        return "ExcelSheet{" +
                "sheetNumber=" + getSheetIndex() +
                ", sheetName=" + getSheetName() +
                ", rowsNumber=" + getRowsNumber() +
                ", columnsNumber=" + getColumnsNumber() +
                ", tablesNumber=" + getTables().size() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    @Override
    public ExcelSheet addRows(int numberOfRows) {
        return (ExcelSheet) super.addRows(numberOfRows);
    }

    public ExcelSheet addColumns(int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            addColumn();
        }
        return this;
    }

    public SheetColumn addColumn() {
        SheetColumn lastColumn = getLastColumn();
        int newColumnIndexOnSheet = lastColumn == null ? 1 : lastColumn.getIndexOnSheet() + 1;
        SheetColumn newColumn = createColumn(newColumnIndexOnSheet, newColumnIndexOnSheet);
        for (SheetRow row : getRows()) {
            SheetCell cell = row.createCell(newColumnIndexOnSheet, newColumnIndexOnSheet);
            row.addCell(cell);
        }
        return addColumn(newColumn);
    }

    public ExcelTable getTable(String... headerColumnsNames) {
        return getTable(false, headerColumnsNames);
    }

    public ExcelTable getTable(boolean ignoreCase, String... headerColumnsNames) {
        SheetRow row = getRow(ignoreCase, headerColumnsNames);
        return getTable(row.getIndex(), null, false, ignoreCase, headerColumnsNames);
    }

    public ExcelTable getTable(boolean ignoreCase, boolean hasEmptyRows, String... headerColumnsNames) {
        SheetRow row = getRow(ignoreCase, headerColumnsNames);
        return getTable(row.getIndex(), null, hasEmptyRows, ignoreCase, headerColumnsNames);
    }

    public ExcelTable getTable(int headerRowIndexOnSheet, String... headerColumnsNames) {
        return getTable(headerRowIndexOnSheet, null, headerColumnsNames);
    }

    public ExcelTable getTable(int headerRowIndexOnSheet, boolean hasEmptyRows, String... headerColumnsNames) {
        return getTable(headerRowIndexOnSheet, null, hasEmptyRows, false, headerColumnsNames);
    }

    public ExcelTable getTable(int headerRowIndexOnSheet, List<Integer> rowsIndexesInTable, String... headerColumnsNames) {
        return getTable(headerRowIndexOnSheet, rowsIndexesInTable, false, false, headerColumnsNames);
    }

    /**
     *  Get ExcelTable object on current sheet with provided {@code headerColumnsNames} header columns found in {@code headerRowIndexOnSheet} row number.
     *
     * @param headerRowIndexOnSheet Table's header row number on current sheet. Index starts from 1
     * @param rowsIndexesInTable rows indexes in table to be used as table rows. If null and {@code hasEmptyRows} is false then all rows from header row up to first row with all empty {@code headerColumnsNames} will be used as tables' rows
     * @param hasEmptyRows if true then all rows starting from {@code headerRowIndexOnSheet} will be used as table rows including empty rows. Otherwise all rows starting from {@code headerRowIndexOnSheet} up to first occurrence of empty row will be used as table's rows.
     *                     ignored if {@code rowsIndexesInTable} is not null
     * @param ignoreCase if true then ignore header column names while searching header columns indexes within {@code headerRowIndexOnSheet} row
     * @param headerColumnsNames header column names of needed ExcelTable. If array is empty then all columns from {@code headerRowIndexOnSheet} will be used as column names
     * @return {@link ExcelTable} object representation of found excel table
     */
    public ExcelTable getTable(int headerRowIndexOnSheet, List<Integer> rowsIndexesInTable, boolean hasEmptyRows, boolean ignoreCase, String... headerColumnsNames) {
        assertThat(headerRowIndexOnSheet).as("Header row number should be greater than 0").isPositive();
        SheetRow headerRow = getRow(headerRowIndexOnSheet);
        assertThat(headerRow.isEmpty()).as("Table header row #%1$s should not be empty on \"%2$s\" sheet", headerRowIndexOnSheet, headerRow.getSheetName()).isFalse();
        List<Integer> columnsIndexesOnSheet = null;
        List<Integer> rowsIndexesOnSheet = rowsIndexesInTable != null ? rowsIndexesInTable.stream().map(r -> r + headerRowIndexOnSheet).collect(Collectors.toList()) : null;

        if (ArrayUtils.isNotEmpty(headerColumnsNames)) {
            List<String> missedHeaderColumnsNames = Stream.of(headerColumnsNames).distinct().collect(Collectors.toList());
            columnsIndexesOnSheet = new ArrayList<>();
            for (SheetCell cell : headerRow) {
                String cellValue = cell.getStringValue();
                if (cellValue == null) {
                    continue;
                }

                Predicate<String> cellValueEqualsToHeaderName = ignoreCase ? cellValue::equalsIgnoreCase : cellValue::equals;
                if (missedHeaderColumnsNames.removeIf(cellValueEqualsToHeaderName)) {
                    columnsIndexesOnSheet.add(cell.getColumnIndex());
                }
            }

            if (!missedHeaderColumnsNames.isEmpty()) {
                throw new ExcelProcessingException(String.format("There are missed header columns %1$s in row number %2$s on sheet \"%3$s\"", missedHeaderColumnsNames, headerRowIndexOnSheet, getSheetName()));
            }
        }

        ExcelTable t = new ExcelTable(headerRow.getPoiRow(), columnsIndexesOnSheet, rowsIndexesOnSheet, hasEmptyRows, this, getCellTypes());
        return addTable(t).getTable(t);
    }

    public ExcelTable addTable(int headerRowIndexOnSheet, String... headerColumnsNames) {
        assertThat(headerColumnsNames).as("Table creation with empty header column names array is not permitted").isNotEmpty();
        Map<Integer, String> headerColumnsIndexesOnSheetAndNames = IntStream.range(0, headerColumnsNames.length).boxed().sorted()
                .collect(Collectors.toMap(i -> i + 1, i -> headerColumnsNames[i], (m1, m2) -> m1, LinkedHashMap::new));
        return addTable(headerRowIndexOnSheet, headerColumnsIndexesOnSheetAndNames);
    }

    public ExcelTable addTable(int headerRowIndexOnSheet, Map<Integer, String> headerColumnsIndexesOnSheetAndNames) {
        assertThat(headerColumnsIndexesOnSheetAndNames.values()).as("Table creation with null or empty header column names is not permitted").isNotEmpty().isNotEmpty();
        Row headerRow = getPoiSheet().createRow(headerRowIndexOnSheet - 1);
        for (Map.Entry<Integer, String> indexAndName : headerColumnsIndexesOnSheetAndNames.entrySet()) {
            headerRow.createCell(indexAndName.getKey() - 1).setCellValue(indexAndName.getValue());
        }

        ExcelTable newTable = new ExcelTable(headerRow, new ArrayList<>(headerColumnsIndexesOnSheetAndNames.keySet()), null, false, this, getCellTypes());
        return addTable(newTable).getTable(newTable);
    }

    protected ExcelSheet addTable(ExcelTable table) {
        if (!getTables().contains(table)) {
            this.tables.add(table);
        }
        return this;
    }

    protected ExcelTable getTable(ExcelTable table) {
        for (ExcelTable t : getTables()) {
            if (t.equals(table)) {
                return t;
            }
        }
        throw new ExcelProcessingException("Internal tables collection does not contain: " + table);
    }
}
