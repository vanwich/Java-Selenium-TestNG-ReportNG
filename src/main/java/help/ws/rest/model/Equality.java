package help.ws.rest.model;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({FIELD})
public @interface Equality {
    /**
     * Define if field will take part in equality calculation
     * Default value is TRUE
     * @return true if field must be considered and false otherwise.
     */
    boolean include() default true;
}
