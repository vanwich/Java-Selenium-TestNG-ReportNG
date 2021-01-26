package help.utils.excel.io.entity.area;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.Writable;
import help.utils.excel.io.entity.iterator.RowIterator;

public abstract class ExcelArea <CELL extends ExcelCell, ROW extends ExcelRow<CELL>, COLUMN extends ExcelColumn<CELL>> implements Writable, Iterable<ROW> {
    private final Sheet sheet;
    private final ExcelFile excelFile;

    private List<Integer> columnsIndexesOnSheet;
    private List<Integer> rowsIndexesOnSheet;
    private List<CellType<?>> cellTypes;

    private List<ROW> rows;
    private List<COLUMN> columns;

    protected ExcelArea(Sheet sheet, List<Integer> columnsIndexes, List<Integer> rowsIndexes, ExcelFile excelFile) {
        this(sheet, columnsIndexes, rowsIndexes, excelFile, excelFile.getCellTypes());
    }

    protected ExcelArea(Sheet sheet, List<Integer> columnsIndexesOnSheet, List<Integer> rowsIndexesOnSheet, ExcelFile excelFile, List<CellType<?>> cellTypes) {
        this.sheet = sheet;
        this.columnsIndexesOnSheet = columnsIndexesOnSheet != null ? columnsIndexesOnSheet.stream().distinct().sorted().collect(Collectors.toList()) : getColumnsIndexes(sheet);
        this.rowsIndexesOnSheet = rowsIndexesOnSheet != null ? rowsIndexesOnSheet.stream().distinct().sorted().collect(Collectors.toList()) : getRowsIndexes(sheet);
        this.excelFile = excelFile;
        this.cellTypes = cellTypes.stream().distinct().collect(Collectors.toList());
        this.rows = new ArrayList<>(this.rowsIndexesOnSheet.size());
        this.columns = new ArrayList<>(this.columnsIndexesOnSheet.size());
    }

    public Sheet getPoiSheet() {
        return this.sheet;
    }

    public String getSheetName() {
        return getPoiSheet().getSheetName();
    }

    public List<Integer> getColumnsIndexes() {
        List<Integer> columnsIndexes = new ArrayList<>(getColumnsNumber());
        for (COLUMN column : getColumns()) {
            columnsIndexes.add(column.getIndex());
        }
        return columnsIndexes;
    }

    public List<Integer> getRowsIndexes() {
        List<Integer> rowsIndexes = new ArrayList<>(getRowsNumber());
        for (ROW row : getRows()) {
            rowsIndexes.add(row.getIndex());
        }
        return rowsIndexes;
    }

    public List<CellType<?>> getCellTypes() {
        return Collections.unmodifiableList(this.cellTypes);
    }

    public int getRowsNumber() {
        return getRows().size();
    }

    public int getColumnsNumber() {
        return getColumns().size();
    }

    public int getFirstRowIndex() {
        return getRowsNumber() == 0 ? 0 : getRowsIndexes().get(0);
    }

    public int getLastRowIndex() {
        return getRowsNumber() == 0 ? 0 : getRowsIndexes().get(getRowsNumber() - 1);
    }

    public int getFirstColumnIndex() {
        return getColumnsNumber() == 0 ? 0 : getColumnsIndexes().get(0);
    }

    public int getLastColumnIndex() {
        return getColumnsNumber() == 0 ? 0 : getColumnsIndexes().get(getColumnsNumber() - 1);
    }

    public ROW getFirstRow() {
        int firstRowIndex = getFirstRowIndex();
        return firstRowIndex == 0 ? null : getRow(firstRowIndex);
    }

    public ROW getLastRow() {
        int lastRowIndex = getLastRowIndex();
        return lastRowIndex == 0 ? null : getRow(lastRowIndex);
    }

    public COLUMN getFirstColumn() {
        int firstColumnIndex = getFirstColumnIndex();
        return firstColumnIndex == 0 ? null : getColumn(firstColumnIndex);
    }

    public COLUMN getLastColumn() {
        int lastColumnIndex = getLastColumnIndex();
        return lastColumnIndex == 0 ? null : getColumn(lastColumnIndex);
    }

    public boolean isEmpty() {
        return getRowsNumber() == 0 || getRows().stream().allMatch(ROW::isEmpty);
    }

    public List<Integer> getColumnsIndexesOnSheet() {
        return Collections.unmodifiableList(this.columnsIndexesOnSheet);
    }

    public List<Integer> getRowsIndexesOnSheet() {
        return Collections.unmodifiableList(this.rowsIndexesOnSheet);
    }

    public List<ROW> getRows() {
        if (this.rows.isEmpty()) {
            int rowIndexInArea = 1;
            for (Integer rowIndexesOnSheet : this.rowsIndexesOnSheet) {
                ROW row = createRow(getPoiSheet().getRow(rowIndexesOnSheet - 1), rowIndexInArea, rowIndexesOnSheet);
                rows.add(row);
                rowIndexInArea++;
            }
        }
        return Collections.unmodifiableList(this.rows);
    }

    public List<COLUMN> getColumns() {
        if (this.columns.isEmpty()) {
            int columnIndexInArea = 1;
            for (Integer columnIndexOnSheet : this.columnsIndexesOnSheet) {
                COLUMN column = createColumn(columnIndexInArea, columnIndexOnSheet);
                this.columns.add(column);
                columnIndexInArea++;
            }
        }
        return Collections.unmodifiableList(this.columns);
    }

    protected int getInitialRowIndexOnSheet() {
        return 1;
    }

    @Override
    @Nonnull
    public Iterator<ROW> iterator() {
        return new RowIterator<>(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        List<Boolean> conditions = new ArrayList<>();
        ExcelArea<?, ?, ?> otherArea = (ExcelArea<?, ?, ?>) other;

        return Objects.equals(getPoiSheet().getSheetName(), otherArea.getPoiSheet().getSheetName())
                && Objects.equals(getCellTypes(), otherArea.getCellTypes())
                && Objects.equals(getRowsIndexes(), otherArea.getRowsIndexes())
                && Objects.equals(getColumnsIndexes(), otherArea.getColumnsIndexes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPoiSheet().getSheetName(), getCellTypes(), getRowsIndexes(), getColumnsIndexes());
    }

    @Override
    public ExcelFile getExcelFile() {
        return this.excelFile;
    }

    @Override
    public String toString() {
        return "ExcelArea{" +
                "sheetName=" + getSheetName() +
                ", rowsNumber=" + getRowsNumber() +
                ", columnsNumber=" + getColumnsNumber() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    public ExcelArea<CELL, ROW, COLUMN> registerCellType(List<CellType<?>> cellTypes) {
        this.cellTypes.addAll(cellTypes);
        this.cellTypes = this.cellTypes.stream().distinct().collect(Collectors.toList());
        getRows().forEach(r -> r.registerCellType(cellTypes));
        return this;
    }

    public ROW getRow(int rowIndex) {
        for (ROW row : getRows()) {
            if (row.getIndex() == rowIndex) {
                return row;
            }
        }
        throw new ExcelProcessingException(String.format("There is no row number %1$s int %2$s", rowIndex, this));
    }

    public COLUMN getColumn(int columnIndex) {
        for (COLUMN column : getColumns()) {
            if (column.getIndex() == columnIndex) {
                return column;
            }
        }
        throw new ExcelProcessingException(String.format("There is no column number %1$s in %2$s", columnIndex, this));
    }

    public boolean hasRow(int rowIndex) {
        for (ROW row : getRows()) {
            if (row.getIndex() == rowIndex) {
                return true;
            }
        }
        return false;
    }

    public boolean hasColumn(int columnIndex) {
        for (COLUMN column : getColumns()) {
            if (column.getIndex() == columnIndex) {
                return true;
            }
        }
        return false;
    }

    public CELL getCell(int rowIndex, int columnIndex) {
        return getRow(rowIndex).getCell(columnIndex);
    }

    public Object getValue(int rowIndex, int columnIndex, DateTimeFormatter... dateTimeFormatters) {
        return getCell(rowIndex, columnIndex).getValue(dateTimeFormatters);
    }

    public String getStringValue(int rowIndex, int columnIndex) {
        return getCell(rowIndex, columnIndex).getStringValue();
    }

    public List<String> getRowStringValues(int rowIndex) {
        return getRowStringValues(rowIndex, 1);
    }

    public List<String> getRowStringValues(int rowIndex, int fromColumnIndex) {
        return getRowStringValues(rowIndex, fromColumnIndex, getLastColumnIndex());
    }

    /**
     * Get all String cell values from provided {@code rowIndex} starting inclusively from {@code fromColumnIndex} and up to inclusively {@code toColumnIndex}
     *
     * @param rowIndex row number from which values should be taken, index starts from 1
     * @param fromColumnIndex the inclusive initial column number on sheet to get values from. Should be positive, index starts from 1
     * @param toColumnIndex the inclusive last column number on sheet to get values from. Should be greater or equal to {@code fromColumnIndex}
     *
     * @return List of String cell values found on provided {@code rowIndex} within {@code fromColumnIndex/toColumnIndex} bounds
     */
    public List<String> getRowStringValues(int rowIndex, int fromColumnIndex, int toColumnIndex) {
        assertThat(fromColumnIndex).as("Start column index should be greater than 0").isPositive();
        assertThat(toColumnIndex).as("End column index should be greater or equal to start column index").isGreaterThanOrEqualTo(fromColumnIndex);
        return IntStream.rangeClosed(fromColumnIndex, toColumnIndex).filter(getRow(rowIndex)::hasCell).mapToObj(getRow(rowIndex)::getStringValue).collect(Collectors.toList());
    }

    public List<Object> getColumnStringValues(int columnIndex) {
        return getColumnStringValues(columnIndex, 1);
    }

    public List<Object> getColumnStringValues(int columnIndex, int fromRowIndex) {
        return getColumnStringValues(columnIndex, fromRowIndex, getLastRowIndex());
    }

    /**
     * Get all String cell values from provided {@code columnIndex} starting inclusively from {@code fromRowIndex} and up to inclusively {@code toRowIndex}
     *
     * @param columnIndex column number from which values should be taken, index starts from 1
     * @param fromRowIndex the inclusive initial row number on sheet to get values from. Should be positive, index starts from 1
     * @param toRowIndex the inclusive last row number on sheet to get values from. Should be greater or equal to {@code fromRowIndex}
     *
     * @return List of String cell values found on provided {@code columnIndex} within {@code fromRowIndex/toRowIndex} bounds
     */
    public List<Object> getColumnStringValues(int columnIndex, int fromRowIndex, int toRowIndex) {
        assertThat(fromRowIndex).as("Start row index should be greater than 0").isPositive();
        assertThat(toRowIndex).as("End row index should be greater or equal to start row index").isGreaterThanOrEqualTo(fromRowIndex);
        return IntStream.rangeClosed(fromRowIndex, toRowIndex).filter(getColumn(columnIndex)::hasCell).mapToObj(getColumn(columnIndex)::getValue).collect(Collectors.toList());
    }

    public ROW getRow(String... valuesInCells) {
        return getRow(false, valuesInCells);
    }

    public ROW getRow(boolean ignoreCase, String... valuesInCells) {
        Set<String> expectedCellValues = new HashSet<>(Arrays.asList(valuesInCells));
        Pair<Integer, List<String>> bestMatchRowNumberWithMissedValues = null;
        for (ROW row : getRows()) {
            List<String> cellValues = row.getStringValues();
            List<String> missedCellValues = new ArrayList<>(expectedCellValues);

            for (String cellValue : cellValues) {
                if (cellValue == null) {
                    continue;
                }
                Predicate<String> cellValueEqualsToExpectedValue = ignoreCase ? cellValue::equalsIgnoreCase : cellValue::equals;
                missedCellValues.removeIf(cellValueEqualsToExpectedValue);
                if (missedCellValues.isEmpty()) {
                    return row;
                }
            }

            if (bestMatchRowNumberWithMissedValues == null || bestMatchRowNumberWithMissedValues.getRight().size() > missedCellValues.size()) {
                bestMatchRowNumberWithMissedValues = Pair.of(row.getIndex(), missedCellValues);
            }
        }

        String errorMessage = String.format("Unable to find row with all these values: %1$s in %2$s", expectedCellValues, this);
        if (bestMatchRowNumberWithMissedValues.getRight().size() < expectedCellValues.size()) {
            errorMessage = String.format("%1$s\nBest match was found in row #%2$s with missed cell values: %3$s",
                    errorMessage, bestMatchRowNumberWithMissedValues.getLeft(), bestMatchRowNumberWithMissedValues.getRight());
        }

        throw new ExcelProcessingException(errorMessage);
    }

    public ExcelArea<CELL, ROW, COLUMN> excludeColumns(Integer... columnsIndexes) {
        List<Integer> columnsIndexesToExclude = Arrays.asList(columnsIndexes);
        //TODO-dchubkov: replace with forEach and throw exception with exact missing index
        assertThat(columnsIndexes).as("Can't exclude columns with indexes %s", columnsIndexesToExclude).allMatch(this::hasColumn);

        getRows().forEach(r -> r.removeCellsIndexes(columnsIndexes));
        for (Integer columnIndex : columnsIndexesToExclude) {
            this.columnsIndexesOnSheet.remove(new Integer(getColumn(columnIndex).getIndexOnSheet()));
        }
        this.columns.removeIf(c -> columnsIndexesToExclude.contains(c.getIndex()));
        return this;
    }

    public ExcelArea<CELL, ROW, COLUMN> excludeRows(Integer... rowsIndexes) {
        List<Integer> rowsIndexesToExclude = Arrays.asList(rowsIndexes);
        //TODO-dchubkov: replace with forEach and throw exception with exact missing index
        assertThat(rowsIndexes).as("Can't exclude rows with indexes %s", rowsIndexesToExclude).allMatch(this::hasRow);

        getColumns().forEach(c -> c.removeCellsIndexes(rowsIndexes));
        for (Integer rowIndex : rowsIndexesToExclude) {
            this.rowsIndexesOnSheet.remove(new Integer(getRow(rowIndex).getIndexOnSheet()));
        }
        this.rows.removeIf(r -> rowsIndexesToExclude.contains(r.getIndex()));
        return this;
    }

    public ExcelArea<CELL, ROW, COLUMN> clearColumns(Integer... columnsIndexes) {
        for (ROW row : getRows()) {
            for (Integer index : columnsIndexes) {
                row.getCell(index).clear();
            }
        }
        return this;
    }

    public ExcelArea<CELL, ROW, COLUMN> clearRows(Integer... rowsIndexes) {
        for (Integer index : rowsIndexes) {
            getRow(index).clear();
        }
        return this;
    }

    public ExcelArea<CELL, ROW, COLUMN> copyColumn(int columnIndex, int destinationColumnIndex) {
        for (ROW row : getRows()) {
            row.getCell(columnIndex).copy(row.getIndex(), row.getCell(destinationColumnIndex).getColumnIndex());
        }
        return this;
    }

    public ExcelArea<CELL, ROW, COLUMN> copyRow(int rowIndex, int destinationRowIndex) {
        getRow(rowIndex).copy(destinationRowIndex);
        return this;
    }

    public ExcelArea<CELL, ROW, COLUMN> deleteColumns(Integer... columnsIndexes) {
        //TODO-dchubkov: implement delete columns
        throw new NotImplementedException("Columns deletion is not implemented yet");
    }

    public ExcelArea<CELL, ROW, COLUMN> deleteRows(Integer... rowsIndexes) {
        int rowsShifts = 0;
        List<Integer> uniqueSortedRowIndexes = Arrays.stream(rowsIndexes).distinct().sorted().collect(Collectors.toList());
        Sheet sheet = getPoiSheet();
        for (int index : uniqueSortedRowIndexes) {
            assertThat(hasRow(index - rowsShifts)).as("There is no row number %1$s in %2$s", index, this).isTrue();
            sheet.shiftRows(index - rowsShifts, sheet.getLastRowNum(), -1);
            rowsShifts++;
        }
        excludeRows(rowsIndexes);
        return this;
    }

    public ExcelArea<CELL, ROW, COLUMN> addRows(int numberOfRows) {
        for (int i = 0; i < numberOfRows; i++) {
            addRow();
        }
        return this;
    }

    public ROW addRow() {
        ROW lastRow = getLastRow();
        int newRowIndex = lastRow == null ? 1 : lastRow.getIndex() + 1;
        int newRowIndexOnSheet = lastRow == null ? getInitialRowIndexOnSheet() : lastRow.getIndexOnSheet() + 1;
        Row poiRow = getPoiSheet().getRow(newRowIndexOnSheet - 1);
        ROW newRow = createRow(poiRow, newRowIndex, newRowIndexOnSheet);
        return addRow(newRow);
    }

    protected COLUMN addColumn(COLUMN newColumn) {
        getColumns(); //to initialize existing columns
        this.columns.add(newColumn);
        this.columnsIndexesOnSheet.add(newColumn.getIndexOnSheet());
        return newColumn;
    }

    protected ROW addRow(ROW newRow) {
        getRows(); //to initialize existing columns
        this.rows.add(newRow);
        this.rowsIndexesOnSheet.add(newRow.getIndexOnSheet());
        return newRow;
    }

    protected abstract ROW createRow(Row row, int rowIndexInArea, int rowIndexOnSheet);

    protected abstract COLUMN createColumn(int columnIndexInArea, int columnIndexOnSheet);

    private List<Integer> getColumnsIndexes(Sheet sheet) {
        int maxCellsNumber = 0;
        for (Row row : sheet) {
            if (row.getLastCellNum() > maxCellsNumber) {
                maxCellsNumber = row.getLastCellNum();
            }
        }
        if (maxCellsNumber == 0) {
            return new ArrayList<>();
        }
        return IntStream.rangeClosed(1, maxCellsNumber).boxed().collect(Collectors.toList());
    }

    private List<Integer> getRowsIndexes(Sheet sheet) {
        if (sheet.getLastRowNum() == 0) {
            return new ArrayList<>();
        }
        return IntStream.rangeClosed(1, sheet.getLastRowNum() + 1).boxed().collect(Collectors.toList());
    }
}
