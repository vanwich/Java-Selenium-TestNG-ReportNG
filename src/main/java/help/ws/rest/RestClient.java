package help.ws.rest;

import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import help.config.ClassConfigurator;
import help.config.ClassConfigurator.Configurable;
import help.exceptions.JstrException;
import help.ws.rest.conf.AbstractUriResolver;
import help.ws.rest.conf.DefaultUriResolver;
import help.ws.rest.conf.IRestServiceMetaData;
import help.ws.rest.conf.client.ConfigurationFactory;
import help.ws.rest.conf.client.DefaultConfiguration;
import help.ws.rest.conf.client.IClientConfiguration;
import help.ws.rest.conf.metadata.IRequestContext;
import help.ws.rest.conf.metadata.InlineRequestContext;
import help.ws.rest.conf.metadata.ServiceMetaData;
import help.ws.rest.conf.metadata.WebTargetResolver;

public class RestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);

    @Configurable
    private static  String configurationPathTmpl = "rest/%s.configuration";
    @Configurable(byClassName = true)
    private static AbstractUriResolver uriResolver = new DefaultUriResolver();
    @Configurable(byClassName = true)
    private static  IRestServiceMetaData defaultMetaData = new ServiceMetaData();

    public enum HttpMethod {GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH, TRACE}

    private Client client;
    private WebTarget rootTarget;
    private final IClientConfiguration configuration;
    private IRestServiceMetaData metaData = defaultMetaData;

    static {
        ClassConfigurator configurator = new ClassConfigurator(RestClient.class);
        configurator.applyConfiguration();
    }

    public RestClient(String name, IClientConfiguration configuration) {
        this.configuration = configuration;
        client = ClientBuilder.newBuilder().sslContext(getSSLContext()).withConfig(configuration.getConfig()).build();
        rootTarget = client.target(uriResolver.getRestServiceTarget(name));
        loadServiceConfiguration(name);
    }

    public Client getClient() {
        return client;
    }

    public IClientConfiguration getConfiguration() {
        return configuration;
    }

    private SSLContext getSSLContext() {
        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            LOGGER.error("Cannot get SSL context", e);
        }

        return sslContext;
    }

    public Response processRequest(String requestAlias, HttpMethod httpMethod, Entity<?> entity, IRequestContext reqParam) {
        String targetPath = metaData.getTarget(requestAlias) == null ? requestAlias : metaData.getTarget(requestAlias);
        Response response = new WebTargetResolver(rootTarget.path(targetPath), reqParam).resolve().method(httpMethod.toString(), entity);
        response.bufferEntity();
        return response;
    }

    public Response processRequest(String requestAlias, HttpMethod httpMethod, Entity<?> entity) {
        return processRequest(requestAlias, httpMethod, entity, InlineRequestContext.empty());
    }

    public Response processRequest(String requestAlias, HttpMethod httpMethod, Object model) {
        return processRequest(requestAlias, httpMethod, Entity.entity(model, getMediaType(requestAlias, httpMethod)), InlineRequestContext.empty());
    }

    public Response processRequest(String requestAlias, HttpMethod httpMethod, Object model, IRequestContext reqParam) {
        return processRequest(requestAlias, httpMethod, Entity.entity(model, getMediaType(requestAlias, httpMethod)), reqParam);
    }

    private void loadServiceConfiguration(String name) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format(configurationPathTmpl, name))) {
            metaData = ConfigurationFactory.get(DefaultConfiguration.class).getObjectMapper().readValue(is, metaData.getClass());
        } catch (MismatchedInputException | NullPointerException | IllegalArgumentException mie) {
            LOGGER.error(String.format("Configuration file for service '%s' not found. RestClient can only be used for a direct request without aliases.", name));
        } catch (Exception e) {
            throw new JstrException("Unable to load service configuration", e);
        }
    }

    private MediaType getMediaType(String requestAlias, HttpMethod httpMethod) {
        return Optional.ofNullable(metaData.getMediaType(requestAlias, httpMethod))
                .orElseThrow(() -> new IllegalArgumentException(String.format("[%s] metadata is not defined for alias [%s]", httpMethod, requestAlias)));
    }

    public void setRootTarget(WebTarget rootTarget) {
        this.rootTarget = rootTarget;
    }

    public WebTarget getRootTarget() {
        return rootTarget;
    }
}
