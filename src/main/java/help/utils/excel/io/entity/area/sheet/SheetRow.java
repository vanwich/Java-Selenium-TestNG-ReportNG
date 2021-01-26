package help.utils.excel.io.entity.area.sheet;

import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelRow;

public class SheetRow extends ExcelRow<SheetCell> {
    public SheetRow(Row row, int rowIndexOnSheet, List<Integer> columnsIndexesOnSheet, ExcelSheet sheet) {
        this(row, rowIndexOnSheet, columnsIndexesOnSheet, sheet, sheet.getCellTypes());
    }

    public SheetRow(Row row, int rowIndexOnSheet, List<Integer> columnsIndexesOnSheet, ExcelSheet sheet, List<CellType<?>> cellTypes) {
        super(row, rowIndexOnSheet, rowIndexOnSheet, columnsIndexesOnSheet, sheet, cellTypes);
    }

    public ExcelSheet getSheet() {
        return (ExcelSheet) getArea();
    }

    @Override
    protected SheetCell createCell(int columnIndex, int columnIndexOnSheet) {
        Cell poiCell = getPoiRow() != null ? getPoiRow().getCell(columnIndexOnSheet - 1) : null;
        return new SheetCell(poiCell, columnIndexOnSheet, this, getCellTypes());
    }

    @Override
    protected void addCell(SheetCell cell) {
        super.addCell(cell);
    }
}
