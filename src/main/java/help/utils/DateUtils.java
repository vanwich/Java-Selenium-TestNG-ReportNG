package help.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import help.config.PropertyProvider;

public class DateUtils {
    public static final String timeZone = PropertyProvider.getProperty("test.environment.timezone", "Europe/Vilnius");
    //corresponds to DateTimeFormatter.ISO_DATE
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_T_HH_MM_SS_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String YYYY_MM_DD_T_00 = "yyyy-MM-dd'T'00:00:00'Z'";

    public static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS_Z);
    public static final TemporalAdjuster plusNextLeapYear = DateUtils::nextLeapYear;

    /**
     * Parse LocalDate with 'yyyy-MM-dd' format <br/>
     *
     * @param date String representation of LocalDate <br/>
     * @return LocalDate if date is not null and null otherwise.
     */
    public static LocalDate parseToLocalDate(String date){
        try {
            return parseToLocalDate(date, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException dtpe) {
            return DateUtils.parseToLocalDateTime(date).toLocalDate();
        }
    }

    /**
     * Parse LocalDate with custom format <br/>
     *
     * @param date String representation of LocalDate <br/>
     * @param format custom DateTimeFormatter <br/>
     * @return LocalDate if date is not null and null otherwise.
     */
    public static LocalDate parseToLocalDate(String date, DateTimeFormatter format){
        return date == null ? null : LocalDate.parse(date, format);
    }

    /**
     * Parse LocalDateTime with yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format and floating fraction count (0 - 3)<br/>
     *
     * @param datetime String representation of LocalDateTime <br/>
     * @return LocalDateTime if date is not null and null otherwise.
     */
    public static LocalDateTime parseToLocalDateTime(String datetime){
        return parseToLocalDateTime(datetime, getISOInstantSSSFormat());
    }

    private static DateTimeFormatter getISOInstantSSSFormat(){
        return new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
                .appendZoneOrOffsetId()
                .toFormatter();
    }

    /**
     * Parse LocalDateTime with custom format<br/>
     *
     * @param datetime String representation of LocalDateTime <br/>
     * @param format custom DateTimeFormatter <br/>
     * @return LocalDateTime if date is not null and null otherwise.
     */
    public static LocalDateTime parseToLocalDateTime(String datetime, DateTimeFormatter format){
        if (datetime == null) {
            return null;
        }
        return ZonedDateTime.parse(datetime, format).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static LocalDateTime atApplicationZone(LocalDateTime time) {
        ZonedDateTime utcZoned = time.atZone(ZoneId.of(DateUtils.timeZone)).withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime().truncatedTo(ChronoUnit.MILLIS);
    }

    public static LocalDateTime atApplicationZone(String datetime) {
        ZonedDateTime utcZoned = LocalDateTime.parse(datetime, ISO_INSTANT).atZone(ZoneId.of(DateUtils.timeZone)).withZoneSameInstant(ZoneId.of("UTC"));
        return utcZoned.toLocalDateTime().truncatedTo(ChronoUnit.MILLIS);
    }

    public static String formatISO(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public static LocalDateTime getCurrentDateTime(){
        return DateTimeUtils.getCurrentDateTime();
    }

    public static LocalDate getCurrentDate(){
        return DateTimeUtils.getCurrentDateTime().toLocalDate();
    }

    private static LocalDateTime nextLeapYear(Temporal t) {
        LocalDateTime ldt = (LocalDateTime) t;
        int year = ldt.getYear();

        while (!((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0)))) {
            year++;
        }
        return ldt.withYear(year);
    }

    public static String getZoneTime(LocalDateTime localDateTime){
        return atApplicationZone(localDateTime.format(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_T_00))).format(DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS_Z));
    }

    public static String getCurrentZoneTime(){
        return getZoneTime(DateTimeUtils.getCurrentDateTime());
    }

    public static String getAfterOneYestZoneTime(){
        return getZoneTime(DateTimeUtils.getCurrentDateTime().plusYears(1));
    }
}
