package help.ws.rest.features.mapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.zalando.jackson.datatype.money.MoneyModule;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import help.ws.rest.features.TestDataModule;

@Provider
public abstract class AbstractContextResolver implements ContextResolver<ObjectMapper> {
    protected ObjectMapper mapper = defineMapper();

    public AbstractContextResolver() {
        mapper.registerModule(new JodaModule());
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new TestDataModule());
        mapper.registerModule(new MoneyModule());
        mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        mapper.setDefaultMergeable(true);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return getObjectMapper();
    }

    public ObjectMapper getObjectMapper() {
        return mapper;
    }

    protected abstract ObjectMapper defineMapper();
}
