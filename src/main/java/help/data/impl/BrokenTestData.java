package help.data.impl;

import java.util.List;
import java.util.Set;
import help.data.DataWriter;
import help.data.TestData;
import help.data.TestDataException;

public class BrokenTestData extends TestData {
    private TestDataException exception;

    public BrokenTestData(String errorMessage, Throwable cause) {
        super(null, null);
        exception = new TestDataException(errorMessage, cause);
    }

    @Override
    protected ValueContainer getByPath(int startIndex, String... keys) {
        throw exception;
    }

    @Override
    public String toString() {
        return "<Broken test data>";
    }

    @Override
    public Set<String> getKeys() {
        throw exception;
    }

    @Override
    public boolean containsKey(String key) {
        throw exception;
    }

    @Override
    public TestData adjust(String key, String value) {
        throw exception;
    }

    @Override
    public TestData adjust(String key, TestData nestedData) {
        throw exception;
    }

    @Override
    public TestData adjust(String key, List<?> list) {
        throw exception;
    }

    @Override
    public TestData adjust(TestData adjustment) {
        throw exception;
    }

    @Override
    protected TestData putAdjustment(String key, Object value) {
        throw exception;
    }

    @Override
    public TestData removeAdjustment(String keyPath) {
        throw exception;
    }

    @Override
    public TestData purgeAdjustments() {
        throw exception;
    }

    @Override
    public void dump(DataWriter dw) {
        throw exception;
    }

    @Override
    public void dump(String groupName, DataWriter dw) {
        throw exception;
    }

    @Override
    protected TestData spawn() {
        throw exception;
    }
}
