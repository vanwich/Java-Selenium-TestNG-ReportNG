package help.utils.excel.io.entity.area;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.ArrayUtils;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.Writable;
import help.utils.excel.io.entity.iterator.CellIterator;

public abstract class CellsQueue<CELL extends ExcelCell> implements Writable, Iterable<CELL> {
    private final int queueIndexInArea;
    private final int queueIndexOnSheet;
    private final ExcelArea<CELL, ?, ?> excelArea;
    protected List<CELL> cells;
    private List<CellType<?>> cellTypes;
    private List<Integer> cellsIndexesOnSheet;

    protected CellsQueue(int queueIndexInArea, int queueIndexOnSheet, List<Integer> cellsIndexesOnSheet, ExcelArea<CELL, ?, ?> excelArea) {
        this(queueIndexInArea, queueIndexOnSheet, cellsIndexesOnSheet, excelArea, excelArea.getCellTypes());
    }

    protected CellsQueue(int queueIndexInArea, int queueIndexOnSheet, List<Integer> cellsIndexesOnSheet, ExcelArea<CELL, ?, ?> excelArea, List<CellType<?>> cellTypes) {
        this.queueIndexInArea = queueIndexInArea;
        this.queueIndexOnSheet = queueIndexOnSheet;
        this.excelArea = excelArea;
        this.cellTypes = new ArrayList<>(cellTypes);
        this.cellsIndexesOnSheet = cellsIndexesOnSheet.stream().distinct().sorted().collect(Collectors.toList());
        this.cells = new ArrayList<>(cellsIndexesOnSheet.size());
    }

    public String getSheetName() {
        return getArea().getSheetName();
    }

    public List<CELL> getCells() {
        if (this.cells.isEmpty()) {
            int cellIndexInQueue = 1;
            for (Integer cellIndexOnSheet : this.cellsIndexesOnSheet) {
                CELL cell = createCell(cellIndexInQueue, cellIndexOnSheet);
                this.cells.add(cell);
                cellIndexInQueue++;
            }
        }
        return Collections.unmodifiableList(this.cells);
    }

    public abstract List<Integer> getCellsIndexes();

    public int getFirstCellIndex() {
        return getCellsIndexes().get(0);
    }

    public int getLastCellIndex() {
        return getCellsIndexes().get(getCellsNumber() - 1);
    }

    public CELL getFirstCell() {
        return getCell(getFirstCellIndex());
    }

    public CELL getLastCell() {
        return getCell(getLastCellIndex());
    }

    public int getCellsNumber() {
        return getCells().size();
    }

    public boolean isEmpty() {
        return getCells().stream().allMatch(ExcelCell::isEmpty);
    }

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>(getCellsNumber());
        for (CELL cell : getCells()) {
            values.add(cell.getValue());
        }
        return values;
    }

    public List<String> getStringValues() {
        List<String> values = new ArrayList<>(getCellsNumber());
        for (CELL cell : getCells()) {
            values.add(cell.getStringValue());
        }
        return values;
    }

    public int getIntSum() {
        return getIntSum(getCellsIndexes().toArray(new Integer[getCellsNumber()]));
    }

    public int getMaxIntValue() {
        return getCells().stream().filter(c -> !c.isEmpty() && c.hasType(ExcelCell.INTEGER_TYPE)).mapToInt(ExcelCell::getIntValue).max().orElse(0);
    }

    public int getMinIntValue() {
        return getCells().stream().filter(c -> !c.isEmpty() && c.hasType(ExcelCell.INTEGER_TYPE)).mapToInt(ExcelCell::getIntValue).min().orElse(0);
    }

    public List<CellType<?>> getCellTypes() {
        return Collections.unmodifiableList(this.cellTypes);
    }

    public int getIndex() {
        return this.queueIndexInArea;
    }

    public int getIndexOnSheet() {
        return this.queueIndexOnSheet;
    }

    public List<Integer> getCellsIndexesOnSheet() {
        return Collections.unmodifiableList(this.cellsIndexesOnSheet);
    }

    protected ExcelArea<CELL, ?, ?> getArea() {
        return this.excelArea;
    }

    @Override
    @Nonnull
    public Iterator<CELL> iterator() {
        return new CellIterator<>(this);
    }

    @Override
    public ExcelFile getExcelFile() {
        return getArea().getExcelFile();
    }

    @Override
    public String toString() {
        return "CellsQueue{" +
                "sheetName=" + getSheetName() +
                ", queueIndex=" + getIndex() +
                ", cellsNumber=" + getCellsNumber() +
                ", values=" + getStringValues() +
                ", cellTypes=" + getCellTypes() +
                '}';
    }

    public abstract List<CELL> getCellsByIndexes(List<Integer> cellsIndexesInQueue);

    public CELL getCellByValue(Object expectedValue, DateTimeFormatter... dateTimeFormatters) {
        for (CELL cell : getCells()) {
            if (cell.hasValue(expectedValue, dateTimeFormatters)) {
                return cell;
            }
        }
        throw new ExcelProcessingException(String.format("There is no cell with \"%1$s\" value%2$s in %3$s",
                expectedValue, ArrayUtils.isEmpty(dateTimeFormatters) ? "" : " using date formatters: " + Arrays.asList(dateTimeFormatters), this));
    }

    public <T> CELL getCellByValue(T expectedValue, CellType<T> cellType, DateTimeFormatter... dateTimeFormatters) {
        for (CELL cell : getCells()) {
            if (cell.hasValue(expectedValue, cellType, dateTimeFormatters)) {
                return cell;
            }
        }
        throw new ExcelProcessingException(String.format("There is no cell with \"%1$s\" value%2$s of %3$s type in %4$s",
                expectedValue, ArrayUtils.isEmpty(dateTimeFormatters) ? "" : " using date formatters: " + Arrays.asList(dateTimeFormatters), cellType, this));
    }

    public int getIntSum(Integer... cellsIndexesInQueue) {
        List<Integer> cellsIndexesList = Arrays.asList(cellsIndexesInQueue);
        return getCells().stream().filter(c -> cellsIndexesList.contains(c.getColumnIndex()) && !c.isEmpty() && c.hasType(ExcelCell.INTEGER_TYPE)).mapToInt(ExcelCell::getIntValue).sum();
    }

    public boolean isEmpty(int cellIndexInQueue) {
        return getCell(cellIndexInQueue).isEmpty();
    }

    public abstract CELL getCell(int cellIndexInQueue);

    public Object getValue(int cellIndexInQueue, DateTimeFormatter... dateTimeFormatters) {
        return getCell(cellIndexInQueue).getValue(dateTimeFormatters);
    }

    public <T> T getValue(int cellIndexInQueue, CellType<T> cellType, DateTimeFormatter... dateTimeFormatters) {
        return getCell(cellIndexInQueue).getValue(cellType, dateTimeFormatters);
    }

    public String getStringValue(int cellIndexInQueue) {
        return getCell(cellIndexInQueue).getStringValue();
    }

    public Boolean getBoolValue(int cellIndexInQueue) {
        return getCell(cellIndexInQueue).getBoolValue();
    }

    public Integer getIntValue(int cellIndexInQueue) {
        return getCell(cellIndexInQueue).getIntValue();
    }

    public Double getDoubleValue(int cellIndexInQueue) {
        return getCell(cellIndexInQueue).getDoubleValue();
    }

    @SuppressWarnings("unchecked")
    public <T extends Temporal> T getDateValue(int cellIndexInQueue, DateTimeFormatter... dateTimeFormatters) {
        return (T) getCell(cellIndexInQueue).getDateValue(ExcelCell.LOCAL_DATE_TIME_TYPE, dateTimeFormatters);
    }

    public CellsQueue<CELL> setValue(int cellIndexInQueue, Object value) {
        getCell(cellIndexInQueue).setValue(value);
        return this;
    }

    public <T> CellsQueue<CELL> setValue(int cellIndexInQueue, T value, CellType<T> valueType) {
        getCell(cellIndexInQueue).setValue(value, valueType);
        return this;
    }

    public abstract boolean hasCell(int cellIndexInQueue);

    public boolean hasValue(int cellIndexInQueue, Object expectedValue, DateTimeFormatter... dateTimeFormatters) {
        return getCell(cellIndexInQueue).hasValue(expectedValue, dateTimeFormatters);
    }

    public CellsQueue<CELL> registerCellType(List<CellType<?>> cellTypes) {
        this.cellTypes.addAll(cellTypes);
        this.cellTypes = this.cellTypes.stream().distinct().collect(Collectors.toList());
        getCells().forEach(c -> c.registerCellType(cellTypes));
        return this;
    }

    public abstract ExcelArea<CELL, ?, ?> exclude();

    public abstract ExcelArea<CELL, ?, ?> delete();

    public CellsQueue<CELL> clear() {
        getCells().forEach(ExcelCell::clear);
        return this;
    }

    public abstract CellsQueue<CELL> copy(int destinationQueueIndex);

    protected void removeCellsIndexes(Integer... cellsIndexesInQueue) {
        List<Integer> cellsIndexesToExclude = Arrays.asList(cellsIndexesInQueue);
        //TODO-dchubkov: replace with forEach and throw exception with exact missing index
        assertThat(cellsIndexesInQueue).as("Can't exclude cells with indexes %s", cellsIndexesToExclude).allMatch(this::hasCell);

        for (Integer cellIndex : cellsIndexesInQueue) {
            this.cellsIndexesOnSheet.remove(getCellIndexOnSheet(cellIndex));
        }
    }

    protected abstract CELL createCell(int cellIndexInQueue, int cellIndexOnSheet);

    protected abstract Integer getCellIndexOnSheet(Integer cellIndexInQueue);

    protected void addCell(CELL cell) {
        getCells(); // to initialize existing cells
        this.cells.add(cell);
        this.cellsIndexesOnSheet.add(cell.getColumnIndexOnSheet());
    }
}
