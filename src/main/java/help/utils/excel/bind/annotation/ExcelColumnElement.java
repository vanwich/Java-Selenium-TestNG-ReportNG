package help.utils.excel.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumnElement {
    String DEFAULT_COLUMN_NAME = "##field_name"; // means take class field name as header column name in table
    String DEFAULT_PRIMARY_KEY_SEPARATOR = ",";
    boolean DEFAULT_CASE_IGNORED = false;
    boolean DEFAULT_IS_PRIMARY_KEY = false;

    /**
     * Define whether field is related to table's primary key column. Values from primary key column is used as table rows filter<p>
     * for collecting List of objects with {@link ExcelTableElement} annotation.<p>
     * Only {@link Integer} type is supported for primary key field but in table it can be stored as String of numbers<p>
     * separated with {@link #primaryKeysSeparator} argument value
     */
    boolean isPrimaryKey() default DEFAULT_IS_PRIMARY_KEY;

    /**
     * Header column name to be searched in excel table (ignoring case if {@link #ignoreCase} argument is set to true).<p>
     * By default class field name is used as header column name in table.<p>
     * If field has {@link java.util.List} type then values from all columns containing field name will be collected to the list of appropriate type.<p>
     * Ignored if {@link #containsName} argument is set.
     */
    String name() default DEFAULT_COLUMN_NAME;

    /**
     * Column name substring pattern to be searched in header column names (ignoring case if {@link #ignoreCase} argument is set to true).<p>
     * If field has {@link java.util.List} type then values from all columns containing column name pattern will be collected to the list of appropriate type.
     */
    String containsName() default DEFAULT_COLUMN_NAME;

    /**
     * If true then ignore case while matching field name with header column name from excel file.<p>
     * Default value is false.
     */
    boolean ignoreCase() default DEFAULT_CASE_IGNORED;

    /**
     * Primary key separator for multiple key values. "," is default separator
     */
    String primaryKeysSeparator() default DEFAULT_PRIMARY_KEY_SEPARATOR;

    /**
     * Date format patterns array for {@link LocalDateTime} field types used for parsing cells with date values
     */
    String[] dateFormatPatterns() default {};
}
