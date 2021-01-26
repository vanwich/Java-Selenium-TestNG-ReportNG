package help.utils.excel.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import help.utils.excel.ExcelUnmarshaller;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelTableElement {
    String DEFAULT_SHEET_NAME = "##first_sheet_index_name";  // means use name of sheet with index=1 in excel file
    String DEFAULT_CONTAINS_SHEET_NAME = "##not_set";        // means use value of "sheetName" parameter

    /**
     * Positive value will be used as table's header row index. After finding this row if {@code strictMatchBinding} argument is true<p>
     * in {@link ExcelUnmarshaller} unmarshaller methods then only class field names will be used as header column names,<p>
     * otherwise ({@code strictMatchBinding} is false) - all non empty cells within found excel row will be used as header column names.<p>
     * Negative value means search for first occurrence of header row on sheet which has all column names defined as class field names.<p>
     * Default value is "-1", rows indexes starts from 1.
     */
    int headerRowIndex() default -1;

    /**
     * Excel sheet name to be searched <p>
     * Ignored if {@link #containsSheetName} argument is set.
     */
    String sheetName() default DEFAULT_SHEET_NAME; // default value is set just to make possible to use "containsSheetName" without "sheetName"

    /**
     * Sheet name substring pattern to be searched in list of excel sheet names.<p>
     */
    String containsSheetName() default DEFAULT_CONTAINS_SHEET_NAME;

    /**
     * If true then ignore case while matching each class field name with header column name from excel file.<p>
     * <b>true</b> value on class level overrides all field's {@link ExcelColumnElement#ignoreCase()} values.
     * Default value is false.
     */
    boolean ignoreCase() default false;

    /**
     * If <b>true</b> then all rows from sheet starting from {@code headerRowIndex} will be used as tables' rows even if they are empty.<p>
     * If <b>false</b> then all rows from header row up to first empty row will be used as tables' rows. Default value is false.
     * @return
     */
    boolean hasEmptyRows() default false;
}
