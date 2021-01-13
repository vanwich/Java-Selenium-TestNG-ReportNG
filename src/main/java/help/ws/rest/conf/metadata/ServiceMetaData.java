package help.ws.rest.conf.metadata;

import java.util.Map;
import java.util.Optional;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import help.ws.rest.RestClient;
import help.ws.rest.conf.IRestServiceMetaData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceMetaData implements IRestServiceMetaData {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMetaData.class);

    @JsonProperty("restTargets")
    private Map<String, Endpoint> restEndpoints;

    @Override
    @JsonIgnore
    public String getTarget(String requestAlias) {
        if (restEndpoints == null || restEndpoints.get(requestAlias) == null) {
            LOGGER.warn(String.format("Metadata is not defined for alias [%s]", requestAlias));
            return null;
        }
        return restEndpoints.get(requestAlias).getTarget();
    }

    @Override
    @JsonIgnore
    public MediaType getMediaType(String requestAlias, RestClient.HttpMethod method) {
        return MediaType.valueOf(Optional.ofNullable(Optional.ofNullable(restEndpoints.get(requestAlias)).orElseThrow(() ->
                new IllegalArgumentException(String.format("Metadata is not defined for alias [%s]", requestAlias))).getMethodAttributes().get(method.name())).
                orElseThrow(() -> new IllegalArgumentException(String.format("Method [%s] is not defined for alias [%s]", method.name(), requestAlias))).getMediaType());
    }

    public Map<String, Endpoint> getRestEndpoints() {
        return restEndpoints;
    }

    public void setRestEndpoints(Map<String, Endpoint> restEndpoints) {
        this.restEndpoints = restEndpoints;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Endpoint {
        @JsonProperty("methods")
        private Map<String, MethodAttributes> methodAttributes;
        private String target;

        public void setMethodAttributes(Map<String, MethodAttributes> methodAttributes) {
            this.methodAttributes = methodAttributes;
        }

        public Map<String, MethodAttributes> getMethodAttributes() {
            return methodAttributes;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MethodAttributes {
        private String mediaType;

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }
    }
}
