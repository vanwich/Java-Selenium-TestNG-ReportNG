package help.ws.rest.filter;

import static java.lang.String.format;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import help.config.ClassConfigurator;
import help.ws.rest.conf.client.ConfigurationFactory;
import help.ws.rest.model.Equality;
import help.ws.rest.model.Model;

public class EqualityFilter extends AbstractFilter{
    /**
     * Condition to assert that field is considered on equality. </br>
     * Default strategy is: </br>
     *
     * {@link Equality} is not set = field is ON for comparison
     * {@link Equality} is set && include is true = field is ON for comparison
     * {@link Equality} is set && include is false = field is OFF for comparison
     *
     */
    @ClassConfigurator.Configurable(byClassName = true)
    private static Function<PropertyWriter,Boolean> conditionFunction = writer ->
            Objects.isNull(writer.getAnnotation(Equality.class)) || writer.getAnnotation(Equality.class).include();

    static {
        ClassConfigurator configurator = new ClassConfigurator(EqualityFilter.class);
        configurator.applyConfiguration();
    }

    protected boolean getCondition(PropertyWriter writer) {
        return conditionFunction.apply(writer);
    }

    public String filterModel(Model model) {
        FilterProvider filter = new SimpleFilterProvider().addFilter(NAME, this);
        try {

            return ConfigurationFactory.getDefault().getObjectMapper().writer(filter).writeValueAsString(model);
        } catch (IOException e) {
            throw new IllegalStateException(
                    format("Can't apply filter %s to the model", this.getClass().getSimpleName()), e);
        }
    }

    public FilterProvider getFilterProvider() {
        return new SimpleFilterProvider().addFilter(AbstractFilter.NAME, this);
    }
}
