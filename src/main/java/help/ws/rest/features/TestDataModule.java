package help.ws.rest.features;

import com.fasterxml.jackson.databind.module.SimpleModule;
import help.data.impl.JSONDataProvider;
import help.data.impl.SimpleDataProvider;
import help.data.impl.XLSDataProvider;
import help.data.impl.YAMLDataProvider;

public class TestDataModule extends SimpleModule {
    public TestDataModule() {
        addSerializer(JSONDataProvider.class, TestDataSerializer.INSTANCE);
        addSerializer(YAMLDataProvider.class, TestDataSerializer.INSTANCE);
        addSerializer(SimpleDataProvider.class, TestDataSerializer.INSTANCE);
        addSerializer(XLSDataProvider.class, TestDataSerializer.INSTANCE);
    }
}
