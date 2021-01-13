package help.ws.rest.features;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import help.data.TestData;
import help.data.TestDataException;

public class TestDataSerializer extends JsonSerializer<TestData> {
    public static final TestDataSerializer INSTANCE = new TestDataSerializer();

    @Override
    public void serialize(TestData value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        for (String key : value.getKeys()) {
            try {
                // For TestData
                TestData td = value.getTestData(key);
                gen.writeFieldName(key);
                serialize(td, gen, serializers);

            } catch (TestDataException tdeTD) {
                try {
                    // For TestDataList
                    List<TestData> tdList = value.getTestDataList(key);

                    gen.writeFieldName(key);
                    gen.writeStartArray();

                    for (TestData td : tdList) {
                        gen.writeObject(td);
                    }

                    gen.writeEndArray();

                } catch (TestDataException tdeTDL) {
                    try {
                        // For Strings
                        String tdValue = value.getValue(key);
                        serializers.defaultSerializeField(key, tdValue, gen);

                    } catch (TestDataException tdeL) {
                        // For List
                        List<String> list = value.getList(key);
                        serializers.defaultSerializeField(key, list, gen);
                    }
                }
            }
        }
        gen.writeEndObject();
    }
}
