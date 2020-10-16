package org.difin.volcanic_getaways.reservation.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = FutureReservationDateValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
public @interface FutureReservationDate {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String message();
}
