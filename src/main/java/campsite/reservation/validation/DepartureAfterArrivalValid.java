package campsite.reservation.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DepartureAfterArrivalValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
@Documented
public @interface DepartureAfterArrivalValid {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String message();
}
