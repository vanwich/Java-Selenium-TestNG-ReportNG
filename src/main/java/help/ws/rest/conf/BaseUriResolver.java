package help.ws.rest.conf;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.ImmutableMap;
import help.application.ApplicationFactory;

public class BaseUriResolver extends DefaultUriResolver{
    private static Map<Object, Object> predefinedURL;

    static {
        predefinedURL = ImmutableMap.builder()
                .put("bpmdev-rs", "/services/bpm-rs/v1/work/dev")
                .build();
    }

    public String getRestServiceTarget(String name) {
        String propertyTarget = super.getRestServiceTarget(name);

        if(name.startsWith("DXP")){
            return ApplicationFactory.getInstance().getDxpApplication().formatUrl();
        }

        if (!StringUtils.isEmpty(propertyTarget)) {
            return propertyTarget;
        }

        if (predefinedURL.containsKey(name)) {
            return ApplicationFactory.getInstance().getMainApplication().formatUrl() + predefinedURL.get(name);
        }

        if("test-data-management".equalsIgnoreCase(name)){
            return ApplicationFactory.getInstance().getMainApplication().formatUrl().replace("/hot-app", "") + "/swagger-spring-1.0.0/testdata";
        }

        return ApplicationFactory.getInstance().getMainApplication().formatUrl() + String.format("/services/%s/v1", name);
    }
}
