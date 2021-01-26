package help.utils.excel.io.entity.area;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.celltype.CellType;

public abstract class ExcelRow<CELL extends ExcelCell> extends CellsQueue<CELL> {
    private Row row;

    protected ExcelRow(Row row, int rowIndexInArea, int rowIndexOnSheet, List<Integer> columnsIndexesOnSheet, ExcelArea<CELL, ?, ?> excelArea) {
        this(row, rowIndexInArea, rowIndexOnSheet, columnsIndexesOnSheet, excelArea, excelArea.getCellTypes());
    }

    protected ExcelRow(Row row, int rowIndexInArea, int rowIndexOnSheet, List<Integer> columnsIndexesOnSheet, ExcelArea<CELL, ?, ?> excelArea, List<CellType<?>> cellTypes) {
        super(rowIndexInArea, rowIndexOnSheet, columnsIndexesOnSheet, excelArea, cellTypes);
        this.row = row;
    }

    public Row getPoiRow() {
        return this.row;
    }

    void setPoiRow(Row row) {
        this.row = row;
    }

    @Override
    public List<Integer> getCellsIndexes() {
        return getCells().stream().map(ExcelCell::getColumnIndex).collect(Collectors.toList());
    }

    @Override
    public List<CELL> getCellsByIndexes(List<Integer> columnsIndexesInRow) {
        return getCells().stream().filter(c -> columnsIndexesInRow.contains(c.getColumnIndex())).collect(Collectors.toList());
    }

    @Override
    public boolean hasCell(int columnIndexInRow) {
        for (CELL cell : getCells()) {
            if (cell.getColumnIndex() == columnIndexInRow) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CELL getCell(int columnIndexInRow) {
        for (CELL cell : getCells()) {
            if (cell.getColumnIndex() == columnIndexInRow) {
                return cell;
            }
        }
        throw new ExcelProcessingException(String.format("There is no column with %1$s index in %2$s", columnIndexInRow, this));
    }

    @Override
    public boolean isEmpty() {
        return getPoiRow() == null || getPoiRow().getLastCellNum() <= 0 || super.isEmpty();
    }

    @Override
    public ExcelArea<CELL, ?, ?> exclude() {
        return getArea().excludeRows(getIndex());
    }

    @Override
    public ExcelRow<CELL> copy(int destinationRowIndex) {
        for (CELL cell : getCells()) {
            cell.copy(destinationRowIndex, cell.getColumnIndex());
        }
        return this;
    }

    @Override
    public ExcelArea<CELL, ?, ?> delete() {
        return getArea().deleteRows(getIndex());
    }

    @Override
    public String toString() {
        return "ExcelRow{" +
                "sheetName=" + getSheetName() +
                ", rowIndex=" + getIndex() +
                ", columnsNumber=" + getCellsNumber() +
                ", values=" + getStringValues() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    @Override
    protected Integer getCellIndexOnSheet(Integer columnIndexInRow) {
        return getCell(columnIndexInRow).getColumnIndexOnSheet();
    }

    @Override
    protected void removeCellsIndexes(Integer... columnIndexesInRow) {
        super.removeCellsIndexes(columnIndexesInRow);
        this.cells.removeIf(c -> Arrays.asList(columnIndexesInRow).contains(c.getColumnIndex()));
    }
}
