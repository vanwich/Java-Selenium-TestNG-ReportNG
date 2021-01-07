package data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.mifmif.common.regex.Generex;
import config.ClassConfigurator;
import config.PropertyProvider;
import config.TestProperties;
import utils.datetimes.DateTimeUtils;

public class DefaultMarkupParser implements MarkupParser{
    protected static Pattern expressionPattern;

    @ClassConfigurator.Configurable
    protected static String expressionRegexp = "\\$<(\\w+)([^>]*)>";//	old-school pattern is "^/(\\w+)\\s*(.*)$"
    @ClassConfigurator.Configurable
    protected static String datePattern = "MM/dd/yyyy";

    protected Random randomizer = new Random();
    protected Map<String, Function<String, String>> processors = new HashMap<>();

    static {
        ClassConfigurator configurator = new ClassConfigurator(DefaultMarkupParser.class);
        configurator.applyConfiguration();
        expressionPattern = Pattern.compile(expressionRegexp);
    }

    public DefaultMarkupParser() {
        registerProcessor("today", args -> processDateTime(DateTimeUtils.getCurrentDateTime(), args));
        registerProcessor("BOM", args -> processDateTime(DateTimeUtils.getCurrentDateTime().with(TemporalAdjusters.firstDayOfMonth()), args));
        registerProcessor("BOY", args -> processDateTime(DateTimeUtils.getCurrentDateTime().with(TemporalAdjusters.firstDayOfYear()), args));
        registerProcessor("file", s -> PropertyProvider.getProperty(TestProperties.UPLOAD_FILES_LOCATION) + s.replaceFirst("^:", ""));
        registerProcessor("rx", s -> new Generex(s.replaceFirst("^:", "")).random());
    }

    /**
     * Register additional markup processors
     * @param exprType expression type (e.g. "today" in "/today" markup string)
     * @param proc expression processor
     */
    public void registerProcessor(String exprType, Function<String, String> proc) {
        processors.put(exprType, proc);
    }

    @Override
    public String parse(String value) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = expressionPattern.matcher(value);
        while (matcher.find()) {
            String expressionType = matcher.group(1);
            String expressionArgs = matcher.group(2);
            matcher.appendReplacement(sb, "");
            String processedValue;
            if (processors.containsKey(expressionType)) {
                processedValue = processors.get(expressionType).apply(expressionArgs);
            } else {
                processedValue = processPattern(value);
            }
            sb.append(processedValue);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected String processDateTime(LocalDateTime dateTime, String argString) {
        String [] splitArgs = argString.split(":", 2);
        Pattern dp = Pattern.compile("([+-])(\\d+)(\\w)");
        Matcher m = dp.matcher(splitArgs[0]);
        while (m.find()) {
            int signum = m.group(1).equals("+") ? 1 : -1;
            long val = Long.parseLong(m.group(2));
            TemporalUnit unit;
            switch (m.group(3)) {
                case "m": unit = ChronoUnit.MINUTES; break;
                case "H": unit = ChronoUnit.HOURS; break;
                case "d": unit = ChronoUnit.DAYS; break;
                case "M": unit = ChronoUnit.MONTHS; break;
                case "y": unit = ChronoUnit.YEARS; break;
                default:
                    throw new IllegalArgumentException("Cannot parse " + m.group(3) + " in " + argString + " as temporal unit");
            }
            dateTime = dateTime.plus(val * signum, unit);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern((splitArgs.length == 2) ? splitArgs[1] : getDatePattern());
        return dateTime.format(fmt);
    }

    /**
     * Override this method if the value matches expression pattern but there is no processor for it
     * @param value input value
     * @return processed value
     */
    protected String processPattern(String value) {
        return value;
    }

    /**
     * Override this method to make configuration of date formatting pattern more flexible (e.g. to support i18n)
     * @return date formatting pattern
     */
    protected String getDatePattern() {
        return datePattern;
    }
}
