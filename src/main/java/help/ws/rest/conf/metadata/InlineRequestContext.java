package help.ws.rest.conf.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import help.application.users.User;

public class InlineRequestContext implements IRequestContext{
    private User user;
    private MultivaluedMap<String, String> headers;
    private Map<String, String> pathParams;
    private MultivaluedMap<String, String> queryParams;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Map<String, String> getPathParams() {
        return pathParams;
    }

    @Override
    public MultivaluedMap<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return headers;
    }

    public static InlineRequestContext empty(){
        return new InlineRequestContext();
    }

    public static Builder builder(){
        return  new InlineRequestContext().new Builder();
    }

    public class Builder{

        public Builder user(String name, String pass){
            user = new User(name, pass);
            return this;
        }

        public Builder user(User userP){
            user = userP;
            return this;
        }

        /**
         * Add header to the request.<br/>
         * In case of adding header value with already existing key value will not be replaced and <br/>
         * will be present as comma-separated list <br/>
         *
         * Example <br/>
         * {@code header("HeaderKey", "A").header("HeaderKey", "B").header("HeaderKey", "C") -> A,B,C}
         *
         * @param name header name
         * @param value header value
         * @return current builder instance
         */
        public Builder header(String name, String value){
            if(value != null) {
                headers = Optional.ofNullable(headers).orElseGet(MultivaluedHashMap::new);
                headers.add(name, value);
            }
            return this;
        }

        public Builder headers(Map<String, String> params){
            params.forEach((name, value) -> {
                if (value != null) {
                    headers = Optional.ofNullable(headers).orElseGet(MultivaluedHashMap::new);
                    headers.add(name, value);
                }
            });
            return this;
        }

        /**
         * Resolve path param (marked with {} at configuration file)
         * @param name path parameter name
         * @param value path parameter value
         * @return current builder instance
         */
        public Builder pathParam(String name, String value){
            if(value != null) {
                pathParams = Optional.ofNullable(pathParams).orElseGet(HashMap::new);
                pathParams.put(name, value);
            }
            return this;
        }

        /**
         * Add query parameter to the request.<br/>
         * In case of adding query value for already existing key query value will not be replaced<br/>
         *
         * Example <br/>
         * {@code queryParam("QueryKey", "A").queryParam("QueryKey", "B").queryParam("HeaderKey", "C") ->
         * ?QueryKey=A&QueryKey=B&QueryKey=C}
         * @param name query parameter name
         * @param value query parameter value
         * @return current builder instance
         */
        public Builder queryParam(String name, String value){
            if(value != null) {
                queryParams = Optional.ofNullable(queryParams).orElseGet(MultivaluedHashMap::new);
                queryParams.add(name, value);
            }
            return this;
        }

        public Builder queryParams(Map<String, String> params) {
            params.forEach((name, value) -> {
                if (value != null) {
                    queryParams = Optional.ofNullable(queryParams).orElseGet(MultivaluedHashMap::new);
                    queryParams.add(name, value);
                }
            });
            return this;
        }

        public Builder queryParams(MultivaluedMap<String, String> params) {
            params.forEach((name, value) -> {
                if (value != null) {
                    queryParams = Optional.ofNullable(queryParams).orElseGet(MultivaluedHashMap::new);
                    queryParams.put(name, value);
                }
            });
            return this;
        }

        public InlineRequestContext build(){
            return InlineRequestContext.this;
        }
    }
}
