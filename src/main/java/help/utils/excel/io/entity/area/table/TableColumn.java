package help.utils.excel.io.entity.area.table;

import java.util.List;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelColumn;

public class TableColumn extends ExcelColumn<TableCell> {
    public TableColumn(int columnIndexInTable, int columnIndexOnSheet, List<Integer> rowsIndexesOnSheet, ExcelTable table) {
        this(columnIndexInTable, columnIndexOnSheet, rowsIndexesOnSheet, table, table.getCellTypes());
    }

    public TableColumn(int columnIndexInTable, int columnIndexOnSheet, List<Integer> rowsIndexesOnSheet, ExcelTable table, List<CellType<?>> cellTypes) {
        super(columnIndexInTable, columnIndexOnSheet, rowsIndexesOnSheet, table, cellTypes);
    }

    public ExcelTable getTable() {
        return (ExcelTable) getArea();
    }

    public String getHeaderName() {
        return getTable().getHeader().getColumnName(getIndex());
    }

    @Override
    public String toString() {
        return "TableColumn{" +
                "sheetName=" + getSheetName() +
                ", columnIndex=" + getIndex() +
                ", columnIndexOnSheet=" + getIndexOnSheet() +
                ", headerColumnName=" + getHeaderName() +
                ", rowsNumber=" + getCellsNumber() +
                ", values=" + getStringValues() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    public TableColumn copy(String destinationHeaderColumnName) {
        return copy(destinationHeaderColumnName, false);
    }

    public TableColumn copy(String destinationHeaderColumnName, boolean ignoreCase) {
        return (TableColumn) copy(getTable().getColumnIndex(destinationHeaderColumnName, ignoreCase));
    }
}
