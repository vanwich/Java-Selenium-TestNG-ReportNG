package help.utils.i18n;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class I18NByTTransformer implements Function<String, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(I18NByTTransformer.class);

    protected static Pattern expressionRegexp = Pattern.compile("\\$<(\\w+[^>]*)>");

    @Override
    public String apply(String s) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = expressionRegexp.matcher(s);
        while (matcher.find()) {
            String i18nQuery = matcher.group(1);
            matcher.appendReplacement(sb, StringUtils.EMPTY);
            String processedValue = StringUtils.EMPTY;
            String[] i18nQueryParts = i18nQuery.split("@");
            if (i18nQueryParts.length == 1) {
                processedValue = I18nResolver.i18n(i18nQuery);
            } else if (i18nQueryParts.length == 2) {
                processedValue = I18nResolver.i18n(i18nQueryParts[1], i18nQueryParts[0]);
            }
            sb.append(processedValue);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
