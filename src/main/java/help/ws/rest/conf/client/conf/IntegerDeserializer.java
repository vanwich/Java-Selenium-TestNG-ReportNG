package help.ws.rest.conf.client.conf;

import java.io.IOException;
import org.javamoney.moneta.Money;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class IntegerDeserializer extends StdDeserializer<Integer> {
    public IntegerDeserializer() {
        super(Money.class);
    }

    @Override
    public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Double value = jp.readValueAs(Double.class);
        return value.intValue();
    }
}
