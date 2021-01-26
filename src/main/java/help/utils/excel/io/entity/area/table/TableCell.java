package help.utils.excel.io.entity.area.table;

import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.poi.ss.usermodel.Cell;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelCell;

public class TableCell extends ExcelCell {
    public TableCell(Cell cell, int columnIndexInTable, int columnIndexOnSheet, TableRow tableRow) {
        this(cell, columnIndexInTable, columnIndexOnSheet, tableRow, tableRow.getCellTypes());
    }

    public TableCell(Cell cell, int columnIndexInTable, int columnIndexOnSheet, TableRow tableRow, List<CellType<?>> cellTypes) {
        super(cell, columnIndexInTable, columnIndexOnSheet, tableRow, cellTypes);
    }

    public String getHeaderColumnName() {
        return getRow().getColumnName(getColumnIndex());
    }

    public ExcelTable getTable() {
        return getRow().getTable();
    }

    @Override
    public int getColumnIndexOnSheet() {
        return super.getColumnIndexOnSheet();
    }

    @Override
    public TableRow getRow() {
        return (TableRow) super.getRow();
    }

    @Override
    public String toString() {
        return "TableCell{" +
                "sheetName=" + getSheetName() +
                ", rowIndex=" + getRowIndex() +
                ", rowIndexOnSheet=" + getRowIndexOnSheet() +
                ", columnIndex=" + getColumnIndex() +
                ", columnIndexOnSheet=" + getColumnIndexOnSheet() +
                ", headerColumnName=" + getHeaderColumnName() +
                ", value=\"" + getStringValue() + "\"" +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    @Override
    public ExcelCell delete() {
        //TODO-dchubkov: implement delete ExcelCell and TableCell
        throw new NotImplementedException("Cell deletion is not implemented yet");
    }

    public TableCell copy(int destinationRowIndex, String destinationHeaderColumnName) {
        return copy(destinationRowIndex, destinationHeaderColumnName, true, true, true);
    }

    public TableCell copy(int destinationRowIndex, String destinationHeaderColumnName, boolean copyCellStyle, boolean copyComment, boolean copyHyperlink) {
        return (TableCell) copy(getTable().getCell(destinationRowIndex, destinationHeaderColumnName), copyCellStyle, copyComment, copyHyperlink);
    }
}
