package utils.datetimes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import config.ClassConfigurator;
import config.PropertyProvider;
import config.TestProperties;

public final class DateTimeUtils {
    private DateTimeUtils() {}

    public static final DateTimeFormatter DD_MM_YYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter MM_DD_YYYY = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter TIME_STAMP = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter TIME_STAMP_WITH_MS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    public static final List<DayOfWeek> weekends =
            Arrays.asList(PropertyProvider.getProperty(TestProperties.DATE_WEEKENDS, "SATURDAY,SUNDAY").trim().split("\\s*,\\s*"))
                    .stream().map(s -> DayOfWeek.valueOf(s.toUpperCase())).collect(Collectors.toList());
    private static final List<LocalDate> nonWorkingDays = propertyToDateList(TestProperties.DATE_NON_WORKING_DAYS);
    private static final List<LocalDate> workingWeekends = propertyToDateList(TestProperties.DATE_WORKING_WEEKENDS);

    @ClassConfigurator.Configurable(byClassName = true) protected static Supplier<LocalDateTime> dateTimeSupplier = () -> LocalDateTime.now();
    @ClassConfigurator.Configurable(byClassName = true) protected static Function<LocalDate, Boolean> isWorkingDay = (LocalDate ld) -> {
        DayOfWeek dow = ld.getDayOfWeek();
        boolean isWeekend = weekends.contains(dow);
        boolean isWorkingWeekend = workingWeekends.contains(ld);
        boolean isNonWorkingDay = nonWorkingDays.contains(ld);
        return !(isWeekend || isNonWorkingDay) || isWorkingWeekend;
    };

    static {
        ClassConfigurator configurator = new ClassConfigurator(DateTimeUtils.class);
        configurator.applyConfiguration();
    }

    /**
     * Get current date/time from configured supplier
     * @return current date/time without time zone
     */
    public static LocalDateTime getCurrentDateTime() {
        return dateTimeSupplier.get();
    }

    /**
     * Get temporal adjuster that adds specified amount of working days
     * @param days number of working days to add
     * @return adjusted LocalDateTime object
     */
    public static TemporalAdjuster plusWorkingDays(int days) {
        return plusWorkingDays(days, isWorkingDay);
    }

    /**
     * Get temporal adjuster that adds specified amount of working days using provided isWorkingDay function
     * @param days number of working days to add
     * @param isWorkingDayFn function to determine whether specified date is a working day
     * @return adjusted LocalDateTime object
     */
    public static TemporalAdjuster plusWorkingDays(int days, Function<LocalDate, Boolean> isWorkingDayFn) {
        return (Temporal date) -> addWorkingDays(date, days, isWorkingDayFn);
    }

    /**
     * Get temporal adjuster that subtracts specified amount of working days
     * @param days number of working days to subtract
     * @return adjusted LocalDateTime object
     */
    public static TemporalAdjuster minusWorkingDays(int days) {
        return minusWorkingDays(days, isWorkingDay);
    }

    /**
     * Get temporal adjuster that subtracts specified amount of working days using provided isWorkingDay function
     * @param days number of working days to subtract
     * @param isWorkingDayFn function to determine whether specified date is a working day
     * @return adjusted LocalDateTime object
     */
    public static TemporalAdjuster minusWorkingDays(int days, Function<LocalDate, Boolean> isWorkingDayFn) {
        return (Temporal date) -> addWorkingDays(date, -days, isWorkingDayFn);
    }

    /**
     * Temporal adjuster that adds one working day
     */
    public final static TemporalAdjuster nextWorkingDay = nextWorkingDay(isWorkingDay);

    /**
     * Temporal adjuster that subtracts one working day
     */
    public final static TemporalAdjuster previousWorkingDay = previousWorkingDay(isWorkingDay);

    /**
     * Temporal adjuster that adds one working day unless today is a working day
     */
    public final static TemporalAdjuster closestFutureWorkingDay = closestFutureWorkingDay(isWorkingDay);

    /**
     * Temporal adjuster that subtracts one working day unless today is a working day
     */
    public final static TemporalAdjuster closestPastWorkingDay = closestPastWorkingDay(isWorkingDay);

    /**
     * Method to return temporal adjuster that adds one working day using provided isWorkingDay function
     * @param isWorkingDayFn function to determine whether specified date is a working day
     * @return temporal adjuster
     */
    public final static TemporalAdjuster nextWorkingDay(Function<LocalDate, Boolean> isWorkingDayFn) {
        return (Temporal date) -> addWorkingDays(date, 1, isWorkingDayFn);
    }

    /**
     * Method to return temporal adjuster that subtracts one working day using provided isWorkingDay function
     * @param isWorkingDayFn function to determine whether specified date is a working day
     * @return temporal adjuster
     */
    public final static TemporalAdjuster previousWorkingDay(Function<LocalDate, Boolean> isWorkingDayFn) {
        return (Temporal date) -> addWorkingDays(date, -1, isWorkingDayFn);
    }

    /**
     * Method to return temporal adjuster that adds one working day unless today is a working day
     * using provided isWorkingDay function
     * @param isWorkingDayFn function to determine whether specified date is a working day
     * @return temporal adjuster
     */
    public final static TemporalAdjuster closestFutureWorkingDay(Function<LocalDate, Boolean> isWorkingDayFn) {
        return (Temporal date) -> closestWorkingDay(date, true, isWorkingDayFn);
    }

    /**
     * Method to return temporal adjuster that subtracts one working day unless today is a working day
     * using provided isWorkingDay function
     * @param isWorkingDayFn function to determine whether specified date is a working day
     * @return temporal adjuster
     */
    public final static TemporalAdjuster closestPastWorkingDay(Function<LocalDate, Boolean> isWorkingDayFn) {
        return (Temporal date) -> closestWorkingDay(date, false, isWorkingDayFn);
    }

    private static List<LocalDate> propertyToDateList(String propName) {
        String propVal = PropertyProvider.getProperty(propName);
        if (StringUtils.isBlank(propVal)) {
            return new ArrayList<>();
        } else {
            try {
                return Arrays.asList(PropertyProvider.getProperty(propName).trim().split("\\s*,\\s*"))
                        .stream().map(s -> MM_DD_YYYY.parse(s, LocalDate::from))
                        .collect(Collectors.toList());
            } catch (DateTimeParseException e) {
                throw new IllegalStateException("Cannot parse property " + propName + " as list of local dates", e);
            }
        }
    }

    private static LocalDateTime addWorkingDays(Temporal t, int days, Function<LocalDate, Boolean> isWorkingDayFn) {
        if (t instanceof LocalDateTime) {
            LocalDateTime ldt = (LocalDateTime) t;
            int step = Integer.signum(days);
            for (int i = 0; i < Math.abs(days); i++) {
                ldt = ldt.plusDays(step);
                while (!isWorkingDayFn.apply(ldt.toLocalDate())) {
                    ldt = ldt.plusDays(step);
                }
            }
            return ldt;
        } else {
            throw new IllegalArgumentException("Temporal " + t + " must be an instance of LocalDateTime!");
        }
    }

    /**
     * Add or subtract one working day unless today is also a working day
     * @param t date to adjust
     * @param isFuture true for next day, false for previous one
     * @return adjusted date
     */
    private static Temporal closestWorkingDay(Temporal date, boolean isFuture, Function<LocalDate, Boolean> isWorkingDayFn) {
        if (date instanceof LocalDateTime) {
            return isWorkingDayFn.apply(((LocalDateTime) date).toLocalDate()) ? date : addWorkingDays(date, isFuture ? 1 : -1, isWorkingDayFn);
        } else {
            throw new IllegalArgumentException("Temporal " + date + " must be an instance of LocalDateTime!");
        }
    }
}
