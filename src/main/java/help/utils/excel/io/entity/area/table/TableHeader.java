package help.utils.excel.io.entity.area.table;

import java.util.Collections;
import java.util.List;
import javax.ws.rs.NotSupportedException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelCell;

public class TableHeader extends TableRow{
    public TableHeader(Row headerRow, List<Integer> columnsIndexesOnSheet, ExcelTable table) {
        super(headerRow, 0, headerRow.getRowNum() + 1, columnsIndexesOnSheet, table, Collections.singletonList(ExcelCell.STRING_TYPE));
    }

    public List<String> getColumnsNames() {
        return getStringValues();
    }

    @Override
    protected HeaderCell createCell(int columnIndexInTable, int columnIndexOnSheet) {
        Cell poiCell = getPoiRow() != null ? getPoiRow().getCell(columnIndexOnSheet - 1) : null;
        return new HeaderCell(poiCell, columnIndexInTable, columnIndexOnSheet, this);
    }

    @Override
    public String toString() {
        return "TableHeader{" +
                "sheetName=" + getSheetName() +
                ", headerRowIndexOnSheet=" + getIndexOnSheet() +
                ", headerColumns=" + getColumnsNames() +
                ", columnsNumber=" + getCellsNumber() +
                '}';
    }

    @Override
    public boolean hasColumn(String headerColumnName) {
        return hasColumn(headerColumnName, false);
    }

    @Override
    public boolean hasColumn(String headerColumnName, boolean ignoreCase) {
        for (String columnName : getColumnsNames()) {
            if (ignoreCase ? columnName.equalsIgnoreCase(headerColumnName) : columnName.equals(headerColumnName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TableCell getCell(String headerColumnName) {
        return getCell(headerColumnName, false);
    }

    @Override
    public TableCell getCell(String headerColumnName, boolean ignoreCase) {
        for (TableCell cell : getCells()) {
            if (cell.hasStringValue(headerColumnName, ignoreCase)) {
                return cell;
            }
        }
        throw new IllegalStateException(String.format("There is no column name \"%1$s\" in the table %2$s", headerColumnName, getTable()));
    }

    @Override
    public String getColumnName(int columnIndex) {
        return getCell(columnIndex).getStringValue();
    }

    @Override
    public ExcelTable exclude() {
        throw new NotSupportedException("Excluding of table header's row is not supported");
    }

    @Override
    public TableHeader clear() {
        throw new NotSupportedException("Clearing of table header's row is not supported");
    }

    @Override
    public ExcelTable delete() {
        throw new NotSupportedException("Deletion of table header's row is not supported");
    }

    @Override
    public TableHeader registerCellType(List<CellType<?>> cellTypes) {
        if (cellTypes.stream().anyMatch(t -> !ExcelCell.STRING_TYPE.equals(t))) {
            throw new NotSupportedException("Table header row does not support non string cell types");
        }
        return (TableHeader) super.registerCellType(cellTypes);
    }

    @Override
    protected void removeCellsIndexes(Integer... columnIndexesInRow) {
        super.removeCellsIndexes(columnIndexesInRow);
    }

    public boolean hasColumn(int columnIndex) {
        return hasCell(columnIndex);
    }

    public int getColumnIndex(String headerColumnName) {
        return getColumnIndex(headerColumnName, false);
    }

    public int getColumnIndex(String headerColumnName, boolean ignoreCase) {
        return getCell(headerColumnName, ignoreCase).getColumnIndex();
    }

    public int getColumnIndexOnSheet(String headerColumnName) {
        return getColumnIndexOnSheet(headerColumnName, false);
    }

    public int getColumnIndexOnSheet(String headerColumnName, boolean ignoreCase) {
        return getCell(headerColumnName, ignoreCase).getColumnIndexOnSheet();
    }

    public int getColumnIndexOnSheet(int columnIndex) {
        return getCell(columnIndex).getColumnIndexOnSheet();
    }

    protected TableHeader addColumn(TableColumn column, String columnName) {
        HeaderCell headerCell = createCell(column.getIndex(), column.getIndexOnSheet());
        headerCell.setValue(columnName, ExcelCell.STRING_TYPE);
        addCell(headerCell);
        return this;
    }
}
