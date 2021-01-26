package help.utils.excel.bind.cache;

import help.utils.excel.ExcelFile;

public class UnmarshallingCache extends TableClassesCache<UnmarshallingClassInfo>{
    private final boolean strictMatchBinding;

    public UnmarshallingCache(ExcelFile excelFile, boolean strictMatchBinding) {
        super(excelFile);
        this.strictMatchBinding = strictMatchBinding;
    }

    public boolean isStrictMatchBinding() {
        return this.strictMatchBinding;
    }

    @Override
    protected UnmarshallingClassInfo getTableClassInfoInstance(Class<?> tableClass) {
        return new UnmarshallingClassInfo(tableClass, getExcelFile(), isStrictMatchBinding());
    }
}
