package help.utils.excel.bind.cache;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.bind.ReflectionHelper;
import help.utils.excel.bind.annotation.ExcelTableElement;
import help.utils.excel.io.entity.area.sheet.ExcelSheet;
import help.utils.excel.io.entity.area.table.ExcelTable;

public class MarshallingClassInfo extends TableClassInfo{
    private ExcelTable excelTable;
    private ExcelSheet excelSheet;
    private List<?> tableRowsObjects;

    MarshallingClassInfo(Class<?> tableClass, ExcelFile excelFile) {
        super(tableClass, excelFile);
    }

    MarshallingClassInfo(Class<?> tableClass, List<Object> tableRowsObjects, ExcelFile excelFile) {
        super(tableClass, excelFile);
        this.tableRowsObjects = Collections.unmodifiableList(tableRowsObjects);
    }

    List<Object> getRowsObjects() {
        if (!hasRowsObjects()) {
            throw new ExcelProcessingException("There is no cached rows objects for table class " + getTableClass().getName());
        }
        return Collections.unmodifiableList(tableRowsObjects);
    }

    void setRowsObjects(List<?> tableRowsObjects) {
        this.tableRowsObjects = Collections.unmodifiableList(tableRowsObjects);
    }

    @Override
    public ExcelTable getExcelTable() {
        if (this.excelTable == null) {
            ExcelSheet sheet = getExcelSheet();
            int headerRowIndex = getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).headerRowIndex();
            if (headerRowIndex < 0) {
                log.warn("Table model class \"{}\" has negative parameter headerRowIndex()={}, first row from sheet will be used as table's header row", getTableClass().getName(), headerRowIndex);
                headerRowIndex = 1;
            }
            this.excelTable = sheet.addTable(headerRowIndex, getHeaderColumnNames().toArray(new String[0]));
        }
        return this.excelTable;
    }

    @Override
    public ExcelSheet getExcelSheet() {
        if (this.excelSheet == null) {
            String sheetName;

            if (!getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).containsSheetName().equals(ExcelTableElement.DEFAULT_CONTAINS_SHEET_NAME)) {
                //containsSheetName() parameter is defined in Table class annotation
                sheetName = getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).containsSheetName();
                List<ExcelSheet> sheets = getExcelFile().getSheetsContains(sheetName);
                if (sheets.isEmpty()) {
                    log.warn("Table model class \"{}\" has parameter containsSheetName()=\"{}\" in ExcelTableElement annotation, excel sheet will be created using this name pattern.", getTableClass().getName(), sheetName);
                    this.excelSheet = getExcelFile().addSheet(sheetName);
                } else {
                    if (sheets.size() > 1) {
                        log.warn("Table model class \"{}\" has parameter containsSheetName()=\"{}\" in ExcelTableElement annotation.\n"
                                + "ExcelManager already has more than one sheet with this name pattern, first one with \"{}\" name will be used for this class", getTableClass().getName(), sheets.get(0).getSheetName());
                    }
                    this.excelSheet = sheets.get(0);
                }

            } else {
                //containsSheetName() parameter is NOT defined in Table class annotation
                if (getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).sheetName().equals(ExcelTableElement.DEFAULT_SHEET_NAME)) {
                    //sheetName() parameter is also NOT defined in Table class annotation
                    if (!getExcelFile().hasSheet(1)) {
                        throw new ExcelProcessingException(String.format("Table model class %1$s has neither sheetName() nor containsSheetName() defined parameter in ExcelTableElement annotation which means take first sheet from excel file,\n"
                                + "but there is no sheets in provided %2$s", getTableClass().getName(), getExcelFile()));
                    }

                    this.excelSheet = getExcelFile().getSheet(1);
                } else {
                    //sheetName() parameter is defined in Table class annotation
                    sheetName = getAnnotatedTableClass().getAnnotation(ExcelTableElement.class).sheetName();
                    this.excelSheet = getExcelFile().hasSheet(sheetName) ? getExcelFile().getSheet(sheetName) : getExcelFile().addSheet(sheetName);
                }
            }
        }
        return this.excelSheet;
    }

    @Override
    public void flushInfo() {
        super.flushInfo();
        this.tableRowsObjects = null;
    }

    boolean hasRowsObjects() {
        return this.tableRowsObjects != null;
    }

    private List<String> getHeaderColumnNames() {
        List<String> headerColumnNames = new ArrayList<>();
        for (Field field : getTableColumnsFields()) {
            TableFieldInfo fieldInfo = getFieldInfo(field);
            headerColumnNames.add(fieldInfo.getHeaderColumnName());
            if (fieldInfo.getBindType().equals(TableFieldInfo.BindType.MULTI_COLUMNS)) {
                int maxMultiColumnsNumber = 0;
                //TODO-dchubkov: check multi columns with containsName() parameter...
                for (Object tableRowObject : getRowsObjects()) {
                    //noinspection unchecked
                    if (((List<Object>) ReflectionHelper.getFieldValue(field, tableRowObject)).size() > maxMultiColumnsNumber) {
                        maxMultiColumnsNumber++;
                    }
                }

                for (int i = 0; i < maxMultiColumnsNumber - 1; i++) {
                    headerColumnNames.add(fieldInfo.getHeaderColumnName());
                }
            }
        }
        return headerColumnNames;
    }
}
