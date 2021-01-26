package help.utils.excel.io.celltype;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import help.utils.Currency;
import help.utils.excel.io.entity.area.ExcelCell;

public class CurrencyCellType extends AbstractCellType<Currency>{
    public CurrencyCellType(Class<Currency> endType) {
        super(endType);
    }

    @Override
    public Currency getRawValueFrom(ExcelCell cell) {
        return hasValueInTextFormat(cell) ? new Currency(getText(cell)) : new Currency(cell.getPoiCell().getNumericCellValue());
    }

    @Override
    public boolean hasValueInTextFormat(ExcelCell cell) {
        return super.hasValueInTextFormat(cell) && (NumberUtils.isNumber(getText(cell)) || hasValueInCurrencyFormat(cell));
    }

    @Override
    public void setRawValueTo(ExcelCell cell, Currency value) {
        cell.getPoiCell().setCellValue(value.toString());
    }

    @Override
    public boolean isTypeOf(ExcelCell cell) {
        return cell.getPoiCell() == null || isNumeric(cell) || hasValueInTextFormat(cell);
    }

    protected boolean isNumeric(ExcelCell cell) {
        Cell c = cell.getPoiCell();
        return c.getCellTypeEnum() == org.apache.poi.ss.usermodel.CellType.NUMERIC && !DateUtil.isCellDateFormatted(c);
    }

    private boolean hasValueInCurrencyFormat(ExcelCell cell) {
        return getText(cell).matches("^\\(?\\$\\d+(\\.\\d{2})?\\)?$");
    }
}
