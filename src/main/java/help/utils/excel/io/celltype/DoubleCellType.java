package help.utils.excel.io.celltype;

public class DoubleCellType extends NumberCellType<Double>{
    public DoubleCellType(Class<Double> endType) {
        super(endType);
    }

    @Override
    protected Double parseText(String t) {
        return Double.valueOf(t);
    }

    @Override
    protected Double parseDouble(double d) {
        return d;
    }
}
