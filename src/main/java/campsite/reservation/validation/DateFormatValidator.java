package campsite.reservation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<DateFormatValid, LocalDate> {

    public void initialize(DateFormatValid constraint) {
    }

    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        boolean isValid = true;

        try {
            date.format(formatter);
        }
        catch (DateTimeParseException e) {
            isValid = false;
        }

        return isValid;
    }
}
