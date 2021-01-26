package help.utils.excel.io.entity.area.table;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelArea;
import help.utils.excel.io.entity.area.sheet.ExcelSheet;

public class ExcelTable extends ExcelArea<TableCell, TableRow, TableColumn> {
    private final Row headerRow;
    private final ExcelSheet excelSheet;

    private TableHeader header;

    public ExcelTable(Row headerRow, ExcelSheet sheet) {
        this(headerRow, sheet, sheet.getCellTypes());
    }

    public ExcelTable(Row headerRow, ExcelSheet sheet, List<CellType<?>> cellTypes) {
        this(headerRow, null, sheet, cellTypes);
    }

    public ExcelTable(Row headerRow, List<Integer> columnsIndexesOnSheet, ExcelSheet sheet, List<CellType<?>> cellTypes) {
        this(headerRow, columnsIndexesOnSheet, null, false, sheet, cellTypes);
    }

    public ExcelTable(Row headerRow, List<Integer> columnsIndexesOnSheet, List<Integer> rowsIndexesOnSheet, boolean hasEmptyRows, ExcelSheet excelSheet, List<CellType<?>> cellTypes) {
        super(excelSheet.getPoiSheet(),
                columnsIndexesOnSheet != null ? columnsIndexesOnSheet : getHeaderColumnsIndexes(headerRow),
                rowsIndexesOnSheet != null ? rowsIndexesOnSheet : getTableRowsIndexes(headerRow, columnsIndexesOnSheet, hasEmptyRows),
                excelSheet.getExcelFile(), cellTypes);
        this.headerRow = headerRow;
        this.excelSheet = excelSheet;
    }

    public TableHeader getHeader() {
        if (this.header == null) {
            this.header = new TableHeader(this.headerRow, getColumnsIndexesOnSheet(), this);
        }
        return this.header;
    }

    public ExcelSheet getSheet() {
        return this.excelSheet;
    }

    public List<Map<String, Object>> getValues() {
        List<Map<String, Object>> values = new ArrayList<>(getRowsNumber());
        for (TableRow row : getRows()) {
            values.add(row.getTableValues());
        }
        return values;
    }

    public List<Map<String, String>> getStringValues() {
        List<Map<String, String>> values = new ArrayList<>(getRowsNumber());
        for (TableRow row : getRows()) {
            values.add(row.getTableStringValues());
        }
        return values;
    }

    public List<String> getColumnsNames() {
        return getHeader().getColumnsNames();
    }

    @Override
    protected int getInitialRowIndexOnSheet() {
        return getHeader().getIndexOnSheet() + 1;
    }

    private static List<Integer> getHeaderColumnsIndexes(Row headerRow) {
        List<Integer> columnsIndexes = new ArrayList<>();
        for (Cell cell : headerRow) {
            if (cell != null && cell.getCellTypeEnum() == org.apache.poi.ss.usermodel.CellType.STRING && StringUtils.isNotBlank(cell.getStringCellValue())) {
                columnsIndexes.add(cell.getColumnIndex() + 1);
            }
        }

        assertThat(columnsIndexes)
                .as("There are no non-empty String columns in header row number %1$s on sheet \"%1$s\"", headerRow.getRowNum() + 1, headerRow.getSheet().getSheetName())
                .isNotEmpty();
        return columnsIndexes;
    }

    private static List<Integer> getTableRowsIndexes(Row headerRow, List<Integer> columnsIndexesOnSheet, boolean hasEmptyRows) {
        List<Integer> rIndexes = new ArrayList<>();
        List<Integer> cIndexes = columnsIndexesOnSheet != null ? columnsIndexesOnSheet : getHeaderColumnsIndexes(headerRow);

        for (int rowIndex = headerRow.getRowNum() + 1; rowIndex <= headerRow.getSheet().getLastRowNum(); rowIndex++) {
            if (!hasEmptyRows && isRowEmpty(headerRow.getSheet().getRow(rowIndex), cIndexes)) {
                break;
            }
            rIndexes.add(rowIndex + 1);
        }
        return rIndexes;
    }

    private static boolean isRowEmpty(Row row, Collection<Integer> cellsIndexes) {
        if (row == null || row.getLastCellNum() <= 0) {
            return true;
        }
        for (Cell cell : row) {
            if (cellsIndexes.contains(cell.getColumnIndex() + 1) && cell != null && cell.getCellTypeEnum() != org.apache.poi.ss.usermodel.CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ExcelTable addRows(int numberOfRows) {
        return (ExcelTable) super.addRows(numberOfRows);
    }

    @Override
    protected TableRow createRow(Row row, int rowIndexInTable, int rowIndexOnSheet) {
        return new TableRow(row, rowIndexInTable, rowIndexOnSheet, getColumnsIndexesOnSheet(), this, getCellTypes());
    }

    @Override
    protected TableColumn createColumn(int columnIndexInTable, int columnIndexOnSheet) {
        return new TableColumn(columnIndexInTable, columnIndexOnSheet, getRowsIndexesOnSheet(), this, getCellTypes());
    }

    @Override
    public String toString() {
        return "ExcelTable{" +
                "sheetName=" + getPoiSheet().getSheetName() +
                ", headerColumnNames=" + getHeader().getColumnsNames() +
                ", rowsNumber=" + getRowsNumber() +
                ", columnsNumber=" + getColumnsNumber() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    @Override
    public ExcelTable deleteRows(Integer... rowsIndexes) {
        int rowsShifts = 0;
        List<Integer> uniqueSortedRowIndexes = Arrays.stream(rowsIndexes).distinct().sorted().collect(Collectors.toList());
        for (int index : uniqueSortedRowIndexes) {
            assertThat(hasRow(index - rowsShifts)).as("There is no row number %1$s in table %2$s", index, this).isTrue();
            ListIterator<Integer> rowsIterator = new ArrayList<>(getRowsIndexes()).listIterator(index - rowsShifts);
            while (rowsIterator.hasNext()) {
                TableRow nextRow = getRow(rowsIterator.next());
                TableRow currentRow = getRow(rowsIterator.previousIndex());
                nextRow.copy(currentRow.getIndex());
            }
            clearRows(rowsIterator.nextIndex());
            excludeRows(rowsIterator.nextIndex());
            rowsShifts++;
        }
        return this;
    }

    @Override
    public ExcelTable excludeColumns(Integer... columnsIndexes) {
        super.excludeColumns(columnsIndexes);
        getHeader().removeCellsIndexes(columnsIndexes);
        return this;
    }

    public ExcelTable addColumns(String... headerColumnNames) {
        for (String columnName : headerColumnNames) {
            TableColumn lastColumn = getLastColumn();
            int newColumnIndex = lastColumn == null ? 1 : lastColumn.getIndex() + 1;
            int newColumnIndexOnSheet = lastColumn == null ? 1 : lastColumn.getIndexOnSheet() + 1;
            TableColumn newColumn = createColumn(newColumnIndex, newColumnIndexOnSheet);
            getHeader().addColumn(newColumn, columnName);
            addColumn(newColumn);
            for (TableRow row : getRows()) {
                TableCell cell = row.createCell(newColumnIndex, newColumnIndexOnSheet);
                row.addCell(cell);
            }
        }
        return this;
    }

    public ExcelTable excludeColumns(String... headerColumnNames) {
        Integer[] columnsIndexes = Arrays.stream(headerColumnNames).map(this::getColumnIndex).toArray(Integer[]::new);
        return excludeColumns(columnsIndexes);
    }

    public int getColumnIndex(String headerColumnName) {
        return getColumnIndex(headerColumnName, false);
    }

    public int getColumnIndex(String headerColumnName, boolean ignoreCase) {
        return getHeader().getColumnIndex(headerColumnName, ignoreCase);
    }

    public boolean hasColumn(String headerColumnName) {
        return hasColumn(headerColumnName, false);
    }

    public boolean hasColumn(String headerColumnName, boolean ignoreCase) {
        return getHeader().hasColumn(headerColumnName, ignoreCase);
    }

    public TableColumn getColumn(String headerColumnName) {
        return getColumn(headerColumnName, false);
    }

    public TableColumn getColumn(String headerColumnName, boolean ignoreCase) {
        return getColumn(getColumnIndex(headerColumnName, ignoreCase));
    }

    public TableRow getRow(String headerColumnName, Object cellValue) {
        return getRow(headerColumnName, false, cellValue);
    }

    public TableRow getRow(String headerColumnName, boolean ignoreHeaderColumnCase, Object cellValue) {
        for (TableRow row : getRows()) {
            if (row.hasValue(headerColumnName, ignoreHeaderColumnCase, cellValue)) {
                return row;
            }
        }
        throw new ExcelProcessingException(String.format("There are no rows in table with value \"%1$s\" in column \"%2$s\"", cellValue, headerColumnName));
    }

    public List<TableRow> getRows(String headerColumnName, Object cellValue) {
        return getRows(headerColumnName, false, cellValue);
    }

    public List<TableRow> getRows(String headerColumnName, boolean ignoreHeaderColumnCase, Object cellValue) {
        List<TableRow> foundRows = getRows().stream().filter(r -> r.hasValue(headerColumnName, ignoreHeaderColumnCase, cellValue)).collect(Collectors.toList());
        assertThat(foundRows).as("There are no rows in table with value \"%1$s\" in column \"%2$s\"", cellValue, headerColumnName).isNotEmpty();
        return foundRows;
    }

    public TableRow getRow(Map<String, Object> query) {
        for (TableRow row : getRows()) {
            boolean searchInNextRow = false;

            for (Map.Entry<String, Object> columnNameAndCellValue : query.entrySet()) {
                if (!row.hasValue(columnNameAndCellValue.getKey(), columnNameAndCellValue.getValue())) {
                    searchInNextRow = true;
                    break;
                }
            }

            if (!searchInNextRow) {
                return row;
            }
        }
        throw new ExcelProcessingException("There is no row in table with column names and cell values query: " + query.entrySet());
    }

    public List<TableRow> getRows(Map<String, Object> query) {
        List<TableRow> foundRows = new ArrayList<>(getRows());
        for (Map.Entry<String, Object> columnNameAndCellValue : query.entrySet()) {
            foundRows.removeIf(r -> !r.hasValue(columnNameAndCellValue.getKey(), columnNameAndCellValue.getValue()));
        }
        assertThat(foundRows).as("There are no rows in table with column names and cell values query: " + query.entrySet()).isNotEmpty();
        return foundRows;
    }

    public TableCell getCell(int rowIndex, String headerColumnName) {
        return getCell(rowIndex, headerColumnName, false);
    }

    public TableCell getCell(int rowIndex, String headerColumnName, boolean ignoreCase) {
        return getRow(rowIndex).getCell(headerColumnName, ignoreCase);
    }

    public Object getValue(int rowIndex, String headerColumnName) {
        return getValue(rowIndex, headerColumnName, false);
    }

    public Object getValue(int rowIndex, String headerColumnName, boolean ignoreCase) {
        return getCell(rowIndex, headerColumnName, ignoreCase).getValue();
    }

    public String getStringValue(int rowIndex, String headerColumnName) {
        return getStringValue(rowIndex, headerColumnName, false);
    }

    public String getStringValue(int rowIndex, String headerColumnName, boolean ignoreCase) {
        return getCell(rowIndex, headerColumnName, ignoreCase).getStringValue();
    }

    public ExcelTable clearRow(String headerColumnName, Object cellValue) {
        return ((TableRow) getRow(headerColumnName, cellValue).clear()).getTable();
    }

    public ExcelTable clearColumns(String... headerColumnNames) {
        return (ExcelTable) clearColumns(Arrays.stream(headerColumnNames).map(this::getColumnIndex).toArray(Integer[]::new));
    }

    public ExcelTable copyColumn(String headerColumnName, String destinationHeaderColumnName) {
        return (ExcelTable) copyColumn(getColumnIndex(headerColumnName), getHeader().getColumnIndex(destinationHeaderColumnName));
    }

    public ExcelTable deleteColumns(String... headerColumnNames) {
        //TODO-dchubkov: implement delete columns by names
        throw new NotImplementedException("Columns deletion by header column names is not implemented yet");
    }

    public ExcelTable deleteRows(String headerColumnName, Object cellValue) {
        List<TableRow> rowsToDelete = getRows(headerColumnName, cellValue);
        return deleteRows(rowsToDelete.stream().map(TableRow::getIndex).toArray(Integer[]::new));
    }
}
