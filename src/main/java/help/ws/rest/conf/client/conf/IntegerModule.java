package help.ws.rest.conf.client.conf;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class IntegerModule extends SimpleModule {
    public IntegerModule() {
        addSerializer(Integer.class, new IntegerSerializer());
        addDeserializer(Integer.class, new IntegerDeserializer());
    }}
