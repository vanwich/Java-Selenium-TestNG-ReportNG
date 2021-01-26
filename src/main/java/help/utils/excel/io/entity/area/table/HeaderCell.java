package help.utils.excel.io.entity.area.table;

import java.util.List;
import javax.ws.rs.NotSupportedException;
import org.apache.poi.ss.usermodel.Cell;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelCell;

public class HeaderCell extends TableCell{
    public HeaderCell(Cell cell, int columnIndexInTable, int columnIndexOnSheet, TableHeader header) {
        super(cell, columnIndexInTable, columnIndexOnSheet, header, header.getCellTypes());
    }

    @Override
    public HeaderCell clear() {
        throw new NotSupportedException("Clearing of table header's cell is not supported");
    }

    @Override
    public HeaderCell delete() {
        throw new NotSupportedException("Deletion of table header's cell is not supported");
    }

    @Override
    public HeaderCell registerCellType(List<CellType<?>> cellTypes) {
        if (cellTypes.stream().anyMatch(t -> !ExcelCell.STRING_TYPE.equals(t))) {
            throw new NotSupportedException("Table header's cell does not support non string cell types");
        }
        return (HeaderCell) super.registerCellType(cellTypes);
    }
}
