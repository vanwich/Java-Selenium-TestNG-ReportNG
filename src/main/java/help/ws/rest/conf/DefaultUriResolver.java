package help.ws.rest.conf;

import help.config.ClassConfigurator;
import help.config.PropertyProvider;

public class DefaultUriResolver extends AbstractUriResolver{
    @ClassConfigurator.Configurable
    private static String endpointPropertyTmpl = "rest.%1$s.endpoint";

    static {
        ClassConfigurator configurator = new ClassConfigurator(DefaultUriResolver.class);
        configurator.applyConfiguration();
    }

    public String getRestServiceTarget(String name){
        return PropertyProvider.getProperty(String.format(endpointPropertyTmpl, name));
    }
}
