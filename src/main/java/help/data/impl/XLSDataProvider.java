package help.data.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import help.config.ClassConfigurator;
import help.data.DataFormat;
import help.data.FileBasedDataProvider;
import help.data.TestData;
import help.data.TestDataException;

public class XLSDataProvider extends FileBasedDataProvider {
    protected static String datePattern = "MM/dd/yyyy";
    public static final String GLOBAL_SCOPE = "*GLOBAL*";
    public static final String NULL_VALUE = "NULL";
    public static final String ARRAY_SEPARATOR = "|";
    protected static final SpreadsheetVersion spreadsheetVersion = SpreadsheetVersion.EXCEL97;

    @ClassConfigurator.Configurable
    private static boolean keyValueColumnNamesIncluded = true;

    //	XLSDataProvider should never be created with globalOnly = true

    private XLSDataProvider() {
        super();
    }

    public XLSDataProvider(String filePath) {
        super(filePath, false);
    }

    public XLSDataProvider(String filePath, String sheetName) {
        super(filePath, sheetName);
    }

    static {
        ClassConfigurator configurator = new ClassConfigurator(XLSDataProvider.class);
        configurator.applyConfiguration();
    }

    @Override
    protected String makePath() {
        String suffix = getExtensionSuffix();
        if (groupPath.endsWith(suffix)) {
            return groupPath;
        } else {
            return groupPath + suffix;
        }
    }

    @Override
    protected List<String> getChildren(String path) {
        List<String> sheetNames = new ArrayList<String>();
        this.filePath = makePath();

        try (InputStream is = new FileInputStream(filePath); Workbook wb = new HSSFWorkbook(is)) {
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                sheetNames.add(wb.getSheetName(i));
            }
        } catch (IOException e) {
            throw new TestDataException("Failed to read sheet names from " + filePath, e);
        }
        return sheetNames;
    }

    @Override
    protected Map<String, Object> readTestData(InputStream is) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();

        try {
            Workbook wb = new HSSFWorkbook(is);
            List<Name> nameRanges = getNameRanges(wb, collectionName);
            for (Name name : nameRanges) {
                try {
                    data.put(name.getNameName(), getNameRangeValues(wb, name));
                } catch (Exception e) {
                    data.put(name.getNameName(), new BrokenTestData("Error reading named range " + name.getNameName() + " in workbook " + makePath(), e));
                }

            }
        } catch (IOException e) {
            throw new TestDataException("Error reading test data from " + filePath, e);
        }

        return data;
    }

    /**
     * Get list of name ranges
     * @param wb - workbook
     * @param sheet - sheet name. If null, only ranges with "workbook" scope are returned.
     * @return list of named ranges' names
     */
    private List<Name> getNameRanges(Workbook wb, String sheet) {
        List<Name> names = new ArrayList<Name>();
        String tempSheetName, refersToFormula;

        for (Name tempNameRange : wb.getAllNames()) {
            try { refersToFormula = tempNameRange.getRefersToFormula(); } catch (Exception e) { continue; } //broken ranges ignoring
            if (!refersToFormula.contains("#REF!")) {
                if (sheet.equals(XLSDataProvider.GLOBAL_SCOPE)) {
                    if (tempNameRange.getSheetIndex() < 0) {
                        names.add(tempNameRange);
                    }
                } else {
                    if (AreaReference.isContiguous(refersToFormula)){
                        tempSheetName = tempNameRange.getSheetName();
                    } else {
                        tempSheetName = AreaReference.generateContiguous(spreadsheetVersion, tempNameRange.getRefersToFormula())[0].getFirstCell().getSheetName();
                    }
                    if (tempSheetName.equals(sheet)) {
                        names.add(tempNameRange);
                    }
                }
            }
        }
        return names;
    }

    private Object getNameRangeValues(Workbook wb, Name namedRange) {
        String formula = namedRange.getRefersToFormula();
        List<AreaReference> refs = new ArrayList<AreaReference>();

        if (AreaReference.isContiguous(formula)){
            refs.add(new AreaReference(formula, spreadsheetVersion));
        } else {
            refs.addAll(Arrays.asList(AreaReference.generateContiguous(spreadsheetVersion, formula)));
        }

        String rangeName = namedRange.getNameName();
        Sheet currentSheet = wb.getSheet(refs.get(0).getFirstCell().getSheetName());
        if (currentSheet == null) {
            throw new TestDataException("Invalid named range " + rangeName + " in workbook " + makePath());
        }

        Cell[][] cells = buildCellArray(currentSheet, rangeName, refs);
        int width = cells[0].length;
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        if (width >= 2) {
            int start, lim, num;
            List<Map<String, Object>> listOfMaps;
            boolean isKeyValue = (width == 2);

            if (isKeyValue) {
                start = keyValueColumnNamesIncluded ? 1 : 0;
                lim = cells.length;
                num = 1;
            } else {
                start = 0;
                lim = width;
                num = cells.length - 1;
            }
            listOfMaps = new ArrayList<Map<String, Object>>(num);
            for (int i = 0; i < num; i++) {
                listOfMaps.add(new LinkedHashMap<String, Object>());
            }

            String key;
            for (int i = start; i < lim; i++) {
                try {
                    Cell keyCell = cells[isKeyValue ? i : 0][isKeyValue ? 0 : i];
                    key = (keyCell == null) ? null : keyCell.getStringCellValue();
                } catch (Exception e) {
                    throw new TestDataException("One of the keys in named range " + rangeName + " does not evaluate to string", e);
                }

                if (!StringUtils.isBlank(key)) {
                    for (int j = 0; j < num; j++) {
                        listOfMaps.get(j).put(key, extractCellValue(cells[isKeyValue ? i : (j + 1)][isKeyValue ? (j + 1) : i], evaluator));
                    }
                }
            }

            return isKeyValue ? listOfMaps.get(0) : listOfMaps;
        } else {
            throw new TestDataException("Named range " + rangeName + " has less than 2 columns");
        }
    }

    private String getCellValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return "";
        }

        CellType evaluatedCellType;
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            evaluatedCellType = cell.getCachedFormulaResultType();
        } else {
            evaluatedCellType = cellType;
        }

        switch (evaluatedCellType) {
            case NUMERIC:
                cell = evaluator.evaluateInCell(cell);
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat(datePattern).format(cell.getDateCellValue());
                } else {
                    return new HSSFDataFormatter().formatCellValue(cell);
                }

            case STRING:
                String str = cell.getRichStringCellValue().toString();
                return str.equals(XLSDataProvider.NULL_VALUE) ? null : str;

            case BLANK:
                return "";

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            default:
                return "";
        }
    }

    @Override
    protected String getExtensionSuffix() {
        return "." + DataFormat.XLS.getExtension();
    }

    public static Boolean isValidDataStorage(String groupPath, String ignored, String extensionPattern) {
        String path;
        if (groupPath.endsWith("." + extensionPattern)) {
            path = groupPath;
        } else {
            path = groupPath + "." + extensionPattern;
        }

        return new File(path).exists();
    }

    private Cell [][] buildCellArray(Sheet currentSheet, String name, List<AreaReference> refs) {
        int totalWidth = 0;
        int totalHeight = 0;

        int [] widths = new int[refs.size()];

        for (int i = 0; i < refs.size(); i++) {
            CellReference first = refs.get(i).getFirstCell();
            CellReference last = refs.get(i).getLastCell();
            int height = last.getRow() - first.getRow() + 1;
            widths[i] = last.getCol() - first.getCol() + 1;
            if (i == 0) {
                totalHeight = height;
            } else if (totalHeight != height) {
                throw new TestDataException("Areas in split named range " + name + " should have the same height");
            }
            totalWidth += widths[i];
        }

        int cx, cy, accWidth = 0;
        Cell [][] cellArray = new Cell[totalHeight][totalWidth];
        for (int i = 0; i < refs.size(); i++) {
            CellReference[] refCells = refs.get(i).getAllReferencedCells();
            cx = cy = 0;
            for (CellReference cr : refCells) {
                if (cx >= widths[i]) {
                    cx = 0;
                    cy++;
                }

                cellArray[cy][cx + accWidth] = currentSheet.getRow(cr.getRow()).getCell(cr.getCol());
                cx++;

                if (!currentSheet.getSheetName().equals(cr.getSheetName())) {
                    throw new TestDataException("Cells in named range " + name + " should be on the same sheet");
                }
            }
            accWidth += widths[i];
        }

        return cellArray;
    }

    private Object extractCellValue(Cell cell, FormulaEvaluator evaluator) {
        String cellValue = getCellValue(cell, evaluator);
        if (StringUtils.isBlank(cellValue)) {
            return cellValue;
        } else {
            //	escape ARRAY_SEPARATOR by doubling
            String [] arr = cellValue.split(String.format("(?<!\\%1$s)\\%1$s(?!\\%1$s)", ARRAY_SEPARATOR));
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].replace(ARRAY_SEPARATOR + ARRAY_SEPARATOR, ARRAY_SEPARATOR);
            }
            if (arr.length == 1) {
                return arr[0];
            } else {
                return Arrays.asList(arr);
            }
        }
    }

    @Override
    protected TestData spawn() {
        XLSDataProvider td = new XLSDataProvider();
        td.groupPath = this.groupPath;
        td.collectionName = this.collectionName;
        return td;
    }
}
