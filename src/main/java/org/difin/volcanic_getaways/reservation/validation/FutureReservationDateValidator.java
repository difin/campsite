package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.utils.DateConversionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FutureReservationDateValidator implements ConstraintValidator<FutureReservationDate, String> {
    public void initialize(FutureReservationDate constraint) {
    }

    public boolean isValid(String date, ConstraintValidatorContext context) {

        try{
            return DateConversionUtils.stringToDate(date).isAfter(LocalDate.now().plusDays(1));
        } catch (DateTimeParseException e) {
            // if date is not parsable, another more precise validation error will be shows to user
            return true;
        }
    }
}
