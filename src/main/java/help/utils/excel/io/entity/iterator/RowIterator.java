package help.utils.excel.io.entity.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import help.utils.excel.io.entity.area.ExcelArea;
import help.utils.excel.io.entity.area.ExcelCell;
import help.utils.excel.io.entity.area.ExcelRow;

public class RowIterator <ROW extends ExcelRow<? extends ExcelCell>> implements Iterator<ROW> {
    private ExcelArea<?, ROW, ?> excelArea;
    private List<Integer> rowsIndexes;
    private Integer currentIndex;

    public RowIterator(ExcelArea<?, ROW, ?> excelArea) {
        this.excelArea = excelArea;
        this.rowsIndexes = new ArrayList<>(excelArea.getRowsIndexes());
        this.currentIndex = excelArea.getFirstRowIndex();
    }

    @Override
    public boolean hasNext() {
        return excelArea.hasRow(currentIndex);
    }

    @Override
    public ROW next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There is no next row");
        }
        ROW returnRow = excelArea.getRow(currentIndex);
        rowsIndexes.remove(currentIndex);
        currentIndex = rowsIndexes.isEmpty() ? -1 : rowsIndexes.get(0);
        return returnRow;
    }
}
