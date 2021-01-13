package help.ws.rest.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public abstract class AbstractFilter extends SimpleBeanPropertyFilter {
    public static final String NAME = "FILTER";

    @Override
    public void serializeAsField
            (Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer)
            throws Exception {
        if (getCondition(writer)) {
            writer.serializeAsField(pojo, jgen, provider);
        }
    }

    protected abstract boolean getCondition(PropertyWriter writer);

    @Override
    protected boolean include(BeanPropertyWriter writer) {
        return true;
    }

    @Override
    protected boolean include(PropertyWriter writer) {
        return true;
    }
}
