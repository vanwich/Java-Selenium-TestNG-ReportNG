package help.utils.excel.io.celltype;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import help.utils.excel.io.entity.area.ExcelCell;

public class LocalDateTimeCellType extends DateCellType<LocalDateTime>{
    public LocalDateTimeCellType(Class<LocalDateTime> endType, DateTimeFormatter... dateTimeFormatters) {
        super(endType, dateTimeFormatters);
        if (ArrayUtils.isEmpty(dateTimeFormatters)) {
            List<DateTimeFormatter> defaultFormatters = new ArrayList<>(2);
            defaultFormatters.add(new DateTimeFormatterBuilder().appendPattern("MM/dd/yyyy")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter());
            defaultFormatters.add(DateTimeFormatter.ofPattern(getBasePattern()));

            setFormatters(defaultFormatters);
        }
    }

    @Override
    protected final String getBasePattern() {
        return "MM/dd/yyyy HHmmss";
    }

    @Override
    protected LocalDateTime parseText(String dateInTextFormat, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(dateInTextFormat, dateTimeFormatter);
        //return TimeSetterUtil.getInstance().parse(dateInTextFormat, dateTimeFormatter);
    }

    @Override
    protected LocalDateTime getRawValueFrom(ExcelCell cell) {
        return cell.getPoiCell().getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    protected Date convertToJavaDate(LocalDateTime value) {
        return Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
    }
}
