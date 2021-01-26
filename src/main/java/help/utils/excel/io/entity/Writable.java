package help.utils.excel.io.entity;

import java.io.File;
import help.utils.excel.ExcelFile;

public interface Writable {
    ExcelFile getExcelFile();

    default ExcelFile save() {
        return getExcelFile().save();
    }

    default ExcelFile save(File destinationFile) {
        return getExcelFile().save(destinationFile);
    }

    default void close() {
        getExcelFile().close();
    }

    default ExcelFile saveAndClose() {
        return getExcelFile().saveAndClose();
    }

    default ExcelFile saveAndClose(File destinationFile) {
        return getExcelFile().saveAndClose(destinationFile);
    }
}
