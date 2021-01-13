package help.ws.rest.conf.metadata;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class WebTargetResolver {
    private WebTarget webTarget;
    private IRequestContext reqParams;
    private Invocation.Builder builder;

    public WebTargetResolver(WebTarget webTarget, IRequestContext reqParams){
        this.webTarget = webTarget;
        this.reqParams = reqParams;
    }

    public Invocation.Builder resolve(){
        resolveQueryParams(reqParams);
        resolvePathParams(reqParams);
        builder = webTarget.request();
        resolveAuthData(reqParams);
        resolveHeaders(reqParams);
        return builder;
    }

    private void resolvePathParams(IRequestContext reqParam){
        if(null != reqParam.getPathParams() && !reqParam.getPathParams().keySet().isEmpty()){
            reqParam.getPathParams().keySet().forEach(param ->
                    webTarget = webTarget.resolveTemplate(param, reqParam.getPathParams().get(param)));
        }
    }

    private void resolveAuthData(IRequestContext reqParam){
        if (null != reqParam.getUser()) {
            builder = builder
                    .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, reqParam.getUser().getLogin())
                    .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, reqParam.getUser().getPassword());
        }
    }

    private void resolveQueryParams(IRequestContext reqParam){
        if (null != reqParam.getQueryParams()  && !reqParam.getQueryParams().keySet().isEmpty()) {
            reqParam.getQueryParams().keySet().forEach(queryKey ->
                    reqParam.getQueryParams().get(queryKey).forEach(queryValue ->
                            webTarget = webTarget.queryParam(queryKey, queryValue)));
        }
    }

    private void resolveHeaders(IRequestContext reqParam){
        if (null != reqParam.getHeaders()  && !reqParam.getHeaders().keySet().isEmpty()) {
            reqParam.getHeaders().keySet().forEach(headerKey ->
                    reqParam.getHeaders().get(headerKey).forEach(headerValue ->
                            builder = builder.header(headerKey, headerValue)));
        }
    }
}
