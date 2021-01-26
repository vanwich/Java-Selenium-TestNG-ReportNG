package help.utils.excel.io.entity.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import help.utils.excel.io.entity.area.CellsQueue;
import help.utils.excel.io.entity.area.ExcelCell;

public class CellIterator<CELL extends ExcelCell> implements Iterator<CELL> {
    private CellsQueue<CELL> cellsQueue;
    private List<Integer> cellsIndexes;
    private Integer currentIndex;

    @Nonnull
    public CellIterator(CellsQueue<CELL> cellsQueue) {
        this.cellsQueue = cellsQueue;
        this.cellsIndexes = new ArrayList<>(cellsQueue.getCellsIndexes());
        this.currentIndex = cellsQueue.getFirstCellIndex();
    }

    @Override
    public boolean hasNext() {
        return cellsQueue.hasCell(currentIndex);
    }

    @Override
    public CELL next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There is no next cell");
        }
        CELL returnCell = cellsQueue.getCell(currentIndex);
        cellsIndexes.remove(currentIndex);
        currentIndex = cellsIndexes.isEmpty() ? -1 : cellsIndexes.get(0);
        return returnCell;
    }
}
