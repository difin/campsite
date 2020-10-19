package org.difin.volcanic_getaways.reservation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateWithinOneMonthValidator implements ConstraintValidator<DateWithinOneMonth, LocalDate> {
    public void initialize(DateWithinOneMonth constraint) {
    }

    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {

        // End date is not in range
        return date.isBefore(LocalDate.now().plusMonths(1).plusDays(2));
    }
}
