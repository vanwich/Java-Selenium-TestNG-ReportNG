package help.utils.excel.bind.cache;

import java.util.List;
import help.utils.excel.ExcelFile;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.bind.ReflectionHelper;

public class MarshallingCache extends TableClassesCache<MarshallingClassInfo>{
    public MarshallingCache(ExcelFile excelFile) {
        super(excelFile);
    }

    @Override
    protected MarshallingClassInfo getTableClassInfoInstance(Class<?> tableClass) {
        return new MarshallingClassInfo(tableClass, getExcelFile());
    }

    public MarshallingClassInfo of(Object tableObject) {
        Class<?> tableClass;
        if (List.class.isAssignableFrom(tableObject.getClass())) {
            if (((List<?>) tableObject).isEmpty()) {
                throw new ExcelProcessingException("Ubable to get table class cache with empty rows objects list");
            }
            tableClass = ((List<?>) tableObject).get(0).getClass();
        } else {
            tableClass = tableObject.getClass();
        }

        MarshallingClassInfo tableClassInfo = of(tableClass);
        if (!tableClassInfo.hasRowsObjects()) {
            tableClassInfo.setRowsObjects(ReflectionHelper.getValueAsList(tableObject));
        }
        return tableClassInfo;
    }
}
