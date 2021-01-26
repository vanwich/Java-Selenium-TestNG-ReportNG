package help.utils.excel.io.celltype;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.io.entity.area.ExcelCell;

public abstract class DateCellType<T extends Temporal> extends AbstractCellType<T>{
    private DateTimeFormatter[] dateTimeFormatters;

    public DateCellType(Class<T> endType, DateTimeFormatter... dateTimeFormatters) {
        super(endType);
        if (ArrayUtils.isNotEmpty(dateTimeFormatters)) {
            this.dateTimeFormatters = Arrays.stream(dateTimeFormatters).distinct().toArray(DateTimeFormatter[]::new);
        }
    }

    public List<DateTimeFormatter> getFormatters() {
        return Stream.of(this.dateTimeFormatters).collect(Collectors.toList());
    }

    protected final void setFormatters(List<DateTimeFormatter> dateTimeFormatters) {
        this.dateTimeFormatters = dateTimeFormatters.toArray(new DateTimeFormatter[0]);
    }

    protected abstract String getBasePattern();

    @Override
    public boolean isTypeOf(ExcelCell cell) {
        return isTypeOf(cell, this.dateTimeFormatters);
    }

    @Override
    public boolean hasValueInTextFormat(ExcelCell cell) {
        return hasValueInTextFormat(cell, this.dateTimeFormatters);
    }

    @Override
    public void setRawValueTo(ExcelCell cell, T value) {
        Cell poiCell = cell.getPoiCell();
        if (poiCell.getCellTypeEnum() != org.apache.poi.ss.usermodel.CellType.NUMERIC || !DateUtil.isCellDateFormatted(poiCell)) {
            Workbook wb = poiCell.getSheet().getWorkbook();
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setDataFormat(poiCell.getSheet().getWorkbook().getCreationHelper().createDataFormat().getFormat(getBasePattern()));
            poiCell.setCellStyle(cellStyle);
        }
        poiCell.setCellValue(convertToJavaDate(value));
    }

    @Override
    public T getValueFrom(ExcelCell cell) {
        return getValueFrom(cell, this.dateTimeFormatters);
    }

    public T getValueFrom(ExcelCell cell, DateTimeFormatter... dateTimeFormatters) {
        assertThat(isTypeOf(cell, dateTimeFormatters)).as("Unable to get value with \"%1$s\" type from %2$s%3$s", getEndType(), cell,
                ArrayUtils.isNotEmpty(dateTimeFormatters) ? " using date formatters: " + Arrays.asList(dateTimeFormatters) : "").isTrue();
        if (cell.getPoiCell() == null) {
            return null;
        }

        T dateValue;
        try {
            if (hasStringValue(cell)) {
                DateTimeFormatter formatter = getValidFormatter(cell, dateTimeFormatters);
                if (formatter != null) {
                    dateValue = parseText(getText(cell), formatter);
                } else {
                    throw new ExcelProcessingException("Unable to find valid DateTimeFormatter");
                }
            } else {
                dateValue = getRawValueFrom(cell);
            }
        } catch (DateTimeException | ExcelProcessingException e) {
            throw new ExcelProcessingException("Cannot get date value from cell " + cell, e);
        }
        return dateValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DateCellType<?> cellType = (DateCellType<?>) o;
        return Objects.equals(endType, cellType.getEndType()) && Arrays.equals(dateTimeFormatters, cellType.dateTimeFormatters);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(dateTimeFormatters);
        return result;
    }

    public boolean isTypeOf(ExcelCell cell, DateTimeFormatter... dateTimeFormatters) {
        Cell c = cell.getPoiCell();
        return c == null || c.getCellTypeEnum() == org.apache.poi.ss.usermodel.CellType.NUMERIC && DateUtil.isCellDateFormatted(c) || hasValueInTextFormat(cell, dateTimeFormatters);
    }

    public boolean hasValueInTextFormat(ExcelCell cell, DateTimeFormatter... dateTimeFormatters) {
        return hasStringValue(cell) && getValidFormatter(cell, dateTimeFormatters) != null;
    }

    protected abstract T parseText(String dateInTextFormat, DateTimeFormatter dateTimeFormatter) throws DateTimeParseException;

    protected abstract Date convertToJavaDate(T value);

    protected boolean hasStringValue(ExcelCell cell) {
        return cell.getPoiCell() != null && cell.getPoiCell().getCellTypeEnum() == org.apache.poi.ss.usermodel.CellType.STRING;
    }

    protected DateTimeFormatter getValidFormatter(ExcelCell cell, DateTimeFormatter... dateTimeFormatters) {
        String text = getText(cell);
        if (text == null) {
            return null;
        }
        DateTimeFormatter[] availableFormatters = ArrayUtils.isNotEmpty(dateTimeFormatters) ? dateTimeFormatters : this.dateTimeFormatters;
        for (DateTimeFormatter formatter : availableFormatters) {
            try {
                parseText(text, formatter);
                return formatter;
            } catch (DateTimeParseException ignore) {
            }
        }
        return null;
    }
}
