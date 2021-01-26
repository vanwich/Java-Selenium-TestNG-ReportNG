package help.utils.excel.io.entity.area;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.celltype.CellType;

public abstract class ExcelColumn <CELL extends ExcelCell> extends CellsQueue<CELL>{
    protected ExcelColumn(int columnIndexInArea, int columnIndexOnSheet, List<Integer> rowsIndexesOnSheet, ExcelArea<CELL, ?, ?> excelArea) {
        this(columnIndexInArea, columnIndexOnSheet, rowsIndexesOnSheet, excelArea, excelArea.getCellTypes());
    }

    protected ExcelColumn(int columnIndexInArea, int columnIndexOnSheet, List<Integer> rowsIndexesOnSheet, ExcelArea<CELL, ?, ?> excelArea, List<CellType<?>> cellTypes) {
        super(columnIndexInArea, columnIndexOnSheet, rowsIndexesOnSheet, excelArea, cellTypes);
    }

    @Override
    public List<Integer> getCellsIndexes() {
        return getCells().stream().map(ExcelCell::getRowIndex).collect(Collectors.toList());
    }

    @Override
    public List<CELL> getCellsByIndexes(List<Integer> rowsIndexesInColumn) {
        return getCells().stream().filter(c -> rowsIndexesInColumn.contains(c.getRowIndex())).collect(Collectors.toList());
    }

    @Override
    public boolean hasCell(int rowIndexInColumn) {
        for (CELL cell : getCells()) {
            if (cell.getRowIndex() == rowIndexInColumn) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CELL getCell(int rowIndexInColumn) {
        for (CELL cell : getCells()) {
            if (cell.getRowIndex() == rowIndexInColumn) {
                return cell;
            }
        }
        throw new ExcelProcessingException(String.format("There is no row with %1$s index in %2$s", rowIndexInColumn, this));
    }

    @Override
    public ExcelArea<CELL, ?, ?> exclude() {
        return getArea().excludeColumns(getIndex());
    }

    @Override
    public ExcelColumn<CELL> copy(int destinationColumnIndex) {
        for (CELL cell : getCells()) {
            cell.copy(cell.getRowIndex(), destinationColumnIndex);
        }
        return this;
    }

    @Override
    public ExcelArea<CELL, ?, ?> delete() {
        return getArea().deleteColumns(getIndex());
    }

    @Override
    protected CELL createCell(int rowIndex, int rowIndexOnSheet) {
        CELL cell;
        if (getArea().getRow(rowIndex).hasCell(getIndex())) {
            cell = getArea().getRow(rowIndex).getCell(getIndex());
        } else {
            cell = getArea().getRow(rowIndex).createCell(getIndex(), getIndexOnSheet());
            getArea().getRow(rowIndex).addCell(cell);
        }
        return cell;
    }

    @Override
    public String toString() {
        return "ExcelColumn{" +
                "sheetName=" + getSheetName() +
                ", columnIndex=" + getIndex() +
                ", rowsNumber=" + getCellsNumber() +
                ", values=" + getStringValues() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    @Override
    protected Integer getCellIndexOnSheet(Integer rowIndexInColumn) {
        return getCell(rowIndexInColumn).getRowIndexOnSheet();
    }

    @Override
    protected void removeCellsIndexes(Integer... rowIndexesInColumn) {
        super.removeCellsIndexes(rowIndexesInColumn);
        this.cells.removeIf(c -> Arrays.asList(rowIndexesInColumn).contains(c.getRowIndex()));
    }
}
