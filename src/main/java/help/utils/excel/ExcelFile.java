package help.utils.excel;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import help.utils.excel.io.celltype.CellType;
import help.utils.excel.io.entity.area.ExcelCell;
import help.utils.excel.io.entity.area.sheet.ExcelSheet;

public class ExcelFile implements Closeable{
    protected static Logger log = LoggerFactory.getLogger(ExcelFile.class);

    private final File file;
    private Workbook workbook;
    private boolean isOpened;
    private List<CellType<?>> allowableCellTypes;
    private List<ExcelSheet> sheets;
    private FormulaEvaluator evaluator;

    /**
     * File based constructor with basic set of {@link CellType}s
     *
     * @param file Excel file to read/write data to/from it. If file does not exist it will be created
     */
    public ExcelFile(File file) {
        this(file, ExcelCell.getBaseTypes());
    }

    /**
     * InputStream based constructor with basic set of {@link CellType}s
     *
     * @param inputStream input stream to read excel data from it. Stream will be closed within constructor right after {@link Workbook} object creation
     */
    public ExcelFile(InputStream inputStream) {
        this(inputStream, ExcelCell.getBaseTypes());
    }

    /**
     * File based constructor with provided set of allowable {@link CellType}s
     *
     * @param file Excel file to read/write data to/from it. If file does not exist it will be created
     * @param allowableCellTypes set of {@link CellType}s to be used for parsing/writing excel data
     */
    public ExcelFile(File file, List<CellType<?>> allowableCellTypes) {
        this.file = file;
        this.workbook = getWorkbook(file);
        this.isOpened = true;
        this.allowableCellTypes = allowableCellTypes.stream().distinct().collect(Collectors.toList());
        this.sheets = new ArrayList<>(this.workbook.getNumberOfSheets());
    }

    /**
     * InputStream based constructor provided set of allowable {@link CellType}s
     *
     * @param inputStream input stream to read excel data from it. Stream will be closed within constructor right after {@link Workbook} object creation
     * @param allowableCellTypes set of {@link CellType}s to be used for parsing/writing excel data
     */
    public ExcelFile(InputStream inputStream, List<CellType<?>> allowableCellTypes) {
        this.file = null;
        this.workbook = getWorkbook(inputStream);
        IOUtils.closeQuietly(inputStream);
        this.isOpened = true;
        this.allowableCellTypes = allowableCellTypes.stream().distinct().collect(Collectors.toList());
        this.sheets = new ArrayList<>(this.workbook.getNumberOfSheets());
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    public File getFile() {
        return this.file;
    }

    public List<CellType<?>> getCellTypes() {
        return Collections.unmodifiableList(this.allowableCellTypes);
    }

    public List<ExcelSheet> getSheets() {
        if (this.sheets.isEmpty()) {
            for (Sheet sheet : getWorkbook()) {
                int sheetIndex = getWorkbook().getSheetIndex(sheet.getSheetName()) + 1;
                this.sheets.add(createSheet(sheet, sheetIndex));
            }
        }
        return Collections.unmodifiableList(this.sheets);
    }

    public FormulaEvaluator getFormulaEvaluator() {
        if (this.evaluator == null) {
            this.evaluator = getWorkbook().getCreationHelper().createFormulaEvaluator();
        }
        return evaluator;
    }

    public List<String> getSheetsNames() {
        return getSheets().stream().map(ExcelSheet::getSheetName).collect(Collectors.toList());
    }

    public List<Integer> getSheetsIndexes() {
        return getSheets().stream().map(ExcelSheet::getSheetIndex).collect(Collectors.toList());
    }

    public int getSheetsNumber() {
        return getSheets().size();
    }

    public ExcelSheet getFirstSheet() {
        return getSheetsNumber() == 0 ? null : getSheet(getSheetsIndexes().get(0));
    }

    public ExcelSheet getLastSheet() {
        return getSheetsNumber() == 0 ? null : getSheet(getSheetsIndexes().get(getSheetsNumber() - 1));
    }

    protected Workbook getWorkbook() {
        if (!isOpened()) {
            if (!initializedFromFile()) {
                throw new ExcelProcessingException("Unable to reopen workbook which was initialized with InputStream and closed");
            }
            this.workbook = getWorkbook(getFile());
            this.isOpened = true;
        }
        return this.workbook;
    }

    @Override
    public String toString() {
        return "ExcelFile{" +
                "isOpened=" + isOpened() +
                ", initializedFrom=" + getWorkbookInitializationSource() +
                ", sheetsNumber=" + getSheetsNumber() +
                ", sheetsNames=" + getSheetsNames() +
                ", allowableCellTypes=" + getCellTypes() +
                '}';
    }

    @Override
    public void close() {
        if (isOpened()) {
            try {
                getWorkbook().close();
                this.isOpened = false;
            } catch (IOException e) {
                throw new ExcelProcessingException(String.format("Closing of excel workbook initialized from %s has been failed", getWorkbookInitializationSource()), e);
            }
        }
    }

    public boolean initializedFromFile() {
        return this.file != null;
    }

    public boolean hasSheet(String sheetName) {
        return getSheetsNames().contains(sheetName);
    }

    public boolean hasSheet(int sheetIndex) {
        return getSheetsIndexes().contains(sheetIndex);
    }

    public ExcelSheet getSheet(int sheetIndex) {
        for (ExcelSheet sheet : getSheets()) {
            if (sheet.getSheetIndex() == sheetIndex) {
                return sheet;
            }
        }
        throw new ExcelProcessingException(String.format("There is no sheet with %s index", sheetIndex));
    }

    public ExcelSheet getSheet(String sheetName) {
        for (ExcelSheet sheet : getSheets()) {
            if (sheet.getSheetName().equals(sheetName)) {
                return sheet;
            }
        }
        throw new ExcelProcessingException(String.format("There is no sheet with \"%s\" name", sheetName));
    }

    public ExcelSheet getSheetContains(String sheetNamePattern) {
        for (ExcelSheet sheet : getSheets()) {
            if (sheet.getSheetName().contains(sheetNamePattern)) {
                return sheet;
            }
        }
        throw new ExcelProcessingException(String.format("There is no sheet which contains \"%s\" name", sheetNamePattern));
    }

    public List<ExcelSheet> getSheetsContains(String sheetNamePattern) {
        List<ExcelSheet> sheets = new ArrayList<>();
        for (ExcelSheet sheet : getSheets()) {
            if (sheet.getSheetName().contains(sheetNamePattern)) {
                sheets.add(sheet);
            }
        }
        return sheets;
    }

    public ExcelSheet addSheet(String sheetName) {
        int newSheetIndex = getLastSheet() == null ? 1 : getLastSheet().getSheetIndex() + 1;
        ExcelSheet newSheet = createSheet(getWorkbook().createSheet(sheetName), newSheetIndex);
        this.sheets.add(newSheet);
        return newSheet;
    }

    public ExcelFile registerCellType(List<CellType<?>> cellTypes) {
        this.allowableCellTypes.addAll(cellTypes);
        this.allowableCellTypes = this.allowableCellTypes.stream().distinct().collect(Collectors.toList());
        getSheets().forEach(s -> s.registerCellType(cellTypes));
        return this;
    }

    public ExcelFile save() {
        if (!initializedFromFile()) {
            //TODO-dchubkov: maybe better to save to working directoty with some filename?
            log.warn("ExcelFile was initialized from InputStream therefore source file does not exist and saving to it has been skipped");
            return this;
        }
        return save(getFile());
    }

    public ExcelFile save(File destinationFile) {
        File writeToFile;
        boolean overwriteOpenedFile = false;

        if (initializedFromFile() && destinationFile.exists() && getFile().equals(destinationFile)) {
            writeToFile = new File(FilenameUtils.removeExtension(destinationFile.getAbsolutePath()) + "_TEMP." + FilenameUtils.getExtension(destinationFile.getName()));
            overwriteOpenedFile = true;
        } else {
            writeToFile = destinationFile;
        }

        Workbook wb = getWorkbook();
        try (FileOutputStream outputStream = new FileOutputStream(writeToFile)) {
            wb.write(outputStream);
        } catch (IOException e) {
            close();
            throw new ExcelProcessingException(String.format("Writing to excel file \"%s\" has been failed", writeToFile.getAbsolutePath()), e);
        }

        if (overwriteOpenedFile) {
            close();
            if (!destinationFile.delete()) {
                log.warn("Can't delete original file \"{}\"", destinationFile.getAbsolutePath());
            }
            if (!writeToFile.renameTo(destinationFile)) {
                log.warn("Can't rename temp file \"{}\" to \"{}\"", writeToFile.getAbsolutePath(), destinationFile.getAbsolutePath());
            }
        }

        return this;
    }

    public ExcelFile saveAndClose() {
        if (!initializedFromFile()) {
            log.warn("ExcelFile was initialized from InputStream therefore source file does not exist and saving to it has been skipped");
            return this;
        }
        return saveAndClose(getFile());
    }

    public ExcelFile saveAndClose(File destinationFile) {
        save(destinationFile);
        close();
        return this;
    }

    protected ExcelSheet createSheet(Sheet sheet, int sheetIndex) {
        return new ExcelSheet(sheet, sheetIndex, this, getCellTypes());
    }

    private Workbook getWorkbook(InputStream inputStream) {
        try {
            return WorkbookFactory.create(inputStream);
        } catch (EncryptedDocumentException | IOException e) {
            throw new ExcelProcessingException("Workbook creation from InputStream has been failed.", e);
        }
    }

    private Workbook getWorkbook(File file) {
        if (!file.exists()) {
            String fileExtension = FilenameUtils.getExtension(file.getName());
            switch (fileExtension) {
                case "xls":
                    return new HSSFWorkbook();
                case "xlsx":
                    return new XSSFWorkbook();
                default:
                    throw new ExcelProcessingException(String.format("Unable to create Workbook for \"%1$s\" file: invalid file extension \"%2$s\"", file.getAbsolutePath(), fileExtension));
            }
        }

        String errorMessage = String.format("Workbook creation from file \"%s\" has been failed.", file.getAbsolutePath());
        try {
            return WorkbookFactory.create(file);
        } catch (FileNotFoundException fnfe) {
            if (fnfe.getMessage().endsWith("(The process cannot access the file because it is being used by another process)")) {
                log.warn("Unable to create Workbook because \"{}\" file is already opened or used by another process, trying to create Workbook from input stream", file.getAbsolutePath());
                try (InputStream inputStream = new FileInputStream(file)) {
                    return getWorkbook(inputStream);
                } catch (ExcelProcessingException | IOException ioe) {
                    throw new ExcelProcessingException(errorMessage, ioe);
                }
            }
            throw new ExcelProcessingException(errorMessage, fnfe);
        } catch (EncryptedDocumentException | IOException e) {
            throw new ExcelProcessingException(errorMessage, e);
        }
    }

    private String getWorkbookInitializationSource() {
        return initializedFromFile() ? "file with path \"" + getFile().getAbsolutePath() + "\"" : "InputStream";
    }
}
