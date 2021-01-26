package help.utils.excel.io.celltype;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Objects;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.entity.area.ExcelCell;

public abstract class AbstractCellType<T> implements CellType<T>{
    protected Class<T> endType;

    public AbstractCellType(Class<T> endType) {
        this.endType = endType;
    }

    @Override
    public Class<T> getEndType() {
        return endType;
    }

    @Override
    public T getValueFrom(ExcelCell cell) {
        assertThat(isTypeOf(cell)).as("Unable to get value with \"%1$s\" type from %2$s", getEndType(), cell).isTrue();
        if (cell.getPoiCell() == null) {
            return null;
        }

        T value;
        try {
            value = getRawValueFrom(cell);
        } catch (RuntimeException e) {
            throw new ExcelProcessingException("Cannot get value from cell " + cell, e);
        }
        return value;
    }

    @Override
    public void setValueTo(ExcelCell cell, T value) {
        try {
            setRawValueTo(cell, value);
        } catch (RuntimeException e) {
            throw new ExcelProcessingException(String.format("Cannot set \"%1$s\" value to cell \"%2$s\"", value, cell), e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CellType<?> cellType = (CellType<?>) o;
        return Objects.equals(endType, cellType.getEndType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(endType);
    }

    @Override
    public String toString() {
        return "CellType{" +
                "endType=" + getEndType() +
                '}';
    }

    @Override
    public String getText(ExcelCell cell) {
        return ExcelCell.STRING_TYPE.getText(cell);
    }

    public boolean hasValueInTextFormat(ExcelCell cell) {
        if (cell.getPoiCell() == null) {
            return false;
        }
        org.apache.poi.ss.usermodel.CellType type = cell.getPoiCell().getCellTypeEnum();
        return type == org.apache.poi.ss.usermodel.CellType.STRING || type == org.apache.poi.ss.usermodel.CellType.BLANK;
    }

    protected abstract T getRawValueFrom(ExcelCell cell);

    protected abstract void setRawValueTo(ExcelCell cell, T value);
}
