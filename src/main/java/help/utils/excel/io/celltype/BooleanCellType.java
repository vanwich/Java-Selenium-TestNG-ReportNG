package help.utils.excel.io.celltype;

import help.utils.excel.io.entity.area.ExcelCell;

public class BooleanCellType extends AbstractCellType<Boolean>{
    public BooleanCellType(Class<Boolean> endType) {
        super(endType);
    }

    @Override
    public Boolean getRawValueFrom(ExcelCell cell) {
        return hasValueInTextFormat(cell) ? Boolean.valueOf(getText(cell)) : cell.getPoiCell().getBooleanCellValue();
    }

    @Override
    public void setRawValueTo(ExcelCell cell, Boolean value) {
        cell.getPoiCell().setCellValue(value);
    }

    @Override
    public boolean isTypeOf(ExcelCell cell) {
        return cell.getPoiCell() == null || cell.getPoiCell().getCellTypeEnum() == org.apache.poi.ss.usermodel.CellType.BOOLEAN || hasValueInTextFormat(cell);
    }

    @Override
    public boolean hasValueInTextFormat(ExcelCell cell) {
        if (super.hasValueInTextFormat(cell)) {
            String value = getText(cell);
            return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
        }
        return false;
    }
}
