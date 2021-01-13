package help.ws.rest.conf.client.conf;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IntegerSerializer extends JsonSerializer<Integer> {
    @Override
    public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumber(value);
    }
}
