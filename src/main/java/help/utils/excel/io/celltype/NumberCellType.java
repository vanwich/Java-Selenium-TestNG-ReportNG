package help.utils.excel.io.celltype;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import help.utils.excel.io.entity.area.ExcelCell;

public abstract class NumberCellType <T extends Number> extends AbstractCellType<T>{
    public NumberCellType(Class<T> endType) {
        super(endType);
    }

    @Override
    public T getRawValueFrom(ExcelCell cell) {
        return hasValueInTextFormat(cell) ? parseText(getText(cell)) : parseDouble(cell.getPoiCell().getNumericCellValue());
    }

    @Override
    public void setRawValueTo(ExcelCell cell, Number value) {
        cell.getPoiCell().setCellValue(value.doubleValue());
    }

    @Override
    public boolean isTypeOf(ExcelCell cell) {
        return cell.getPoiCell() == null || isNumeric(cell) || hasValueInTextFormat(cell);
    }

    @Override
    public boolean hasValueInTextFormat(ExcelCell cell) {
        return super.hasValueInTextFormat(cell) && NumberUtils.isNumber(getText(cell));
    }

    protected abstract T parseText(String s);

    protected abstract T parseDouble(double d);

    protected boolean isNumeric(ExcelCell cell) {
        Cell c = cell.getPoiCell();
        return c.getCellTypeEnum() == org.apache.poi.ss.usermodel.CellType.NUMERIC && !DateUtil.isCellDateFormatted(c);
    }
}
