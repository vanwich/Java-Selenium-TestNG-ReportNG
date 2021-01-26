package help.utils.excel.io.celltype;

import help.utils.excel.io.entity.area.ExcelCell;

public interface CellType<T> {
    Class<T> getEndType();

    T getValueFrom(ExcelCell cell);

    void setValueTo(ExcelCell cell, T value);

    boolean isTypeOf(ExcelCell cell);

    String getText(ExcelCell cell);
}
