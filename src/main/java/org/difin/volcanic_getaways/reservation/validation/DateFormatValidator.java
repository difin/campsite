package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.service.common.DateConversionService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<DateFormatValid, String> {

    public void initialize(DateFormatValid constraint) {
    }

    public boolean isValid(String date, ConstraintValidatorContext context) {

        boolean isValid = true;

        try {
            DateConversionService.stringToDate(date);
        }
        catch (DateTimeParseException e) {
            isValid = false;
        }

        return isValid;
    }
}
